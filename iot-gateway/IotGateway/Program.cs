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
using Newtonsoft.Json.Serialization;

namespace IotGateway
{

    /// <summary>
    /// 物联网网关
    /// </summary>
    public class Program
    {

        /// <summary>
        /// 网关序号
        /// </summary>
        private static readonly int gatewaySn = 123;
        /// <summary>
        /// 设备序号
        /// </summary>
        private static readonly int deviceSn = 1;
        /// <summary>
        /// 请求超时时间(秒)[2分钟]
        /// </summary>
        private static readonly int timeout = 120;

        /// <summary>
        /// 请求序号Map 命令代码,请求序号,请求时间
        /// </summary>
        private static readonly Dictionary<int, Queue<Tuple<long, DateTime>>> map = new Dictionary<int, Queue<Tuple<long, DateTime>>>();

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
        private static readonly string subscribeTopic = "$iot/request/" + gatewaySn + "/+/+";
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
                string topic = GetMqttTopic(buffer);
                if (topic != null)
                {
                    Log(log);
                    // MQTT发送消息
                    MqttSend(topic, GetMqttMsg(buffer));
                }
                else
                {
                    Log(log + " 错误！");
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
                        return null;
                    }
                // 温度计事件
                case 0x00:
                    {
                        return msg.Length == 5 ? "$iot/event/" + gatewaySn + "/" + deviceSn + "/10100" : null;
                    }
                // 温度计故障
                case 0x40:
                    {
                        return msg.Length == 2 ? "$iot/fault/" + gatewaySn + "/" + deviceSn + "/20100" : null;
                    }
                // 设置温度计配置
                case 0xC0:
                    {
                        return msg.Length == 2 ? "$iot/response/" + gatewaySn + "/" + deviceSn + "/30100" : null;
                    }
                // 获取温度计配置
                case 0xC1:
                    {
                        return msg.Length == 5 ? "$iot/response/" + gatewaySn + "/" + deviceSn + "/40100" : null;
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
            Queue<Tuple<long, DateTime>> queue = map[commandCode];
            if (queue.Count == 0)
            {
                return 0;
            }
            else
            {
                return queue.Dequeue().Item1;
            }
        }

        /// <summary>
        /// 添加请求序号
        /// </summary>
        /// <param name="commandCode">命令代码</param>
        /// <param name="requestSn">请求序号</param>
        private static void AddRequestSn(int commandCode, long requestSn)
        {
            map[commandCode].Enqueue(new Tuple<long, DateTime>(requestSn, DateTime.Now));
        }

        /// <summary>
        /// 获取错误代码
        /// </summary>
        /// <param name="b">byte</param>
        /// <returns>错误代码</returns>
        private static int GetErrorCode(byte b)
        {
            return (int)(b == 0 ? ErrorCode.NoError : ErrorCode.DeviceResponseError);
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
                        return JsonConvert.SerializeObject(e);
                    }
                // 温度警报
                case 0x40:
                    {
                        return JsonConvert.SerializeObject(GetFaultList(msg));
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
                        return JsonConvert.SerializeObject(i);
                    }
            }
        }

        /// <summary>
        /// 获取故障列表
        /// </summary>
        /// <param name="msg">byte[]</param>
        /// <returns>List int</returns>
        private static List<int> GetFaultList(byte[] msg)
        {
            List<int> list = new List<int>(msg.Length - 1);
            for (int i = 1; i < msg.Length; i++)
            {
                list.Add(msg[i]);
            }
            return list;
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
            byte[] messageArray = msg.ApplicationMessage.PayloadSegment.Array;
            string message = null;
            string log = "MQTT接收到主题：" + topic + " ，消息：";
            if (messageArray == null)
            {
                Log(log);
            }
            else
            {
                message = Encoding.UTF8.GetString(messageArray);
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
        private static void MqttMsgHandle(string topic, string message)
        {
            string[] topicPart = topic.Split('/');
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
                // 设置通信地址
                case 30000:
                    {
                        Interact30000.Request r = JsonConvert.DeserializeObject<Interact30000.Request>(message);
                        if (r != null && r.Uri != null && r.Username != null && r.Password != null)
                        {
                            MqttSend(topicPrefix + (int)ErrorCode.NoError, null);
                        }
                        else
                        {
                            MqttSend(topicPrefix + (int)ErrorCode.MqttMsgError, null);
                        }
                        return;
                    }
                // 获取通信地址
                case 40000:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.NoError, JsonConvert.SerializeObject(interact40000Response));
                        return;
                    }
                // 获取网关配置
                case 40001:
                    {
                        MqttSend(topicPrefix + (int)ErrorCode.NoError, JsonConvert.SerializeObject(interact40001Response));
                        return;
                    }
                // 设置温度计配置
                case 30100:
                    {
                        if (DeviceOfflineHandle(topicPrefix))
                        {
                            return;
                        }
                        Interact30100.Request r = JsonConvert.DeserializeObject<Interact30100.Request>(message);
                        if (r != null
                            && r.IntervalRefresh != null && r.IntervalPush != null && r.TemperatureMax != null && r.TemperatureMin != null
                            && r.IntervalRefresh >= 1 && r.IntervalRefresh <= 60
                            && r.IntervalPush >= 1 && r.IntervalPush <= 60
                            && r.IntervalPush >= r.IntervalRefresh
                            && r.TemperatureMax >= -55 && r.TemperatureMax <= 99
                            && r.TemperatureMin >= -55 && r.TemperatureMin <= 99
                            && r.TemperatureMax > r.TemperatureMin
                            )
                        {
                            AddRequestSn(30100, requestSn);
                            byte[] msg = new byte[] {
                                0x80,
                                (byte) r.IntervalRefresh,
                                (byte) r.IntervalPush,
                                (byte) r.TemperatureMax,
                                (byte) r.TemperatureMin
                            };
                            SerialPortSend(msg);
                        }
                        else
                        {
                            MqttSend(topicPrefix + (int)ErrorCode.MqttMsgError, null);
                        }
                        return;
                    }
                // 获取温度计配置
                case 40100:
                    {
                        if (DeviceOfflineHandle(topicPrefix))
                        {
                            return;
                        }
                        AddRequestSn(40100, requestSn);
                        byte[] msg = new byte[] {
                                0x81
                            };
                        SerialPortSend(msg);
                        return;
                    }
            }
        }

        /// <summary>
        /// 设备离线处理
        /// </summary>
        /// <returns>设备离线</returns>
        private static bool DeviceOfflineHandle(string topicPrefix)
        {
            if (serialPort.IsOpen)
            {
                return false;
            }
            MqttSend(topicPrefix + (int)ErrorCode.DeviceOffline, null);
            return true;
        }

        /// <summary>
        /// 获取通信地址 响应
        /// </summary>
        private static readonly Interact40000.Response interact40000Response = new Interact40000.Response()
        {
            Uri = "mqtt://127.0.0.1:1883",
            Username = "account",
            Password = "pwd"
        };

        /// <summary>
        /// 获取网关配置 响应
        /// </summary>
        private static readonly Interact40001.Response interact40001Response = new Interact40001.Response()
        {
            DeviceList = new List<Interact40001.Device>()
            {
                new Interact40001.Device()
                {
                    Sn = 0,
                    Type = 0
                },
                new Interact40001.Device()
                {
                    Sn = 1,
                    Type = 1
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
        private static void Log(string text)
        {
            Console.WriteLine(DateTime.Now.ToString("[yyyy-MM-dd HH:mm:ss.fff]: ") + text);
        }

        /// <summary>
        /// 请求超时处理
        /// </summary>
        private static void RequestTimeoutHandle()
        {
            // 10秒轮询一次
            while (true)
            {
                DateTime now = DateTime.Now;
                Queue<Tuple<long, DateTime>> map30100 = map[30100];
                Queue<Tuple<long, DateTime>> map40100 = map[40100];
                for (int i = 0; i < map30100.Count; i++)
                {
                    Tuple<long, DateTime> tuple = map30100.Peek();
                    if (now.Subtract(tuple.Item2).TotalSeconds < timeout)
                    {
                        break;
                    }
                    map30100.Dequeue();
                    MqttSend("$iot/response/" + gatewaySn + "/1/30100/" + tuple.Item1 + "/" + (int)ErrorCode.DeviceRequestTimeout, null);
                }
                for (int i = 0; i < map40100.Count; i++)
                {
                    Tuple<long, DateTime> tuple = map40100.Peek();
                    if (now.Subtract(tuple.Item2).TotalSeconds < timeout)
                    {
                        break;
                    }
                    map40100.Dequeue();
                    MqttSend("$iot/response/" + gatewaySn + "/1/40100/" + tuple.Item1 + "/" + (int)ErrorCode.DeviceRequestTimeout, null);
                }
                Thread.Sleep(10000);
            }
        }

        public static void Main(string[] args)
        {
            // 请求序号Map初始化
            map.Add(30100, new Queue<Tuple<long, DateTime>>());
            map.Add(40100, new Queue<Tuple<long, DateTime>>());
            // JSON默认序列化设置
            JsonConvert.DefaultSettings = () => new JsonSerializerSettings()
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver()
            };
            // 串口初始化
            Task serialPortInit = new Task(() => SerialPortInit());
            serialPortInit.Start();
            // MQTT初始化
            Task mqttInit = new Task(() => MqttInit());
            mqttInit.Start();
            // 请求超时处理
            Task requestTimeoutHandle = new Task(() => RequestTimeoutHandle());
            requestTimeoutHandle.Start();
            Task.WaitAll(serialPortInit);
            Task.WaitAll(mqttInit);
            Task.WaitAll(requestTimeoutHandle);
        }

    }
}
