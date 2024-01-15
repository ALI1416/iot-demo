package com.demo.announce;

import lombok.Getter;

/**
 * <h1>字段单位</h1>
 *
 * <p>
 * createDate 2023/12/13 11:46:56
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldUnit {

    /**
     * 时间(year 年)
     */
    YEAR("year", "年"),
    /**
     * 时间(month 月)
     */
    MONTH("month", "月"),
    /**
     * 时间(day 日)
     */
    DAY("day", "日"),
    /**
     * 时间(h 小时)
     */
    HOUR("h", "小时"),
    /**
     * 时间(min 分钟)
     */
    MINUTE("min", "分钟"),
    /**
     * 时间(s 秒)
     */
    SECOND("s", "秒"),
    /**
     * 时间(ms 毫秒)
     */
    MILLISECOND("ms", "毫秒"),

    /**
     * 温度(m℃ 毫摄氏度)
     */
    TEMPERATURE_N3("m℃", "毫摄氏度"),
    /**
     * 温度(℃ 摄氏度)
     */
    TEMPERATURE("℃", "摄氏度"),

    /**
     * 万分比(%% 万分之)
     */
    PERCENTAGE_N2("%%", "万分之"),
    /**
     * 千分比(‰ 千分之)
     */
    PERCENTAGE_N1("‰", "千分之"),
    /**
     * 百分比(% 百分之)
     */
    PERCENTAGE("%", "百分之"),

    /**
     * 无量纲
     */
    NONE("", "");

    /**
     * 符号
     */
    private final String symbol;
    /**
     * 名称
     */
    private final String name;

    FieldUnit(String symbol, String name) {
        this.symbol = symbol;
        this.name = name;
    }

}
