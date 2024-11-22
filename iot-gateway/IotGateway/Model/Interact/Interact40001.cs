using System.Collections.Generic;

namespace IotGateway.Model.Interact
{

    /// <summary>
    /// 获取网关配置
    /// </summary>
    public class Interact4000001
    {

        /// <summary>
        /// 响应
        /// </summary>
        public class Response
        {

            /// <summary>
            /// 设备列表
            /// </summary>
            public List<Device> DeviceList { get; set; }

        }

        /// <summary>
        /// 设备
        /// </summary>
        public class Device
        {

            /// <summary>
            /// 设备序号
            /// </summary>
            public int Sn { get; set; }
            /// <summary>
            /// 设备类型 0网关 1温度计
            /// </summary>
            public int Type { get; set; }

        }

    }
}
