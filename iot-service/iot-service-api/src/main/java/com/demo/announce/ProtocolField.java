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
     * 字段单位
     */
    FieldUnit unit() default FieldUnit.NONE;

}
