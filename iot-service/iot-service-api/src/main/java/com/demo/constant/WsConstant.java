package com.demo.constant;

/**
 * <h1>WebSocket常量</h1>
 *
 * <p>
 * createDate 2023/11/14 17:36:16
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class WsConstant {

    private WsConstant() {
    }

    /* 广播模式 */
    // /topic/iot
    //   /event/[网关序号]/[设备序号]/[命令代码] 事件
    //   /faultDetail/[网关序号] 故障详情
    //   /interact/[网关序号]/[设备序号]/[命令代码] 互动

    /**
     * 前缀{@value}
     */
    public static final String PREFIX = "/iot/";
    /**
     * 广播模式{@value}
     */
    public static final String BROADCAST = "/topic" + PREFIX;
    /**
     * 事件前缀(广播模式){@value}
     */
    public static final String EVENT_PREFIX = BROADCAST + "event/";
    /**
     * 故障前缀(广播模式){@value}
     */
    public static final String FAULT_PREFIX = BROADCAST + "fault/";
    /**
     * 互动前缀(用户模式){@value}
     */
    public static final String INTERACT_PREFIX = BROADCAST + "interact/";

}
