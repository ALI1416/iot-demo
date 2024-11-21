package com.demo.constant;

import lombok.Getter;

/**
 * <h1>错误代码</h1>
 *
 * <p>
 * createDate 2024/08/30 10:43:13
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum ErrorCode {

    /**
     * 没有错误
     */
    NO_ERROR(0),
    /**
     * 网关不存在
     */
    GATEWAY_NOT_FOUND(1),
    /**
     * 设备不存在
     */
    DEVICE_NOT_FOUND(2),
    /**
     * 网关离线
     */
    GATEWAY_OFFLINE(3),
    /**
     * 设备离线
     */
    DEVICE_OFFLINE(4),
    /**
     * MQTT主题错误
     */
    MQTT_TOPIC_ERROR(5),
    /**
     * MQTT消息错误
     */
    MQTT_MESSAGE_ERROR(6),
    /**
     * 命令代码错误
     */
    COMMAND_CODE_ERROR(7),
    /**
     * 请求序号错误
     */
    REQUEST_SN_ERROR(8),
    /**
     * 网关执行失败
     */
    GATEWAY_RESPONSE_ERROR(9),
    /**
     * 设备执行失败
     */
    DEVICE_RESPONSE_ERROR(10),
    /**
     * 网关执行超时
     */
    GATEWAY_REQUEST_TIMEOUT(11),
    /**
     * 设备执行超时
     */
    DEVICE_REQUEST_TIMEOUT(12),
    ;

    /**
     * 编码
     */
    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

}
