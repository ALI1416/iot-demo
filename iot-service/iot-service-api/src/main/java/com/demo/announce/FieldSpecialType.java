package com.demo.announce;

import lombok.Getter;

/**
 * <h1>字段特殊类型</h1>
 *
 * <p>
 * createDate 2024/01/16 09:26:06
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldSpecialType {

    /**
     * 默认
     */
    DEFAULT(0),
    ;

    /**
     * 字段特殊类型编码
     */
    private final int code;

    FieldSpecialType(int code) {
        this.code = code;
    }

}
