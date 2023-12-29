package com.demo.service;

import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.websocket.WebSocketTemp;
import com.demo.base.ServiceBase;
import com.demo.constant.MongoConstant;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.FaultDao;
import com.demo.dao.mongo.FaultDetailDao;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <h1>故障</h1>
 *
 * <p>
 * createDate 2023/12/11 15:37:49
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class FaultService extends ServiceBase {

    private final FaultDao faultDao;
    private final FaultDetailDao faultDetailDao;
    private final GatewayService gatewayService;
    private final WebSocketTemp webSocketTemp;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.FAULT_PREFIX + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String faultArray
    ) {
        log.info("主题:[{}] ,消息:[{}]", topic, faultArray);
        // 故障数组处理
        faultArrayHandle(gatewaySn, deviceSn, commandCode, faultArray);
    }

    /**
     * 故障数组处理
     */
    private void faultArrayHandle(int gatewaySn, int deviceSn, int commandCode, String faultArray) {
        // 协议格式错误或转换失败
        Map.Entry<Integer, List<Integer>> faultListMap = Protocol.faultConvert(commandCode, faultArray);
        if (faultListMap == null) {
            log.warn("协议格式错误或转换失败 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, faultArray);
            return;
        }
        // 网关序号、设备序号、设备类型不匹配
        if (gatewayService.notValid(gatewaySn, deviceSn, faultListMap.getKey())) {
            log.warn("网关序号、设备序号、设备类型不匹配 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, faultArray);
            return;
        }
        ProtocolVo base = new ProtocolVo();
        base.setGatewaySn(gatewaySn);
        base.setDeviceSn(deviceSn);
        base.setCommandCode(commandCode);
        base.setFaultList(faultListMap.getValue());
        base.setFaultDetailList(Protocol.faultDetailConvert(faultListMap.getValue()));
        // 插入
        ProtocolVo result = faultDao.insert(base);
        if (result != null) {
            log.info("故障接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.FAULT_PREFIX + gatewaySn, result.toWebString());
            // 故障详情处理
            faultDetailHandle(result);
        } else {
            log.warn("故障接收失败:[{}]", base);
        }
    }

    /**
     * 故障详情处理
     */
    private void faultDetailHandle(ProtocolVo base) {
        int gatewaySn = base.getGatewaySn();
        int deviceSn = base.getDeviceSn();
        int commandCode = base.getCommandCode();
        Timestamp createTime = base.getCreateTime();
        // 故障详情处理
        ProtocolVo result;
        for (Protocol.FaultDetail faultDetail : base.getFaultDetailList()) {
            ProtocolVo lastOne = findFaultDetailLastOne(gatewaySn, deviceSn, commandCode, faultDetail.getGroupNumber(), faultDetail.getBits(), createTime);
            if (lastOne == null) {
                // 没有历史记录 新增
                ProtocolVo newFaultDetail = new ProtocolVo();
                newFaultDetail.setGatewaySn(gatewaySn);
                newFaultDetail.setDeviceSn(deviceSn);
                newFaultDetail.setCommandCode(commandCode);
                newFaultDetail.setGroupNumber(faultDetail.getGroupNumber());
                newFaultDetail.setBits(faultDetail.getBits());
                result = faultDetailDao.insert(newFaultDetail);
            } else {
                lastOne.setGatewaySn(gatewaySn);
                // 有历史记录 更新
                result = faultDetailDao.save(lastOne);
            }
            if (result != null) {
                log.info("故障详情处理成功:[{}]", result);
            } else {
                log.info("故障详情处理失败:[{}]", base);
            }
        }
    }

    /**
     * 查询1分钟内最后一条故障详情
     */
    private ProtocolVo findFaultDetailLastOne(int gatewaySn, int deviceSn, int commandCode, int groupNumber, int bits, Timestamp createTime) {
        Criteria criteria = new Criteria();
        criteria.and("deviceSn").is(deviceSn);
        criteria.and("commandCode").is(commandCode);
        criteria.and("groupNumber").is(groupNumber);
        criteria.and("bits").is(bits);
        criteria.and("updateTime").gte(new Timestamp(createTime.getTime() - 60 * 1000));
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Query query = Query.query(criteria).with(sort);
        return faultDetailDao.findOne(query, gatewaySn + MongoConstant.FAULT_DETAIL + "_" + DateUtils.getYyyy(createTime.getTime()));
    }

}
