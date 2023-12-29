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
     * 温度(m℃ 毫摄氏度)
     */
    CENTIGRADE_N3("m℃", "毫摄氏度"),
    /**
     * 温度(℃ 摄氏度)
     */
    CENTIGRADE("℃", "摄氏度"),
    /**
     * 万分之(%% 万分之)
     */
    PERCENTAGE_N2("%%", "万分之"),
    /**
     * 千分之(‰ 百分之)
     */
    PERCENTAGE_N1("‰", "千分之"),
    /**
     * 百分之(% 百分之)
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
