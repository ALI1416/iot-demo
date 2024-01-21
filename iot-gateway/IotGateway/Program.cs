using System.IO.Ports;
using System.Text;
using System.Threading;
using System;
using MQTTnet.Client;
using MQTTnet;
using System.Threading.Tasks;
using System.Linq;
using IotGateway.Model;
using Newtonsoft.Json;
using System.Collections.Generic;
using IotGateway.Model.Interact;

namespace IotGateway
{

    /// <summary>
    /// 物联网网关
    /// </summary>
    public class Program
    {

        /// <summary>
        /// JSON序列化设置
        /// </summary>
        private static readonly JsonSerializerSettings jsonSerializerSettings = new JsonSerializerSettings
        {
            ContractResolver = new Newtonsoft.Json.Serialization.CamelCasePropertyNamesContractResolver()
        };

        /// <summary>
        /// 网关序号
        /// </summary>
        private static readonly int gatewaySn = 123;
        /// <summary>
        /// 设备序号
        /// </summary>
        private static readonly int deviceSn = 1;

        /// <summary>
        /// 请求序号Map<命令代码,请求序号队列>
        /// </summary>
        private static readonly Dictionary<int, Queue<long>> map = new Dictionary<int, Queue<long>>();

        /// <summary>
        /// 串口配置
        /// </summary>
        private static readonly SerialPort serialPort = new SerialPort
        {
            PortName = "COM3",
            BaudRate = 4800,
            Parity = Parity.None,
            DataBits = 8,
            StopBits = StopBits.One,
        };

        /// <summary>
        /// MQTT订阅主题
        /// </summary>
        private static readonly string subscribeTopic = "$iot/request/" + gatewaySn + "/#";
        /// <summary>
        /// MQTT连接选项
        /// </summary>
        private static readonly MqttClientOptions connectOptions = new MqttClientOptionsBuilder()
            .WithTcpServer("127.0.0.1", 1883)
            .Build();

        /// <summary>
        /// MQTT客户端
        /// </summary>
        private static IMqttClient client;

        // 数据交互格式：
        // 事件(单片机 --> 网关)：0x00 - 0x3F
        //   温度事件：0x00
        //     温度x10000(32bit)
        // 故障(单片机 --> 网关)：0x40 - 0x7F
        //   温度报警：0x40
        //     温度报警状态(8bit)
        // 互动请求(网关 --> 单片机)：0x80 - 0xBF
        //   设置温度参数：0x80
        //     F温度读取时间间隔 C串口发送时间间隔 H高温报警 L低温报警(32bit)
        //   读取温度参数：0x81
        // 互动响应(单片机 --> 网关)：0xC0 - 0xFF
        //   设置温度参数：0xC0
        //     设置状态(8bit) 0x00成功 0x01失败
        //   读取温度参数：0xC1
        //     F温度读取时间间隔 C串口发送时间间隔 H高温报警 L低温报警(32bit)

        /// <summary>
        /// 串口初始化
        /// </summary>
        private static void SerialPortInit()
        {
            // 接收消息处理
            serialPort.DataReceived += SerialPortReceive;
            // 建立连接(失败10秒后重连)
            while (true)
            {
                if (!serialPort.IsOpen)
                {
                    // 连接串口
                    try
                    {
                        serialPort.Open();
                        Log("串口建立连接成功！");
                    }
                    catch
                    {
                        Log("串口建立连接失败！等待重连...");
                    }
                }
                Thread.Sleep(10000);
            }
        }

        /// <summary>
        /// 串口接收消息处理
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e">SerialDataReceivedEventArgs</param>
        private static void SerialPortReceive(object sender, SerialDataReceivedEventArgs e)
        {
            // 读取串口数据
            int length = serialPort.BytesToRead;
            if (length > 0)
            {
                byte[] buffer = new byte[length];
                serialPort.Read(buffer, 0, length);
                string log = "串口收到消息：" + Bytes2Hex(buffer);
                if (SerialPortReceiveMsgCheck(buffer))
                {
                    Log(log);
                    // MQTT发送消息
                    MqttSend(GetMqttTopic(buffer), GetMqttMsg(buffer));
                }
                else
                {
                    Log(log + " 错误！");
                }
            }
        }

        /// <summary>
        /// 串口接收消息检查
        /// </summary>
        /// <param name="msg">消息</param>
        /// <returns>是否正确</returns>
        private static bool SerialPortReceiveMsgCheck(byte[] msg)
        {
            switch (msg[0])
            {
                default:
                    {
                        return false;
                    }
                // 温度事件
                case 0x00:
                    {
                        return msg.Length == 5;
                    }
                // 温度报警
                case 0x40:
                    {
                        return msg.Length == 2;
                    }
                // 设置温度参数
                case 0xC0:
                    {
                        return msg.Length == 2;
                    }
                // 读取温度参数
                case 0xC1:
                    {
                        return msg.Length == 5;
                    }
            }
        }

        /// <summary>
        /// 获取MQTT主题
        /// </summary>
        /// <param name="msg">消息</param>
        /// <returns>主题</returns>
        private static string GetMqttTopic(byte[] msg)
        {
            switch (msg[0])
            {
                default:
                    {
                        return "";
                    }
                // 温度事件
                case 0x00:
                    {
                        return "$iot/event/" + gatewaySn + "/" + deviceSn + "/10100";
                    }
                // 温度报警
                case 0x40:
                    {
                        return "$iot/fault/" + gatewaySn + "/" + deviceSn + "/20100";
                    }
                // 设置温度参数
                case 0xC0:
                    {
                        return "$iot/response/" + gatewaySn + "/" + deviceSn + "/30100/" +
                            GetRequestSn(30100) + "/" + GetErrorCode(msg[0]);
                    }
                // 读取温度参数
                case 0xC1:
                    {
                        return "$iot/response/" + gatewaySn + "/" + deviceSn + "/40100/" +
                            GetRequestSn(40100) + "/0";
                    }
            }
        }

        /// <summary>
        /// 获取请求序号
        /// </summary>
        /// <param name="commandCode">命令代码</param>
        /// <returns>请求序号(不存在返回0)</returns>
        private static long GetRequestSn(int commandCode)
        {
            map.TryGetValue(commandCode, out Queue<long> queue);
            if (queue == null || queue.Count == 0)
            {
                return 0;
            }
            else
            {
                return queue.Dequeue();
            }
        }

        /// <summary>
        /// 获取错误代码
        /// </summary>
        /// <param name="b">byte</param>
        /// <returns>错误代码</returns>
        private static int GetErrorCode(byte b)
        {
            return (int)(b == 0 ? ErrorCode.NoError : ErrorCode.DeviceReturnError);
        }

        /// <summary>
        /// 获取MQTT消息
        /// </summary>
        /// <param name="msg">消息</param>
        /// <returns>消息</returns>
        private static string GetMqttMsg(byte[] msg)
        {
            switch (msg[0])
            {
                default:
                    {
                        return null;
                    }
                // 温度事件
                case 0x00:
                    {
                        Event10100.Event e = new Event10100.Event
                        {
                            Temperature = Bytes2Int(msg.Skip(1).Take(4).ToArray()) / 10
                        };
                        return JsonConvert.SerializeObject(e, jsonSerializerSettings);
                    }
                // 温度报警
                case 0x40:
                    {
                        List<int> list = new List<int>
                        {
                            msg[1]
                        };
                        return JsonConvert.SerializeObject(list);
                    }
                // 读取温度参数
                case 0xC1:
                    {
                        Interact40100.Response i = new Interact40100.Response()
                        {
                            IntervalRefresh = msg[1],
                            IntervalPush = msg[2],
                            TemperatureMax = msg[3],
                            TemperatureMin = msg[4]
                        };
                        return JsonConvert.SerializeObject(i, jsonSerializerSettings);
                    }
            }
        }

        /// <summary>
        /// 串口发送消息
        /// </summary>
        /// <param name="message">消息</param>
        private static void SerialPortSend(byte[] message)
        {
            if (serialPort.IsOpen)
            {
                try
                {
                    // 发送消息
                    serialPort.Write(message, 0, message.Length);
                    Log("串口发送消息：" + Bytes2Hex(message));
                }
                catch
                {
                    Log("串口发送消息失败！");
                }
            }
            else
            {
                Log("串口连接未建立，发送失败！");
            }
        }

        /// <summary>
        /// MQTT初始化
        /// </summary>
        private static async void MqttInit()
        {
            client = new MqttFactory().CreateMqttClient();
            // 建立连接(失败10秒后重连)
            while (true)
            {
                try
                {
                    await client.ConnectAsync(connectOptions);
                    // 连接关闭处理
                    client.DisconnectedAsync += MqttDisconnected;
                    Log("MQTT建立连接成功！");
                    break;
                }
                catch
                {
                    Log("MQTT建立连接失败！等待重连...");
                    await Task.Delay(10000);
                }
            }
            // 订阅主题
            try
            {
                await client.SubscribeAsync(subscribeTopic);
                Log("MQTT订阅主题成功！");
            }
            catch
            {
                Log("MQTT订阅主题失败！");
                return;
            }
            // 接收消息处理
            client.ApplicationMessageReceivedAsync += MqttReceive;
        }

        /// <summary>
        /// MQTT连接关闭处理
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e">MqttClientDisconnectedEventArgs</param>
        /// <returns>Task</returns>
        private static Task MqttDisconnected(MqttClientDisconnectedEventArgs arg)
        {
            Log("MQTT连接关闭！等待重连...");
            MqttInit();
            return Task.CompletedTask;
        }

        /// <summary>
        /// MQTT接收消息处理
        /// </summary>
        /// <param name="msg">MqttApplicationMessageReceivedEventArgs</param>
        /// <returns>Task</returns>
        private static Task MqttReceive(MqttApplicationMessageReceivedEventArgs msg)
        {
            string topic = msg.ApplicationMessage.Topic;
            byte[] message = msg.ApplicationMessage.PayloadSegment.Array;
            string log = "MQTT接收到主题：" + topic + " ，消息：";
            if (message == null)
            {
                Log(log);
            }
            else
            {
                Log(log + message);
            }
            MqttMsgHandle(topic, message);
            return Task.CompletedTask;
        }

        /// <summary>
        /// MQTT消息处理
        /// </summary>
        /// <param name="topic">主题</param>
        /// <param name="message">消息</param>
        private static void MqttMsgHandle(string topic, byte[] message)
        {
            string[] topicPart = topic.Split('/');
            if (topicPart.Length != 6)
            {
                // 格式错误
                return;
            }
            string deviceSnString = topicPart[3];
            string commandCodeString = topicPart[4];
            string requestSnString = topicPart[5];
            int deviceSn;
            int commandCode;
            long requestSn;
            string topicPrefix = "$iot/response/" + gatewaySn + "/" + deviceSnString + "/" + commandCodeString + "/" + requestSnString + "/";
            try
            {
                deviceSn = int.Parse(deviceSnString);
                commandCode = int.Parse(commandCodeString);
                requestSn = long.Parse(requestSnString);
            }
            catch
            {
                // MQTT主题错误
                MqttSend(topicPrefix + (int)ErrorCode.MqttTopicError, null);
                return;
            }
            if (!((deviceSn == 0) || (deviceSn == Program.deviceSn)))
            {
                // 设备不存在
                MqttSend(topicPrefix + (int)ErrorCode.DeviceNotFound, null);
                return;
            }
            if (requestSn <= 0)
            {
                // 请求序号错误
                MqttSend(topicPrefix + (int)ErrorCode.RequestSnError, null);
                return;
            }
            switch (commandCode)
            {
                // 命令代码错误
                default:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.CommandCodeError, null);
                        return;
                    }
                // 设置网关通信地址
                case 30000:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.NoError, null);
                        return;
                    }
                // 设置温度计参数
                case 30100:
                    {
                        return;
                    }
                // 获取网关通信地址
                case 40000:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.NoError, JsonConvert.SerializeObject(interact40000Response, jsonSerializerSettings));
                        return;
                    }
                // 获取网关信息
                case 40001:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.NoError, JsonConvert.SerializeObject(interact40001Response, jsonSerializerSettings));
                        return;
                    }
                // 获取温度计参数
                case 40100:
                    {
                        return;
                    }
            }
        }

        /// <summary>
        /// 获取网关通信地址 响应
        /// </summary>
        private static readonly Interact40000.Response interact40000Response = new Interact40000.Response()
        {
            Uri = "mqtt://127.0.0.1:1883",
            Username = "",
            Password = ""
        };

        /// <summary>
        /// 获取网关信息
        /// </summary>
        private static readonly Interact40001.Response interact40001Response = new Interact40001.Response()
        {
            DeviceList = new List<Interact40001.Device>()
            {
                new Interact40001.Device()
                {
                    Type=0,
                    Sn=0
                },
                new Interact40001.Device()
                {
                    Type=1,
                    Sn=1
                }
            }
        };

        /// <summary>
        /// MQTT发送消息
        /// </summary>
        /// <param name="topic">主题</param>
        /// <param name="message">消息</param>
        private static void MqttSend(string topic, string message)
        {
            if (client != null && client.IsConnected)
            {
                try
                {
                    // 发送消息
                    client.PublishStringAsync(topic, message);
                    Log("MQTT发送到主题：" + topic + " ，消息：" + message);
                }
                catch
                {
                    Log("MQTT发送消息失败！");
                }
            }
            else
            {
                Log("MQTT连接未建立，发送失败！");
            }
        }

        /// <summary>
        /// byte[]转Hex字符串
        /// </summary>
        /// <param name="bytes">byte[]</param>
        /// <returns>Hex字符串</returns>
        private static string Bytes2Hex(byte[] bytes)
        {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.Length; i++)
            {
                sb.Append(bytes[i].ToString("X2"));
                sb.Append(" ");
            }
            return sb.ToString();
        }

        /// <summary>
        /// byte[4]转int(高位在前)
        /// </summary>
        /// <param name="data">byte[4]</param>
        /// <returns>int</returns>
        private static int Bytes2Int(byte[] data)
        {
            return (data[0] << 24) | (data[1] << 16) | (data[2] << 8) | data[3];
        }

        /// <summary>
        /// 打印日志
        /// </summary>
        /// <param name="text">文本</param>
        private static void Log(String text)
        {
            Console.WriteLine(DateTime.Now.ToString("[yyyy-MM-dd HH:mm:ss.fff]: ") + text);
        }

        public static void Main(string[] args)
        {
            // 串口初始化
            new Thread(t =>
            {
                SerialPortInit();
            })
            {
                IsBackground = true
            }.Start();
            // MQTT初始化
            new Thread(t =>
            {
                MqttInit();
            })
            {
                IsBackground = true
            }.Start();
            Console.ReadLine();
        }

    }
}
