package com.demo.service;

import cn.z.clock.Clock;
import cn.z.mongo.entity.Page;
import cn.z.mongo.entity.Pageable;
import cn.z.util.TimestampUtils;
import com.demo.base.ServiceBase;
import com.demo.dao.mongo.EventDao;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    /**
     * 消息 网关序号32位 设备序号8位 命令代码24位 消息
     */
    private static final Map<Long, ProtocolVo> MSG = new HashMap<>();

    static {
        Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "EVENT_MSG");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            // 2分钟消息超时
            long deleteTimestamp = Clock.now() - 2 * TimestampUtils.MILLS_OF_MINUTE;
            MSG.entrySet().removeIf(msg -> msg.getValue().getCreateTime().getTime() < deleteTimestamp);
        }, 2, 2, TimeUnit.MINUTES);
    }

    /**
     * 分页查询报表
     *
     * @param protocol ProtocolVo
     * @return Page ProtocolVo
     */
    public Page<ProtocolVo> findReportPage(ProtocolVo protocol) {
        Query query = Query.query(buildCriteria(protocol));
        Pageable pageable = buildPage(protocol.getPages(), protocol.getRows(), protocol.getOrderBy());
        return eventDao.findReportPage(query, protocol.getGatewaySn(), protocol.getYear(), protocol.getReportType(), pageable);
    }

    /**
     * 查找报表最后一个
     *
     * @param protocol ProtocolVo
     * @return ProtocolVo(没有返回null)
     */
    public ProtocolVo findReportLastOne(ProtocolVo protocol) {
        protocol.setPages(1);
        protocol.setRows(1);
        protocol.setOrderBy("id desc");
        List<ProtocolVo> list = findReportPage(protocol).getList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    private static Criteria buildCriteria(ProtocolVo protocol) {
        Criteria criteria = new Criteria();
        if (protocol.getDeviceSn() != null) {
            criteria.and("deviceSn").is(protocol.getDeviceSn());
        }
        if (protocol.getCommandCode() != null) {
            criteria.and("commandCode").is(protocol.getCommandCode());
        }
        if (protocol.getReportType() != null) {
            // 无break
            switch (protocol.getReportType()) {
                case MINUTE: {
                    if (protocol.getMinute() != null) {
                        criteria.and("minute").is(protocol.getMinute());
                    }
                }
                case HOUR: {
                    if (protocol.getHour() != null) {
                        criteria.and("hour").is(protocol.getHour());
                    }
                }
                case DAY: {
                    if (protocol.getDay() != null) {
                        criteria.and("day").is(protocol.getDay());
                    }
                }
                case MONTH: {
                    if (protocol.getMonth() != null) {
                        criteria.and("month").is(protocol.getMonth());
                    }
                }
            }
        }
        buildRange(criteria, "createTime", protocol.getCreateTime(), protocol.getCreateTimeEnd(), protocol.getCreateTimeNot());
        return criteria;
    }

    /**
     * 插入消息
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @param msg         ProtocolVo
     */
    public static void addMsg(int gatewaySn, int deviceSn, int commandCode, ProtocolVo msg) {
        MSG.put(((long) gatewaySn) << 32 | deviceSn << 24 | commandCode, msg);
    }

    /**
     * 获取消息
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return ProtocolVo
     */
    public static ProtocolVo getMsg(int gatewaySn, int deviceSn, int commandCode) {
        return MSG.get(((long) gatewaySn) << 32 | deviceSn << 24 | commandCode);
    }

}
