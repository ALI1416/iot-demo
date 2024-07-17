package com.demo.constant;

import com.demo.entity.pojo.GlobalException;
import com.demo.util.DateUtils;

import java.sql.Timestamp;

/**
 * <h1>MongoDB常量</h1>
 *
 * <p>
 * createDate 2023/11/13 17:27:10
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class MongoConstant {

    private MongoConstant() {
    }

    // [网关序号]
    //   _event 事件
    //     _[yyyyMM] 全部数据(月) 1月30天24小时60分钟60秒*每秒10条=25,920,000条=2600万条
    //     _minute_[yyyy] 分钟报表(年) 1年365天24小时60分钟*每分钟10条=5,256,000条=526万条
    //     _hour_[yyyy] 小时报表(年) 1年365天24小时*每小时10条=87,600条=9万条
    //     _day_[yyyy] 日报表(年) 1年365天*每天10条=3650条
    //     _month_[yyyy] 月报表(年) 1年12月*每月10条=120条
    //   _fault_[yyyy] 故障(年)
    //   _fault_detail_[yyyy] 故障详情(年)
    //   _interact_[yyyy] 互动(年)

    /**
     * 事件{@value}
     */
    public static final String EVENT = "_event";
    /**
     * 故障{@value}
     */
    public static final String FAULT = "_fault";
    /**
     * 故障详情{@value}
     */
    public static final String FAULT_DETAIL = "_fault_detail";
    /**
     * 互动{@value}
     */
    public static final String INTERACT = "_interact";

    /**
     * 报表-分钟{@value}
     */
    public static final String MINUTE = "_minute";
    /**
     * 报表-小时{@value}
     */
    public static final String HOUR = "_hour";
    /**
     * 报表-日{@value}
     */
    public static final String DAY = "_day";
    /**
     * 报表-月{@value}
     */
    public static final String MONTH = "_month";

    /**
     * 获取事件集合名
     *
     * @param gatewaySn 网关序号
     * @param timestamp 时间戳
     * @return 集合名
     */
    public static String getEventCollectionName(int gatewaySn, Timestamp timestamp) {
        return gatewaySn + MongoConstant.EVENT + "_" + DateUtils.getYearMonth(timestamp);
    }

    /**
     * 获取事件集合名
     *
     * @param gatewaySn 网关序号
     * @param year      年
     * @param month     月
     * @return 集合名
     */
    public static String getEventCollectionName(int gatewaySn, int year, int month) {
        return gatewaySn + MongoConstant.EVENT + "_" + DateUtils.getYearMonth(year, month);
    }

    /**
     * 获取事件报表集合名
     *
     * @param gatewaySn  网关序号
     * @param year       年
     * @param reportType 报表类型
     * @return 集合名
     */
    public static String getEventReportCollectionName(int gatewaySn, int year, ReportType reportType) {
        switch (reportType) {
            case MINUTE -> {
                return gatewaySn + MongoConstant.EVENT + MongoConstant.MINUTE + "_" + year;
            }
            case HOUR -> {
                return gatewaySn + MongoConstant.EVENT + MongoConstant.HOUR + "_" + year;
            }
            case DAY -> {
                return gatewaySn + MongoConstant.EVENT + MongoConstant.DAY + "_" + year;
            }
            case MONTH -> {
                return gatewaySn + MongoConstant.EVENT + MongoConstant.MONTH + "_" + year;
            }
            default -> throw new GlobalException("报表类型错误！");
        }
    }

    /**
     * 获取故障集合名
     *
     * @param gatewaySn 网关序号
     * @param timestamp 时间戳
     * @return 集合名
     */
    public static String getFaultCollectionName(int gatewaySn, Timestamp timestamp) {
        return gatewaySn + MongoConstant.FAULT + "_" + DateUtils.getYear(timestamp);
    }

    /**
     * 获取故障详情集合名
     *
     * @param gatewaySn 网关序号
     * @param timestamp 时间戳
     * @return 集合名
     */
    public static String getFaultDetailCollectionName(int gatewaySn, Timestamp timestamp) {
        return gatewaySn + MongoConstant.FAULT_DETAIL + "_" + DateUtils.getYear(timestamp);
    }

    /**
     * 获取故障详情集合名
     *
     * @param gatewaySn 网关序号
     * @param year      年
     * @return 集合名
     */
    public static String getFaultDetailCollectionName(int gatewaySn, int year) {
        return gatewaySn + MongoConstant.FAULT_DETAIL + "_" + year;
    }

    /**
     * 获取交互集合名
     *
     * @param gatewaySn 网关序号
     * @param timestamp 时间戳
     * @return 集合名
     */
    public static String getInteractCollectionName(int gatewaySn, Timestamp timestamp) {
        return gatewaySn + MongoConstant.INTERACT + "_" + DateUtils.getYear(timestamp);
    }

    /**
     * 获取交互集合名
     *
     * @param gatewaySn 网关序号
     * @param timestamp 时间戳
     * @return 集合名
     */
    public static String getInteractCollectionName(int gatewaySn, long timestamp) {
        return gatewaySn + MongoConstant.INTERACT + "_" + DateUtils.getYear(timestamp);
    }

}
