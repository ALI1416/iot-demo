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
    //   /fault/[网关序号] 故障详情
    //   /interact/[网关序号]/[设备序号]/[命令代码] 交互

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
     * 交互前缀(广播模式){@value}
     */
    public static final String INTERACT_PREFIX = BROADCAST + "interact/";

    /**
     * 获取事件主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 事件主题
     */
    public static String getEventTopic(int gatewaySn, int deviceSn, int commandCode) {
        return WsConstant.EVENT_PREFIX + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取故障主题
     *
     * @param gatewaySn 网关序号
     * @return 故障主题
     */
    public static String getFaultTopic(int gatewaySn) {
        return WsConstant.FAULT_PREFIX + gatewaySn;
    }

    /**
     * 获取交互主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 交互主题
     */
    public static String getInteractTopic(int gatewaySn, int deviceSn, int commandCode) {
        return WsConstant.INTERACT_PREFIX + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

}
