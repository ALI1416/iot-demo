namespace IotGateway.Model.Interact
{

    /// <summary>
    /// 设置网关通信地址
    /// </summary>
    public class Interact30100
    {

        /// <summary>
        /// 请求
        /// </summary>
        public class Request
        {

            /// <summary>
            /// 刷新间隔(s 秒)
            /// </summary>
            public int IntervalRefresh { get; set; }
            /// <summary>
            /// 推送间隔(s 秒)
            /// </summary>
            public int IntervalPush { get; set; }
            /// <summary>
            /// 温度上限(℃ 摄氏度)
            /// </summary>
            public int TemperatureMax { get; set; }
            /// <summary>
            /// 温度下限(℃ 摄氏度)
            /// </summary>
            public int TemperatureMin { get; set; }

        }

    }
}
