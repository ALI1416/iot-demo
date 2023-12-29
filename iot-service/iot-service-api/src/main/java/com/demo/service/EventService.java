package com.demo.service;

import cn.z.clock.Clock;
import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.util.TimestampUtils;
import cn.z.websocket.WebSocketTemp;
import com.demo.base.ServiceBase;
import com.demo.constant.MongoConstant;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.EventDao;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <h1>事件</h1>
 *
 * <p>
 * createDate 2023/11/10 16:51:59
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class EventService extends ServiceBase {

    private final EventDao eventDao;
    private final GatewayService gatewayService;
    private final WebSocketTemp webSocketTemp;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.EVENT_PREFIX + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String eventJson
    ) {
        log.info("主题:[{}] ,消息:[{}]", topic, eventJson);
        // 事件JSON处理
        eventJsonHandle(gatewaySn, deviceSn, commandCode, eventJson);
    }

    /**
     * 事件JSON处理
     */
    private void eventJsonHandle(int gatewaySn, int deviceSn, int commandCode, String eventJson) {
        // 参数修正
        if (eventJson.isEmpty()) {
            eventJson = null;
        }
        // 事件转换
        Map.Entry<Integer, Protocol.Data> eventMap = Protocol.eventConvert(commandCode, eventJson);
        // 协议格式错误或转换失败或数据检查未通过
        if (eventMap == null || (eventMap.getValue() != null && eventMap.getValue().dataCheckNotPass())) {
            log.warn("协议格式错误或转换失败或数据检查未通过 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, eventJson);
            return;
        }
        // 网关序号、设备序号、设备类型不匹配
        if (gatewayService.notValid(gatewaySn, deviceSn, eventMap.getKey())) {
            log.warn("网关序号、设备序号、设备类型不匹配 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, eventJson);
            return;
        }
        ProtocolVo base = new ProtocolVo();
        base.setGatewaySn(gatewaySn);
        base.setDeviceSn(deviceSn);
        base.setCommandCode(commandCode);
        base.setEvent(eventMap.getValue());
        // 插入
        ProtocolVo result = eventDao.insert(base);
        if (result != null) {
            log.info("事件接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.EVENT_PREFIX + gatewaySn + "/" + deviceSn + "/" + commandCode, result.toWebString());
        } else {
            log.warn("事件接收失败:[{}]", base);
        }
    }

    private static Criteria buildCriteria(ProtocolVo base) {
        Criteria criteria = new Criteria();
        if (base.getDeviceSn() != null) {
            criteria.and("deviceSn").is(base.getDeviceSn());
        }
        if (base.getCommandCode() != null) {
            criteria.and("commandCode").is(base.getCommandCode());
        }
        if (base.getErrorCode() != null) {
            criteria.and("errorCode").is(base.getErrorCode());
        }
        if (base.getMonth() != null) {
            criteria.and("month").is(base.getMonth());
        }
        if (base.getDay() != null) {
            criteria.and("day").is(base.getDay());
        }
        if (base.getHour() != null) {
            criteria.and("hour").is(base.getHour());
        }
        if (base.getMinute() != null) {
            criteria.and("minute").is(base.getMinute());
        }
        buildRange(criteria, "createTime", base.getCreateTime(), base.getCreateTimeEnd(), base.getCreateTimeNot());
        return criteria;
    }

    /**
     * 报表查询(倒序)
     *
     * @param createTimeStart 起始时间(包含)
     * @param createTimeEnd   结束时间(不包含)
     * @param commandCode     命令代码
     * @param collectionName  集合名
     * @return List ProtocolVo
     */
    private List<ProtocolVo> find(Timestamp createTimeStart, Timestamp createTimeEnd, int commandCode, String collectionName) {
        Criteria criteria = new Criteria();
        criteria.and("commandCode").is(commandCode);
        criteria.and("createTime").gte(createTimeStart).lt(createTimeEnd);
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Query query = Query.query(criteria).with(sort);
        return eventDao.find(query, collectionName);
    }

    /**
     * 查询 命令代码 不同值
     *
     * @param createTimeStart 起始时间(包含)
     * @param createTimeEnd   结束时间(不包含)
     * @param collectionName  集合名
     * @return List Integer
     */
    private List<Integer> findDistinctCommandCode(Timestamp createTimeStart, Timestamp createTimeEnd, String collectionName) {
        Criteria criteria = new Criteria();
        criteria.and("createTime").gte(createTimeStart).lt(createTimeEnd);
        Query query = Query.query(criteria);
        return eventDao.findDistinct(query, collectionName, "commandCode", Integer.class);
    }

    /**
     * 获取报表
     *
     * @param suffix1         后缀1
     * @param suffix2         后缀2
     * @param createTimeStart 起始时间(包含)
     * @param createTimeEnd   结束时间(不包含)
     */
    private List<Map.Entry<String, ProtocolVo>> getReport(String suffix1, String suffix2, Timestamp createTimeStart, Timestamp createTimeEnd) {
        List<Map.Entry<String, ProtocolVo>> result = new ArrayList<>();
        // 网关序号
        for (Integer gatewaySn : gatewayService.getGatewaySn()) {
            String collectionName1 = gatewaySn + suffix1;
            String collectionName2 = gatewaySn + suffix2;
            // 命令代码
            List<Integer> commandCodeList = findDistinctCommandCode(createTimeStart, createTimeEnd, collectionName1);
            for (Integer commandCode : commandCodeList) {
                List<ProtocolVo> list = find(createTimeStart, createTimeEnd, commandCode, collectionName1);
                if (list.isEmpty()) {
                    continue;
                }
                for (List<ProtocolVo> value : list.stream().collect(Collectors.groupingBy(ProtocolVo::getDeviceSn)).values()) {
                    // 新增报表
                    result.add(new AbstractMap.SimpleEntry<>(collectionName2, value.get(0)));
                }
            }
        }
        return result;
    }

    /**
     * 上一分钟 分钟报表(每分钟的1秒)
     */
    @Async
    @Scheduled(cron = "1 * * * * *")
    public void reportMinute() {
        // 这一分钟的0秒0毫秒
        long timestampEnd = TimestampUtils.getTimestamp(Clock.now(), Calendar.SECOND, true, -1, 0);
        // 上一分钟的0秒0毫秒
        long timestampStart = timestampEnd - TimestampUtils.MILLS_OF_MINUTE;
        // 全部数据(月) [网关序号]_event_[yyyyMM]
        String suffixAll = MongoConstant.EVENT + "_" + DateUtils.getYyyyMm(timestampStart);
        // 分钟报表(年) [网关序号]_event_minute_[yyyy]
        String suffixMinute = MongoConstant.EVENT + MongoConstant.MINUTE + "_" + DateUtils.getYyyy(timestampStart);
        // 获取报表
        List<Map.Entry<String, ProtocolVo>> report = getReport(suffixAll, suffixMinute, new Timestamp(timestampStart), new Timestamp(timestampEnd));
        for (Map.Entry<String, ProtocolVo> entry : report) {
            // 设置月、日、小时、分钟
            ProtocolVo value = entry.getValue();
            Calendar calendar = new Calendar.Builder().setInstant(value.getCreateTime()).build();
            value.setMonth(calendar.get(Calendar.MONTH) + 1);
            value.setDay(calendar.get(Calendar.DAY_OF_MONTH));
            value.setHour(calendar.get(Calendar.HOUR_OF_DAY));
            value.setMinute(calendar.get(Calendar.MINUTE));
            // 插入报表
            eventDao.insertReport(value, entry.getKey());
        }
    }

    /**
     * 上一小时 小时报表(每小时的0分6秒)
     */
    @Async
    // @Scheduled(cron = "6 * * * * *")
    @Scheduled(cron = "6 0 * * * *")
    public void reportHour() {
        // 这一小时的0分0秒0毫秒
        long timestampEnd = TimestampUtils.getTimestamp(Clock.now(), Calendar.MINUTE, true, -1, 0);
        // 上一小时的0分0秒0毫秒
        long timestampStart = timestampEnd - TimestampUtils.MILLS_OF_HOUR;
        String year = "_" + DateUtils.getYyyy(timestampStart);
        // 分钟报表(年) [网关序号]_event_minute_[yyyy]
        String suffixMinute = MongoConstant.EVENT + MongoConstant.MINUTE + year;
        // 小时报表(年) [网关序号]_event_hour_[yyyy]
        String suffixHour = MongoConstant.EVENT + MongoConstant.HOUR + year;
        // 获取报表
        List<Map.Entry<String, ProtocolVo>> report = getReport(suffixMinute, suffixHour, new Timestamp(timestampStart), new Timestamp(timestampEnd));
        for (Map.Entry<String, ProtocolVo> entry : report) {
            // 清空分钟
            ProtocolVo value = entry.getValue();
            entry.getValue().setMinute(null);
            // 插入报表
            eventDao.insertReport(value, entry.getKey());
        }
    }

    /**
     * 上一天 日报表(每天的0时0分11秒)
     */
    @Async
    // @Scheduled(cron = "11 * * * * *")
    @Scheduled(cron = "11 0 0 * * *")
    public void reportDay() {
        // 这一天的0时0分0秒0毫秒
        long timestampEnd = TimestampUtils.getTimestamp(Clock.now(), Calendar.HOUR_OF_DAY, true, -1, 0);
        // 上一天的0时0分0秒0毫秒
        long timestampStart = timestampEnd - TimestampUtils.MILLS_OF_DAY;
        String year = "_" + DateUtils.getYyyy(timestampStart);
        // 小时报表 [网关序号]_event_hour_[yyyy]
        String suffixHour = MongoConstant.EVENT + MongoConstant.HOUR + year;
        // 日报表 [网关序号]_event_day_[yyyy]
        String suffixDay = MongoConstant.EVENT + MongoConstant.DAY + year;
        // 获取报表
        List<Map.Entry<String, ProtocolVo>> report = getReport(suffixHour, suffixDay, new Timestamp(timestampStart), new Timestamp(timestampEnd));
        for (Map.Entry<String, ProtocolVo> entry : report) {
            // 清空小时
            ProtocolVo value = entry.getValue();
            entry.getValue().setHour(null);
            // 插入报表
            eventDao.insertReport(value, entry.getKey());
        }
    }

    /**
     * 上一月 月报表(每月的1日0时0分16秒)
     */
    @Async
    // @Scheduled(cron = "16 * * * * *")
    @Scheduled(cron = "16 0 0 1 * *")
    public void reportMonth() {
        // 这一月的1日0时0分0秒0毫秒
        long timestampEnd = TimestampUtils.getTimestamp(Clock.now(), Calendar.DAY_OF_YEAR, true, -1, 0);
        // 上一月的1日0时0分0秒0毫秒
        long timestampStart = TimestampUtils.getTimestamp(timestampEnd, Calendar.DAY_OF_YEAR, true, Calendar.MONTH, -1);
        String year = "_" + DateUtils.getYyyy(timestampStart);
        // 日报表 [网关序号]_event_day_[yyyy]
        String suffixDay = MongoConstant.EVENT + MongoConstant.DAY + year;
        // 月报表 [网关序号]_event_month_[yyyy]
        String suffixMonth = MongoConstant.EVENT + MongoConstant.MONTH + year;
        // 获取报表
        List<Map.Entry<String, ProtocolVo>> report = getReport(suffixDay, suffixMonth, new Timestamp(timestampStart), new Timestamp(timestampEnd));
        for (Map.Entry<String, ProtocolVo> entry : report) {
            // 清空日
            ProtocolVo value = entry.getValue();
            entry.getValue().setDay(null);
            // 插入报表
            eventDao.insertReport(value, entry.getKey());
        }
    }

}
