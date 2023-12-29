package com.demo.constant;

/**
 * <h1>MQTT常量</h1>
 *
 * <p>
 * createDate 2023/11/10 16:49:46
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class MqttConstant {

    private MqttConstant() {
    }

    // $iot
    //   /event/[网关序号]/[设备序号]/[命令代码] 事件
    //   /fault/[网关序号]/[设备序号]/[命令代码] 故障
    //   /request/[网关序号]/[设备序号]/[命令代码]/[请求序号] 请求
    //   /response/[网关序号]/[设备序号]/[命令代码]/[请求序号]/[错误代码] 响应

    /**
     * 前缀{@value}
     */
    public static final String PREFIX = "$iot/";
    /**
     * 事件前缀{@value}
     */
    public static final String EVENT_PREFIX = PREFIX + "event/";
    /**
     * 故障前缀{@value}
     */
    public static final String FAULT_PREFIX = PREFIX + "fault/";
    /**
     * 请求前缀{@value}
     */
    public static final String REQUEST_PREFIX = PREFIX + "request/";
    /**
     * 响应前缀{@value}
     */
    public static final String RESPONSE_PREFIX = PREFIX + "response/";

}
