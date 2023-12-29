package com.demo.constant;

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
    //     _[yyyyMM] 全部数据(月) 1月30天24小时60分钟60秒10条=25,920,000条=2600万条
    //     _minute_[yyyy] 分钟报表(年) 1年365天24小时60分钟10条=5,256,000条=526万条
    //     _hour_[yyyy] 小时报表(年) 1年365天24小时10条=87,600条=9万条
    //     _day_[yyyy] 日报表(年) 1年365天10条=3650条
    //     _month_[yyyy] 月报表(年) 1年12月10条=120条
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
     * 分钟报表{@value}
     */
    public static final String MINUTE = "_minute";
    /**
     * 小时报表{@value}
     */
    public static final String HOUR = "_hour";
    /**
     * 日报表{@value}
     */
    public static final String DAY = "_day";
    /**
     * 月报表{@value}
     */
    public static final String MONTH = "_month";

}
