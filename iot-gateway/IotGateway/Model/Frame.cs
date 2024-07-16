using System;

namespace IotGateway.Model
{
    /// <summary>
    /// 协议帧
    /// </summary>
    public class Frame
    {

        /// <summary>
        /// 时间戳
        /// </summary>
        public long? Timestamp { get; set; }
        /// <summary>
        /// 请求序号
        /// </summary>
        public long? RequestSn { get; set; }
        /// <summary>
        /// 错误代码
        /// </summary>
        public int? ErrorCode { get; set; }
        /// <summary>
        /// 事件
        /// </summary>
        public object Event { get; set; }
        /// <summary>
        /// 故障
        /// </summary>
        public object Fault { get; set; }
        /// <summary>
        /// 请求
        /// </summary>
        public object Request { get; set; }
        /// <summary>
        /// 响应
        /// </summary>
        public object Response { get; set; }

        /// <summary>
        /// 获取初始化
        /// </summary>
        /// <returns>Frame</returns>
        public static Frame GetInit()
        {
            return new Frame
            {
                Timestamp = new DateTimeOffset(DateTime.Now).ToUnixTimeMilliseconds()
            };
        }

    }
}
