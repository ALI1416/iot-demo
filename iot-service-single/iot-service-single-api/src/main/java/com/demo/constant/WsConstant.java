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
    //   /broadcast/[命令代码] 广播
    //   /communication/[网关序号]/[设备序号]/[命令代码] 交流

    /**
     * 前缀{@value}
     */
    public static final String PREFIX = "/iot/";
    /**
     * 广播模式{@value}
     */
    public static final String BROADCAST_MODE = "/topic" + PREFIX;
    /**
     * 事件(广播模式){@value}
     */
    public static final String EVENT = BROADCAST_MODE + "event/";
    /**
     * 故障(广播模式){@value}
     */
    public static final String FAULT = BROADCAST_MODE + "fault/";
    /**
     * 交互(广播模式){@value}
     */
    public static final String INTERACT = BROADCAST_MODE + "interact/";
    /**
     * 广播(广播模式){@value}
     */
    public static final String BROADCAST = BROADCAST_MODE + "broadcast/";
    /**
     * 交流(广播模式){@value}
     */
    public static final String COMMUNICATION = BROADCAST_MODE + "communication/";

    /**
     * 获取事件主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 事件主题
     */
    public static String getEventTopic(int gatewaySn, int deviceSn, int commandCode) {
        return WsConstant.EVENT + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取故障主题
     *
     * @param gatewaySn 网关序号
     * @return 故障主题
     */
    public static String getFaultTopic(int gatewaySn) {
        return WsConstant.FAULT + gatewaySn;
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
        return WsConstant.INTERACT + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取广播主题
     *
     * @param commandCode 命令代码
     * @return 广播主题
     */
    public static String getBroadcastTopic(int commandCode) {
        return WsConstant.BROADCAST + commandCode;
    }

    /**
     * 获取交流主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 交流主题
     */
    public static String getCommunicationTopic(int gatewaySn, int deviceSn, int commandCode) {
        return WsConstant.COMMUNICATION + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

}
