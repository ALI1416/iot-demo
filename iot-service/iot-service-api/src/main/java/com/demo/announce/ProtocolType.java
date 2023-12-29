package com.demo.announce;

import lombok.Getter;

/**
 * <h1>协议类型</h1>
 *
 * <p>
 * createDate 2023/12/13 14:45:43
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum ProtocolType {

    /**
     * 事件
     */
    EVENT(0),
    /**
     * 故障
     */
    FAULT(1),
    /**
     * 互动
     */
    INTERACT(2);

    /**
     * 协议类型编码
     */
    private final int code;

    ProtocolType(int code) {
        this.code = code;
    }

}
