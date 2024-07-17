package com.demo.constant;

import lombok.Getter;

/**
 * <h1>交互类型</h1>
 *
 * <p>
 * createDate 2024/04/17 11:11:25
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum InteractType {

    /**
     * 默认(双向互动)
     */
    DEFAULT(0),
    /**
     * 单向互动
     */
    SINGLE(1),
    ;

    /**
     * 编码
     */
    private final int code;

    InteractType(int code) {
        this.code = code;
    }

}
