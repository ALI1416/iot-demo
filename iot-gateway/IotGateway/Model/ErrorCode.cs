namespace IotGateway.Model
{

    /// <summary>
    /// 错误代码
    /// </summary>
    public enum ErrorCode
    {
        /// <summary>
        /// 没有错误
        /// </summary>
        NoError = 0,
        /// <summary>
        /// 网关不存在
        /// </summary>
        GatewayNotFound = 1,
        /// <summary>
        /// 网关离线
        /// </summary>
        GatewayOffline = 2,
        /// <summary>
        /// 设备不存在
        /// </summary>
        DeviceNotFound = 3,
        /// <summary>
        /// 设备离线
        /// </summary>
        DeviceOffline = 4,
        /// <summary>
        /// MQTT主题错误
        /// </summary>
        MqttTopicError = 5,
        /// <summary>
        /// MQTT消息错误
        /// </summary>
        MqttMsgError = 6,
        /// <summary>
        /// 命令代码错误
        /// </summary>
        CommandCodeError = 7,
        /// <summary>
        /// 请求序号错误
        /// </summary>
        RequestSnError = 8,
        /// <summary>
        /// 网关执行失败
        /// </summary>
        GatewayReturnError = 9,
        /// <summary>
        /// 设备执行失败
        /// </summary>
        DeviceReturnError = 10,

    }
}
