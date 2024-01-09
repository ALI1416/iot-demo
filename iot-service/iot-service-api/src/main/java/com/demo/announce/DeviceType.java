package com.demo.announce;

import lombok.Getter;

/**
 * <h1>设备类型</h1>
 *
 * <p>
 * createDate 2023/12/22 15:03:08
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum DeviceType {

    /**
     * 网关
     */
    GATEWAY(0),
    /**
     * 温度计
     */
    THERMOMETER(1);

    /**
     * 设备类型编码
     */
    private final int code;

    DeviceType(int code) {
        this.code = code;
    }

}
