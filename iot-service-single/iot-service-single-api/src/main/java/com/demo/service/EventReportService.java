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
        reportMinute(null, null, null, year, month, day, hour, minute);
    }

    /**
     * 分钟报表
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     */
    private void reportMinuteInner(int gatewaySn, Integer deviceSn, Integer commandCode, int year, int month, int day, int hour, int minute) {
        // 这一分钟的0秒0毫秒
        long timestampStart = DateUtils.getTimestamp(year, month, day, hour, minute, 0, 0);
        Timestamp createTimeStart = new Timestamp(timestampStart);
        // 下一分钟的0秒0毫秒
        Timestamp createTimeEnd = new Timestamp(timestampStart + TimestampUtils.MILLS_OF_MINUTE);
        // 全部数据
        eventDao.find(gatewaySn, deviceSn, commandCode, year, month, createTimeStart, createTimeEnd)
                // 设备序号
                .stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(v ->
                        // 命令代码
                        v.stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(list ->
                                // 插入报表
                                eventDao.insertReport(Protocol.getEventReportMinute(list), gatewaySn, year, ReportType.MINUTE)
                        )
                );
    }

    /**
     * 分钟报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     * @param hour        小时<br>
     *                    null 0-23小时
     * @param minute      分钟<br>
     *                    null 0-59分钟
     */
    public void reportMinute(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour, Integer minute) {
        if (gatewaySn == null) {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                for (int g : GatewayService.getGatewaySnList()) {
                                    reportMinuteInner(g, deviceSn, commandCode, year, i, j, k, m);
                                }
                            }
                        }
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                for (int g : GatewayService.getGatewaySnList()) {
                                    reportMinuteInner(g, deviceSn, commandCode, year, month, j, k, m);
                                }
                            }
                        }
                    }
                } else {
                    if (hour == null) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                for (int g : GatewayService.getGatewaySnList()) {
                                    reportMinuteInner(g, deviceSn, commandCode, year, month, day, k, m);
                                }
                            }
                        }
                    } else {
                        if (minute == null) {
                            for (int m = 0; m < 60; m++) {
                                for (int g : GatewayService.getGatewaySnList()) {
                                    reportMinuteInner(g, deviceSn, commandCode, year, month, day, hour, m);
                                }
                            }
                        } else {
                            for (int g : GatewayService.getGatewaySnList()) {
                                reportMinuteInner(g, deviceSn, commandCode, year, month, day, hour, minute);
                            }
                        }
                    }
                }
            }
        } else {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                reportMinuteInner(gatewaySn, deviceSn, commandCode, year, i, j, k, m);
                            }
                        }
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                reportMinuteInner(gatewaySn, deviceSn, commandCode, year, month, j, k, m);
                            }
                        }
                    }
                } else {
                    if (hour == null) {
                        for (int k = 0; k < 24; k++) {
                            for (int m = 0; m < 60; m++) {
                                reportMinuteInner(gatewaySn, deviceSn, commandCode, year, month, day, k, m);
                            }
                        }
                    } else {
                        if (minute == null) {
                            for (int m = 0; m < 60; m++) {
                                reportMinuteInner(gatewaySn, deviceSn, commandCode, year, month, day, hour, m);
                            }
                        } else {
                            reportMinuteInner(gatewaySn, deviceSn, commandCode, year, month, day, hour, minute);
                        }
                    }
                }
            }
        }
    }

    /**
     * 删除分钟报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     * @param hour        小时<br>
     *                    null 0-23小时
     * @param minute      分钟<br>
     *                    null 0-59分钟
     * @return 删除条数
     */
    public long reportMinuteDelete(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour, Integer minute) {
        if (gatewaySn == null) {
            long count = 0;
            for (int g : GatewayService.getGatewaySnList()) {
                count += eventDao.deleteReport(g, deviceSn, commandCode, year, month, day, hour, minute, ReportType.MINUTE);
            }
            return count;
        } else {
            return eventDao.deleteReport(gatewaySn, deviceSn, commandCode, year, month, day, hour, minute, ReportType.MINUTE);
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
        reportHour(null, null, null, year, month, day, hour);
    }

    /**
     * 小时报表
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     */
    private void reportHourInner(int gatewaySn, Integer deviceSn, Integer commandCode, int year, int month, int day, int hour) {
        // 分钟报表
        eventDao.findReport(gatewaySn, deviceSn, commandCode, year, month, day, hour, null, ReportType.MINUTE)
                // 设备序号
                .stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(v ->
                        // 命令代码
                        v.stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(list ->
                                // 插入报表
                                eventDao.insertReport(Protocol.getEventReportHour(list), gatewaySn, year, ReportType.HOUR)
                        )
                );
    }

    /**
     * 小时报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     * @param hour        小时<br>
     *                    null 0-23小时
     */
    public void reportHour(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour) {
        if (gatewaySn == null) {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int g : GatewayService.getGatewaySnList()) {
                                reportHourInner(g, deviceSn, commandCode, year, i, j, k);
                            }
                        }
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            for (int g : GatewayService.getGatewaySnList()) {
                                reportHourInner(g, deviceSn, commandCode, year, month, j, k);
                            }
                        }
                    }
                } else {
                    if (hour == null) {
                        for (int k = 0; k < 24; k++) {
                            for (int g : GatewayService.getGatewaySnList()) {
                                reportHourInner(g, deviceSn, commandCode, year, month, day, k);
                            }
                        }
                    } else {
                        for (int g : GatewayService.getGatewaySnList()) {
                            reportHourInner(g, deviceSn, commandCode, year, month, day, hour);
                        }
                    }
                }
            }
        } else {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            reportHourInner(gatewaySn, deviceSn, commandCode, year, i, j, k);
                        }
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        for (int k = 0; k < 24; k++) {
                            reportHourInner(gatewaySn, deviceSn, commandCode, year, month, j, k);
                        }
                    }
                } else {
                    if (hour == null) {
                        for (int k = 0; k < 24; k++) {
                            reportHourInner(gatewaySn, deviceSn, commandCode, year, month, day, k);
                        }
                    } else {
                        reportHourInner(gatewaySn, deviceSn, commandCode, year, month, day, hour);
                    }
                }
            }
        }
    }

    /**
     * 删除小时报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     * @param hour        小时<br>
     *                    null 0-23小时
     * @return 删除条数
     */
    public long reportHourDelete(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour) {
        if (gatewaySn == null) {
            long count = 0;
            for (int g : GatewayService.getGatewaySnList()) {
                count += eventDao.deleteReport(g, deviceSn, commandCode, year, month, day, hour, null, ReportType.HOUR);
            }
            return count;
        } else {
            return eventDao.deleteReport(gatewaySn, deviceSn, commandCode, year, month, day, hour, null, ReportType.HOUR);
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
        reportDay(null, null, null, year, month, day);
    }

    /**
     * 日报表
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月
     * @param day         日
     */
    private void reportDayInner(int gatewaySn, Integer deviceSn, Integer commandCode, int year, int month, int day) {
        // 小时报表
        eventDao.findReport(gatewaySn, deviceSn, commandCode, year, month, day, null, null, ReportType.HOUR)
                // 设备序号
                .stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(v ->
                        // 命令代码
                        v.stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(list ->
                                // 插入报表
                                eventDao.insertReport(Protocol.getEventReportDay(list), gatewaySn, year, ReportType.DAY)
                        )
                );
    }

    /**
     * 日报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     */
    public void reportDay(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day) {
        if (gatewaySn == null) {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        for (int g : GatewayService.getGatewaySnList()) {
                            reportDayInner(g, deviceSn, commandCode, year, i, j);
                        }
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        for (int g : GatewayService.getGatewaySnList()) {
                            reportDayInner(g, deviceSn, commandCode, year, month, j);
                        }
                    }
                } else {
                    for (int g : GatewayService.getGatewaySnList()) {
                        reportDayInner(g, deviceSn, commandCode, year, month, day);
                    }
                }
            }
        } else {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int j = 1; j < 31; j++) {
                        reportDayInner(gatewaySn, deviceSn, commandCode, year, i, j);
                    }
                }
            } else {
                if (day == null) {
                    for (int j = 1; j < 31; j++) {
                        reportDayInner(gatewaySn, deviceSn, commandCode, year, month, j);
                    }
                } else {
                    reportDayInner(gatewaySn, deviceSn, commandCode, year, month, day);
                }
            }
        }
    }

    /**
     * 删除日报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @param day         日<br>
     *                    null 1-31日
     * @return 删除条数
     */
    public long reportDayDelete(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day) {
        if (gatewaySn == null) {
            long count = 0;
            for (int g : GatewayService.getGatewaySnList()) {
                count += eventDao.deleteReport(g, deviceSn, commandCode, year, month, day, null, null, ReportType.DAY);
            }
            return count;
        } else {
            return eventDao.deleteReport(gatewaySn, deviceSn, commandCode, year, month, day, null, null, ReportType.DAY);
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
        reportMonth(null, null, null, year, month);
    }

    /**
     * 月报表
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月
     */
    private void reportMonthInner(int gatewaySn, Integer deviceSn, Integer commandCode, int year, int month) {
        // 日报表
        eventDao.findReport(gatewaySn, deviceSn, commandCode, year, month, null, null, null, ReportType.DAY)
                // 设备序号
                .stream().collect(Collectors.groupingBy(Protocol::getDeviceSn)).values().forEach(v ->
                        // 命令代码
                        v.stream().collect(Collectors.groupingBy(Protocol::getCommandCode)).values().forEach(list ->
                                // 插入报表
                                eventDao.insertReport(Protocol.getEventReportMonth(list), gatewaySn, year, ReportType.MONTH)
                        )
                );
    }

    /**
     * 月报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     */
    public void reportMonth(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month) {
        if (gatewaySn == null) {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    for (int g : GatewayService.getGatewaySnList()) {
                        reportMonthInner(g, deviceSn, commandCode, year, i);
                    }
                }
            } else {
                for (int g : GatewayService.getGatewaySnList()) {
                    reportMonthInner(g, deviceSn, commandCode, year, month);
                }
            }
        } else {
            if (month == null) {
                for (int i = 1; i < 13; i++) {
                    reportMonthInner(gatewaySn, deviceSn, commandCode, year, i);
                }
            } else {
                reportMonthInner(gatewaySn, deviceSn, commandCode, year, month);
            }
        }
    }

    /**
     * 删除月报表
     *
     * @param gatewaySn   网关序号<br>
     *                    null 所有网关
     * @param deviceSn    设备序号<br>
     *                    null 所有设备
     * @param commandCode 命令代码<br>
     *                    null 所有命令
     * @param year        年
     * @param month       月<br>
     *                    null 1-12月
     * @return 删除条数
     */
    public long reportMonthDelete(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month) {
        if (gatewaySn == null) {
            long count = 0;
            for (int g : GatewayService.getGatewaySnList()) {
                count += eventDao.deleteReport(g, deviceSn, commandCode, year, month, null, null, null, ReportType.MONTH);
            }
            return count;
        } else {
            return eventDao.deleteReport(gatewaySn, deviceSn, commandCode, year, month, null, null, null, ReportType.MONTH);
        }
    }

}
