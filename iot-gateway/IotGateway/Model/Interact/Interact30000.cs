namespace IotGateway.Model.Interact
{

    /// <summary>
    /// 设置通信地址
    /// </summary>
    public class Interact30000
    {

        /// <summary>
        /// 请求
        /// </summary>
        public class Request
        {

            /// <summary>
            /// URI
            /// </summary>
            public string Uri { get; set; }
            /// <summary>
            /// 用户名
            /// </summary>
            public string Username { get; set; }
            /// <summary>
            /// 密码
            /// </summary>
            public string Password { get; set; }

        }

    }
}
