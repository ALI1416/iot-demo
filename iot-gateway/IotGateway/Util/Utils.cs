using IotGateway.Model.Interact;
using IotGateway.Model;
using System.Collections.Generic;
using System.Text;

namespace IotGateway.Util
{

    /// <summary>
    /// 工具
    /// </summary>
    public class Utils
    {

        /// <summary>
        /// byte[4]转int(高位在前)
        /// </summary>
        /// <param name="data">byte[4]</param>
        /// <returns>int</returns>
        public static int Byte4ToInt(byte[] data)
        {
            return (data[0] << 24) | (data[1] << 16) | (data[2] << 8) | data[3];
        }

        /// <summary>
        /// 获取故障列表
        /// </summary>
        /// <param name="data">byte[]</param>
        /// <returns>List int</returns>
        public static List<int> GetFaultList(byte[] data)
        {
            List<int> list = new List<int>(data.Length - 1);
            for (int i = 1; i < data.Length; i++)
            {
                list.Add(data[i]);
            }
            return list;
        }

        /// <summary>
        /// 获取响应错误代码
        /// </summary>
        /// <param name="data">byte[]</param>
        /// <returns>错误代码</returns>
        public static int GetResponseErrorCode(byte[] data)
        {
            return (int)(data[1] == 0 ? ErrorCode.NoError : ErrorCode.DeviceResponseError);
        }

        /// <summary>
        /// byte[]转Hex字符串
        /// </summary>
        /// <param name="bytes">byte[]</param>
        /// <returns>Hex字符串</returns>
        public static string Bytes2Hex(byte[] bytes)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.Length; i++)
            {
                sb.Append(bytes[i].ToString("X2"));
                sb.Append(" ");
            }
            sb.Remove(sb.Length - 1, 1);
            return sb.ToString();
        }

        /// <summary>
        /// 获取通信地址 响应
        /// </summary>
        public static readonly Interact4000000.Response Interact4000000Response = new Interact4000000.Response()
        {
            Uri = "mqtt://127.0.0.1:1883",
            Username = "account",
            Password = "pwd"
        };

        /// <summary>
        /// 获取网关配置 响应
        /// </summary>
        public static readonly Interact4000001.Response Interact4000001Response = new Interact4000001.Response()
        {
            DeviceList = new List<Interact4000001.Device>()
            {
                new Interact4000001.Device()
                {
                    Sn = 0,
                    Type = 0
                },
                new Interact4000001.Device()
                {
                    Sn = 1,
                    Type = 1
                }
            }
        };

    }
}
