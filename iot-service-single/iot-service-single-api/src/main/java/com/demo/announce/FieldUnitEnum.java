package com.demo.announce;

import com.demo.entity.protocol.ProtocolInfo;
import lombok.Getter;

/**
 * <h1>字段单位枚举</h1>
 *
 * <p>
 * createDate 2023/12/13 11:46:56
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldUnitEnum {

    /**
     * 无
     */
    NONE("", ""),

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
     * 温度(0.0001℃ 0.0001摄氏度)
     */
    TEMPERATURE_N4("0.0001℃", "0.0001摄氏度"),
    /**
     * 温度(m℃ 毫摄氏度)
     */
    TEMPERATURE_N3("m℃", "毫摄氏度"),
    /**
     * 温度(0.01℃ 0.01摄氏度)
     */
    TEMPERATURE_N2("0.01℃", "0.01摄氏度"),
    /**
     * 温度(0.1℃ 0.1摄氏度)
     */
    TEMPERATURE_N1("0.1℃", "0.1摄氏度"),
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
    ;

    /**
     * 单位
     */
    private final ProtocolInfo.FieldUnit fieldUnit;

    FieldUnitEnum(String symbol, String name) {
        this.fieldUnit = new ProtocolInfo.FieldUnit(symbol, name);
    }

}
