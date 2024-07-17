package com.demo.service;

import cn.z.clock.Clock;
import cn.z.util.TimestampUtils;
import com.demo.base.ServiceBase;
import com.demo.constant.ReportType;
import com.demo.dao.mongo.EventDao;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.stream.Collectors;

import static java.util.Calendar.*;

/**
 * <h1>事件报表</h1>
 *
 * <p>
 * createDate 2024/04/11 17:42:53
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class EventReportService extends ServiceBase {

    private final EventDao eventDao;

    /**
     * 上一分钟 分钟报表(每分钟的11秒)
     */
    @Async
    @Scheduled(cron = "11 * * * * *")
    public void reportMinute() {
        // 这一分钟的0秒0毫秒
        long timestampEnd = TimestampUtils.getTimestamp(Clock.now(), Calendar.SECOND, true, -1, 0);
        // 上一分钟的0秒0毫秒
        long timestampStart = timestampEnd - TimestampUtils.MILLS_OF_MINUTE;
        Calendar calendar = new Builder().setInstant(timestampStart).build();
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH) + 1;
        Timestamp createTimeStart = new Timestamp(timestampStart);
        Timestamp createTimeEnd = new Timestamp(timestampEnd);
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 查询网关下所有数据
            eventDao.find(gatewaySn, year, month, createTimeStart, createTimeEnd)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list -> {
                                ProtocolVo data = list.get(0);
                                // 设置月、日、小时、分钟
                                Calendar c = new Builder().setInstant(data.getCreateTime()).build();
                                data.setMonth(c.get(Calendar.MONTH) + 1);
                                data.setDay(c.get(Calendar.DAY_OF_MONTH));
                                data.setHour(c.get(Calendar.HOUR_OF_DAY));
                                data.setMinute(c.get(Calendar.MINUTE));
                                // 插入报表
                                eventDao.insertReport(data, gatewaySn, year, ReportType.MINUTE);
                            })
                    );
        }
    }

    /**
     * 上一小时 小时报表(每小时的1分16秒)
     */
    @Async
    // @Scheduled(cron = "16 * * * * *")
    @Scheduled(cron = "16 1 * * * *")
    public void reportHour() {
        // 上一小时
        Calendar calendar = new Builder().setInstant(Clock.now() - TimestampUtils.MILLS_OF_HOUR).build();
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH) + 1;
        int day = calendar.get(DAY_OF_MONTH);
        int hour = calendar.get(HOUR_OF_DAY);
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 查询网关下分钟报表
            eventDao.findReport(gatewaySn, year, month, day, hour, null, ReportType.MINUTE)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list -> {
                                ProtocolVo data = list.get(0);
                                // 清空分钟
                                data.setMinute(null);
                                // 插入报表
                                eventDao.insertReport(data, gatewaySn, year, ReportType.HOUR);
                            })
                    );
        }
    }

    /**
     * 上一天 日报表(每天的0时2分21秒)
     */
    @Async
    // @Scheduled(cron = "21 * * * * *")
    @Scheduled(cron = "21 2 0 * * *")
    public void reportDay() {
        // 上一天
        Calendar calendar = new Builder().setInstant(Clock.now() - TimestampUtils.MILLS_OF_DAY).build();
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH) + 1;
        int day = calendar.get(DAY_OF_MONTH);
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 查询网关下小时报表
            eventDao.findReport(gatewaySn, year, month, day, null, null, ReportType.HOUR)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list -> {
                                ProtocolVo data = list.get(0);
                                // 清空小时
                                data.setHour(null);
                                // 插入报表
                                eventDao.insertReport(data, gatewaySn, year, ReportType.DAY);
                            })
                    );
        }
    }

    /**
     * 上一月 月报表(每月的1日0时3分26秒)
     */
    @Async
    // @Scheduled(cron = "26 * * * * *")
    @Scheduled(cron = "26 3 0 1 * *")
    public void reportMonth() {
        // 上一月
        Calendar calendar = new Builder().setInstant(Clock.now()).build();
        calendar.add(MONTH, -1);
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH) + 1;
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 查询网关下小时报表
            eventDao.findReport(gatewaySn, year, month, null, null, null, ReportType.DAY)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list -> {
                                ProtocolVo data = list.get(0);
                                // 清空日
                                data.setDay(null);
                                // 插入报表
                                eventDao.insertReport(data, gatewaySn, year, ReportType.MONTH);
                            })
                    );
        }
    }

}
