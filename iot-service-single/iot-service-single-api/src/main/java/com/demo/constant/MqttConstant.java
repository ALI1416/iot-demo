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
    //   /request/[网关序号]/[设备序号]/[命令代码] 请求
    //   /response/[网关序号]/[设备序号]/[命令代码] 响应
    //   /broadcast/[命令代码] 广播
    //   /read/[网关序号]/[设备序号]/[命令代码] 读取
    //   /write/[网关序号]/[设备序号]/[命令代码] 写入

    /**
     * 前缀{@value}
     */
    public static final String PREFIX = "$iot/";
    /**
     * 事件{@value}
     */
    public static final String EVENT = PREFIX + "event/";
    /**
     * 故障{@value}
     */
    public static final String FAULT = PREFIX + "fault/";
    /**
     * 请求{@value}
     */
    public static final String REQUEST = PREFIX + "request/";
    /**
     * 响应{@value}
     */
    public static final String RESPONSE = PREFIX + "response/";
    /**
     * 广播{@value}
     */
    public static final String BROADCAST = PREFIX + "broadcast/";
    /**
     * 读取{@value}
     */
    public static final String READ = PREFIX + "read/";
    /**
     * 写入{@value}
     */
    public static final String WRITE = PREFIX + "write/";

    /**
     * 获取事件主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 事件主题
     */
    public static String getEventTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.EVENT + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取故障主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 故障主题
     */
    public static String getFaultTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.FAULT + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取请求主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 请求主题
     */
    public static String getRequestTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.REQUEST + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取响应主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 响应主题
     */
    public static String getResponseTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.RESPONSE + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取广播主题
     *
     * @param commandCode 命令代码
     * @return 广播主题
     */
    public static String getBroadcastTopic(int commandCode) {
        return MqttConstant.BROADCAST + commandCode;
    }

    /**
     * 获取读取主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 读取主题
     */
    public static String getReadTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.READ + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

    /**
     * 获取写入主题
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return 写入主题
     */
    public static String getWriteTopic(int gatewaySn, int deviceSn, int commandCode) {
        return MqttConstant.WRITE + gatewaySn + "/" + deviceSn + "/" + commandCode;
    }

}
