package com.demo.service;

import cn.z.clock.Clock;
import cn.z.util.TimestampUtils;
import com.demo.base.ServiceBase;
import com.demo.constant.ReportType;
import com.demo.dao.mongo.EventDao;
import com.demo.entity.protocol.Protocol;
import com.demo.util.DateUtils;
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
        // 上一分钟
        Calendar calendar = new Builder().setInstant(Clock.now() - TimestampUtils.MILLS_OF_MINUTE).build();
        int year = calendar.get(YEAR);
        int month = calendar.get(MONTH) + 1;
        int day = calendar.get(DAY_OF_MONTH);
        int hour = calendar.get(HOUR_OF_DAY);
        int minute = calendar.get(MINUTE);
        reportMinute(year, month, day, hour, minute);
    }

    /**
     * 分钟报表
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   小时
     * @param minute 分钟
     */
    public void reportMinute(int year, int month, int day, int hour, int minute) {
        // 这一分钟的0秒0毫秒
        long timestampStart = DateUtils.getTimestamp(year, month, day, hour, minute, 0, 0);
        Timestamp createTimeStart = new Timestamp(timestampStart);
        // 下一分钟的0秒0毫秒
        Timestamp createTimeEnd = new Timestamp(timestampStart + TimestampUtils.MILLS_OF_MINUTE);
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 全部数据
            eventDao.find(gatewaySn, year, month, createTimeStart, createTimeEnd)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list ->
                                    // 插入报表
                                    eventDao.insertReport(Protocol.getEventReportMinute(list), gatewaySn, year, ReportType.MINUTE)
                            )
                    );
        }
    }

    /**
     * 分钟报表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param hour  小时
     */
    public void reportMinute(int year, int month, int day, int hour) {
        for (int i = 0; i < 60; i++) {
            reportMinute(year, month, day, hour, i);
        }
    }

    /**
     * 分钟报表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    public void reportMinute(int year, int month, int day) {
        for (int i = 0; i < 24; i++) {
            reportMinute(year, month, day, i);
        }
    }

    /**
     * 分钟报表
     *
     * @param year  年
     * @param month 月
     */
    public void reportMinute(int year, int month) {
        for (int i = 1; i < 32; i++) {
            reportMinute(year, month, i);
        }
    }

    /**
     * 分钟报表
     *
     * @param year 年
     */
    public void reportMinute(int year) {
        for (int i = 1; i < 13; i++) {
            reportMinute(year, i);
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
        reportHour(year, month, day, hour);
    }

    /**
     * 小时报表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @param hour  小时
     */
    public void reportHour(int year, int month, int day, int hour) {
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 分钟报表
            eventDao.findReport(gatewaySn, year, month, day, hour, null, ReportType.MINUTE)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list ->
                                    // 插入报表
                                    eventDao.insertReport(Protocol.getEventReportHour(list), gatewaySn, year, ReportType.HOUR)
                            )
                    );
        }
    }

    /**
     * 小时报表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    public void reportHour(int year, int month, int day) {
        for (int i = 0; i < 24; i++) {
            reportHour(year, month, day, i);
        }
    }

    /**
     * 小时报表
     *
     * @param year  年
     * @param month 月
     */
    public void reportHour(int year, int month) {
        for (int i = 1; i < 32; i++) {
            reportHour(year, month, i);
        }
    }

    /**
     * 小时报表
     *
     * @param year 年
     */
    public void reportHour(int year) {
        for (int i = 1; i < 13; i++) {
            reportHour(year, i);
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
        reportDay(year, month, day);
    }

    /**
     * 日报表
     *
     * @param year  年
     * @param month 月
     * @param day   日
     */
    public void reportDay(int year, int month, int day) {
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 小时报表
            eventDao.findReport(gatewaySn, year, month, day, null, null, ReportType.HOUR)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list ->
                                    // 插入报表
                                    eventDao.insertReport(Protocol.getEventReportDay(list), gatewaySn, year, ReportType.DAY)
                            )
                    );
        }
    }

    /**
     * 日报表
     *
     * @param year  年
     * @param month 月
     */
    public void reportDay(int year, int month) {
        for (int i = 1; i < 32; i++) {
            reportDay(year, month, i);
        }
    }

    /**
     * 日报表
     *
     * @param year 年
     */
    public void reportDay(int year) {
        for (int i = 1; i < 13; i++) {
            reportDay(year, i);
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
        reportMonth(year, month);
    }

    /**
     * 月报表
     *
     * @param year  年
     * @param month 月
     */
    public void reportMonth(int year, int month) {
        for (int gatewaySn : GatewayService.getGatewaySnList()) {
            // 日报表
            eventDao.findReport(gatewaySn, year, month, null, null, null, ReportType.DAY)
                    // 命令代码
                    .stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(v ->
                            // 设备代码
                            v.stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(list ->
                                    // 插入报表
                                    eventDao.insertReport(Protocol.getEventReportMonth(list), gatewaySn, year, ReportType.MONTH)
                            )
                    );
        }
    }

    /**
     * 月报表
     *
     * @param year 年
     */
    public void reportMonth(int year) {
        for (int i = 1; i < 13; i++) {
            reportMonth(year, i);
        }
    }

}
