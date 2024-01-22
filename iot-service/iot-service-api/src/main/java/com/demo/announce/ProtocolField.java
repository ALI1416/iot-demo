package com.demo.announce;

import java.lang.annotation.*;

/**
 * <h1>协议字段</h1>
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
public @interface ProtocolField {

    /**
     * 字段名
     */
    String name();

    /**
     * 字段特殊类型
     */
    FieldSpecialType specialType() default FieldSpecialType.DEFAULT;

    /**
     * 字段单位
     */
    FieldUnit unit() default FieldUnit.NONE;

    /**
     * 字段Map
     */
    FieldMap map() default FieldMap.NULL;

    /**
     * 最大值
     */
    String max() default "";

    /**
     * 最小值
     */
    String min() default "";

    /**
     * 最大数组长度
     */
    int[] maxArrayLength() default {};

    /**
     * 最小数组长度
     */
    int[] minArrayLength() default {};

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

}
