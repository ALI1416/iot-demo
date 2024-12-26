namespace IotGateway.Model.Event
{

    /// <summary>
    /// 温度计事件
    /// </summary>
    public class Event1000100
    {

        /// <summary>
        /// 温度计事件
        /// </summary>
        public class Event
        {

            /// <summary>
            /// 温度(0.0001℃ 0.0001摄氏度)
            /// </summary>
            public int? Temperature { get; set; }

        }

    }
}
