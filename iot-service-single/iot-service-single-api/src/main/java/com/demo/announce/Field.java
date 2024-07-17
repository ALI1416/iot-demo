package com.demo.announce;

import java.lang.annotation.*;

/**
 * <h1>字段</h1>
 *
 * <p>
 * createDate 2023/12/13 11:02:45
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {

    /**
     * 字段名
     */
    String name();

    /**
     * 字段类型
     */
    FieldType type() default FieldType.NULL;

    /**
     * 字段特殊数据类型
     */
    FieldSpecialDataType specialDataType() default FieldSpecialDataType.DEFAULT;

    /**
     * 字段单位
     */
    FieldUnitEnum unit() default FieldUnitEnum.NONE;

    /**
     * 推荐字段单位
     */
    FieldUnitEnum recommend() default FieldUnitEnum.NONE;

    /**
     * 推荐字段单位转换-乘以
     */
    int multiply() default 1;

    /**
     * 推荐字段单位转换-除以
     */
    int divide() default 1;

    /**
     * 字段状态
     */
    FieldStatusEnum status() default FieldStatusEnum.NULL;

    /**
     * 最大值
     */
    String max() default "";

    /**
     * 最小值
     */
    String min() default "";

    /**
     * 最大字符长度
     */
    int maxLength() default Integer.MAX_VALUE;

    /**
     * 最小字符长度
     */
    int minLength() default 0;

    /**
     * 特殊处理
     */
    boolean special() default false;

    /**
     * 备注
     */
    String comment() default "";

}
