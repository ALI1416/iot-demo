using System;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Linq;
using System.Collections.Generic;
using log4net;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Newtonsoft.Json.Serialization;
using IotGateway.Model;
using IotGateway.Model.Event;
using IotGateway.Model.Interact;
using IotGateway.Service;
using IotGateway.Util;
using IotGateway.Model.Communication;
using IotGateway.Model.Broadcast;

namespace IotGateway
{

    /// <summary>
    /// 物联网网关
    /// </summary>
    public class Program
    {

        private static readonly ILog log = LogManager.GetLogger(typeof(Program));

        private static readonly int reconnectTime = 10;

        private static readonly SerialPortService serialPortService = new SerialPortService();
        private static readonly string portName = "COM3";
        private static readonly int baudRate = 4800;

        private static readonly MqttService mqttService = new MqttService();
        private static readonly string ip = "127.0.0.1";
        private static readonly int port = 1883;
        private static readonly string username = null;
        private static readonly string password = null;

        /// <summary>
        /// 网关序号
        /// </summary>
        private static readonly int gatewaySn = 123;
        /// <summary>
        /// 设备序号
        /// </summary>
        private static readonly int deviceSn = 1;
        /// <summary>
        /// 请求超时时间(秒)[1分钟]
        /// </summary>
        private static readonly int timeout = 60;

        private static readonly string[] subscribeTopicArray = new string[] {
            $"$iot/request/{gatewaySn}/+/+",
            $"$iot/write/{gatewaySn}/+/+",
            "$iot/broadcast/+",
        };

        /// <summary>
        /// 请求序号Map 命令代码,请求序号,请求时间
        /// </summary>
        private static readonly Dictionary<int, Queue<Tuple<long, DateTime>>> map = new Dictionary<int, Queue<Tuple<long, DateTime>>>()
        {
            { 3000100, new Queue<Tuple<long, DateTime>>() },
            { 4000100, new Queue<Tuple<long, DateTime>>() }
        };

        /// <summary>
        /// 串口接收消息回调函数
        /// </summary>
        /// <param name="data">消息</param>
        private static void SerialPortReceiveCallback(byte[] data)
        {
            string logger = $"串口收到消息:[{Utils.Bytes2Hex(data)}]";
            string topic = null;
            Frame frame = new Frame();
            string type = "";
            switch (data[0])
            {
                // 温度计事件
                case 0x00:
                    {
                        if (data.Length == 5)
                        {
                            topic = $"$iot/event/{gatewaySn}/{deviceSn}/1000100";
                            frame.Event = new Event1000100.Event
                            {
                                Temperature = Utils.Byte4ToInt(data.Skip(1).Take(4).ToArray())
                            };
                            type = "事件";
                        }
                        break;
                    }
                // 温度计警报
                case 0x40:
                    {
                        if (data.Length == 2)
                        {
                            topic = $"$iot/fault/{gatewaySn}/{deviceSn}/2000100";
                            frame.Fault = Utils.GetFaultList(data);
                            type = "故障";
                        }
                        break;
                    }
                // 设置温度计配置
                case 0xC0:
                    {
                        if (data.Length == 2)
                        {
                            topic = $"$iot/response/{gatewaySn}/{deviceSn}/3000100";
                            frame.RequestSn = GetRequestSn(3000100);
                            frame.ErrorCode = Utils.GetResponseErrorCode(data);
                            type = "交互";
                        }
                        break;
                    }
                // 获取温度计配置
                case 0xC1:
                    {
                        if (data.Length == 5)
                        {
                            topic = $"$iot/response/{gatewaySn}/{deviceSn}/4000100";
                            frame.RequestSn = GetRequestSn(4000100);
                            frame.ErrorCode = (int)ErrorCode.NoError;
                            frame.Response = new Interact4000100.Response()
                            {
                                IntervalRefresh = (sbyte)data[1],
                                IntervalPush = (sbyte)data[2],
                                TemperatureMax = (sbyte)data[3],
                                TemperatureMin = (sbyte)data[4]
                            };
                            type = "交互";
                        }
                        break;
                    }
            }
            if (topic != null)
            {
                string message = JsonConvert.SerializeObject(frame);
                mqttService.Send(topic, Encoding.UTF8.GetBytes(message));
                log.Info(logger + $"\n  MQTT发送{type}主题:[{topic}], 消息:[{message}]");
            }
            // 无法解析
            else
            {
                log.Warn(logger + "\n  无法解析！");
            }
        }

        /// <summary>
        /// MQTT接收消息回调函数
        /// </summary>
        /// <param name="topic">主题</param>
        /// <param name="data">消息</param>
        private static void MqttReceiveCallback(string topic, byte[] data)
        {
            string message = Encoding.UTF8.GetString(data);
            Frame frame = JsonConvert.DeserializeObject<Frame>(message);
            string[] topicPart = topic.Split('/');
            string logger = "MQTT收到";
            switch (topicPart[1])
            {
                // 交互
                case "request":
                    {
                        logger += $"交互主题:[{topic}], 消息:[{message}]";
                        MqttInteractHandle(topicPart, frame, logger);
                        break;
                    }
                // 交流
                case "write":
                    {
                        logger += $"交流主题:[{topic}], 消息:[{message}]";
                        MqttCommunicationHandle(topicPart, frame, logger);
                        break;
                    }
                // 广播
                case "broadcast":
                    {
                        logger += $"广播主题:[{topic}], 消息:[{message}]";
                        MqttBroadcastHandle(topicPart, frame, logger);
                        break;
                    }
            }
        }

        /// <summary>
        /// MQTT交互消息处理
        /// <param name="topicPart">主题片段</param>
        /// <param name="frame">协议帧</param>
        /// <param name="logger">日志</param>
        /// </summary>
        private static void MqttInteractHandle(string[] topicPart, Frame frame, string logger)
        {
            bool mqttSend = true;
            int deviceSn;
            int commandCode;
            long requestSn;
            try
            {
                deviceSn = int.Parse(topicPart[3]);
                commandCode = int.Parse(topicPart[4]);
            }
            // 主题错误
            catch
            {
                log.Warn(logger + "\n  主题错误！");
                return;
            }
            long? requestSnN = frame.RequestSn;
            // 请求序号错误
            if (requestSnN == null || requestSnN <= 0)
            {
                log.Warn(logger + "\n  请求序号错误！");
                return;
            }
            requestSn = (long)requestSnN;
            string mqttTopic = $"$iot/response/{gatewaySn}/{deviceSn}/{commandCode}";
            Frame mqttFrame = new Frame
            {
                RequestSn = requestSn
            };
            if ((deviceSn == 0) || (deviceSn == Program.deviceSn))
            {
                JObject request = (JObject)frame.Request;
                switch (commandCode)
                {
                    // 命令代码错误
                    default:
                        {
                            mqttFrame.ErrorCode = (int)ErrorCode.CommandCodeError;
                            logger += "\n  命令代码错误！";
                            break;
                        }
                    // 设置通信地址
                    case 3000000:
                        {
                            if (request != null)
                            {
                                Interact3000000.Request r = request.ToObject<Interact3000000.Request>();
                                if (r != null
                                    && r.Uri != null && r.Username != null && r.Password != null)
                                {
                                    mqttFrame.ErrorCode = (int)ErrorCode.NoError;
                                }
                            }
                            break;
                        }
                    // 获取通信地址
                    case 4000000:
                        {
                            mqttFrame.ErrorCode = (int)ErrorCode.NoError;
                            mqttFrame.Response = Utils.Interact4000000Response;
                            break;
                        }
                    // 获取网关配置
                    case 4000001:
                        {
                            mqttFrame.ErrorCode = (int)ErrorCode.NoError;
                            mqttFrame.Response = Utils.Interact4000001Response;
                            break;
                        }
                    // 设置温度计配置
                    case 3000100:
                        {
                            if (serialPortService.IsOpen())
                            {
                                if (request != null)
                                {
                                    Interact3000100.Request r = request.ToObject<Interact3000100.Request>();
                                    if (r != null
                                        && r.IntervalRefresh != null && r.IntervalPush != null
                                        && r.TemperatureMax != null && r.TemperatureMin != null
                                        && r.IntervalRefresh >= 1 && r.IntervalRefresh <= 60
                                        && r.IntervalPush >= 1 && r.IntervalPush <= 60
                                        && r.IntervalPush >= r.IntervalRefresh
                                        && r.TemperatureMax >= -55 && r.TemperatureMax <= 99
                                        && r.TemperatureMin >= -55 && r.TemperatureMin <= 99
                                        && r.TemperatureMax > r.TemperatureMin)
                                    {
                                        byte[] msg = new byte[] {
                                            0x80,
                                            (byte) r.IntervalRefresh,
                                            (byte) r.IntervalPush,
                                            (byte) r.TemperatureMax,
                                            (byte) r.TemperatureMin
                                        };
                                        if (serialPortService.Send(msg))
                                        {
                                            mqttSend = false;
                                            mqttFrame.ErrorCode = (int)ErrorCode.NoError;
                                            AddRequestSn(3000100, requestSn);
                                        }
                                        else
                                        {
                                            mqttFrame.ErrorCode = (int)ErrorCode.DeviceOffline;
                                        }
                                    }
                                }
                            }
                            else
                            {
                                mqttFrame.ErrorCode = (int)ErrorCode.DeviceOffline;
                            }
                            break;
                        }
                    // 获取温度计配置
                    case 4000100:
                        {
                            if (serialPortService.IsOpen())
                            {
                                byte[] msg = new byte[] {
                                            0x81
                                        };
                                if (serialPortService.Send(msg))
                                {
                                    mqttSend = false;
                                    mqttFrame.ErrorCode = (int)ErrorCode.NoError;
                                    AddRequestSn(4000100, requestSn);
                                }
                                else
                                {
                                    mqttFrame.ErrorCode = (int)ErrorCode.DeviceOffline;
                                }
                            }
                            else
                            {
                                mqttFrame.ErrorCode = (int)ErrorCode.DeviceOffline;
                            }
                            break;
                        }
                }
            }
            // 设备不存在
            else
            {
                mqttFrame.ErrorCode = (int)ErrorCode.DeviceNotFound;
                logger += "\n  设备不存在！";
            }
            // MQTT消息错误
            if (mqttFrame.ErrorCode == null)
            {
                mqttFrame.ErrorCode = (int)ErrorCode.MqttMessageError;
                logger += "\n  MQTT消息错误！";
            }
            if (mqttSend)
            {
                string mqttMessage = JsonConvert.SerializeObject(mqttFrame);
                mqttService.Send(mqttTopic, Encoding.UTF8.GetBytes(mqttMessage));
                logger += $"\n  MQTT发送交互主题:[{mqttTopic}], 消息:[{mqttMessage}]";
            }
            if (mqttFrame.ErrorCode == (int)ErrorCode.NoError)
            {
                log.Info(logger);
            }
            else
            {
                log.Warn(logger);
            }
        }

        /// <summary>
        /// MQTT交流消息处理
        /// <param name="topicPart">主题片段</param>
        /// <param name="frame">协议帧</param>
        /// <param name="logger">日志</param>
        /// </summary>
        private static void MqttCommunicationHandle(string[] topicPart, Frame frame, string logger)
        {
            bool messageError = true;
            int deviceSn;
            int commandCode;
            try
            {
                deviceSn = int.Parse(topicPart[3]);
                commandCode = int.Parse(topicPart[4]);
            }
            // 主题错误
            catch
            {
                log.Warn(logger + "\n  主题错误！");
                return;
            }
            // 错误代码错误
            if (frame.ErrorCode != (int)ErrorCode.NoError)
            {
                log.Warn(logger + "\n  错误代码错误！");
                return;
            }
            if ((deviceSn == 0) || (deviceSn == Program.deviceSn))
            {
                JObject write = (JObject)frame.Write;
                switch (commandCode)
                {
                    // 命令代码错误
                    default:
                        {
                            log.Warn(logger + "\n  命令代码错误！");
                            return;
                        }
                    // 获取时间戳
                    case 6000000:
                        {
                            if (write != null)
                            {
                                Communication6000000.Write w = write.ToObject<Communication6000000.Write>();
                                if (w != null && w.Timestamp != null)
                                {
                                    messageError = false;
                                    logger += "\n  获取时间戳 " + DateTimeOffset.FromUnixTimeMilliseconds((long)w.Timestamp);
                                }
                            }
                            break;
                        }
                }
            }
            // 设备不存在
            else
            {
                log.Warn(logger + "\n  设备不存在！");
                return;
            }
            if (messageError)
            {
                log.Warn(logger + "\n  消息错误！");
            }
            else
            {
                log.Info(logger);
            }
        }

        /// <summary>
        /// MQTT广播消息处理
        /// <param name="topicPart">主题片段</param>
        /// <param name="frame">协议帧</param>
        /// <param name="logger">日志</param>
        /// </summary>
        private static void MqttBroadcastHandle(string[] topicPart, Frame frame, string logger)
        {
            bool messageError = true;
            int commandCode;
            try
            {
                commandCode = int.Parse(topicPart[2]);
            }
            // 主题错误
            catch
            {
                log.Warn(logger + "\n  主题错误！");
                return;
            }
            JObject broadcast = (JObject)frame.Broadcast;
            switch (commandCode)
            {
                // 命令代码错误
                default:
                    {
                        log.Warn(logger + "\n  命令代码错误！");
                        return;
                    }
                // 校时广播
                case 5000000:
                    {
                        if (broadcast != null)
                        {
                            Broadcast5000000.Broadcast b = broadcast.ToObject<Broadcast5000000.Broadcast>();
                            if (b != null && b.Timestamp != null)
                            {
                                messageError = false;
                                logger += "\n  校时广播 " + DateTimeOffset.FromUnixTimeMilliseconds((long)b.Timestamp);
                            }
                        }
                        break;
                    }
            }
            if (messageError)
            {
                log.Warn(logger + "\n  消息错误！");
            }
            else
            {
                log.Info(logger);
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
        /// 请求超时处理
        /// </summary>
        private static void RequestTimeoutHandle()
        {
            // 10秒轮询一次
            while (true)
            {
                DateTime now = DateTime.Now;
                Queue<Tuple<long, DateTime>> map3000100 = map[3000100];
                Queue<Tuple<long, DateTime>> map4000100 = map[4000100];
                for (int i = 0; i < map3000100.Count; i++)
                {
                    Tuple<long, DateTime> tuple = map3000100.Peek();
                    if (now.Subtract(tuple.Item2).TotalSeconds < timeout)
                    {
                        break;
                    }
                    map3000100.Dequeue();
                    SendTimeoutMessage($"$iot/response/{gatewaySn}/{deviceSn}/3000100", tuple.Item1);
                }
                for (int i = 0; i < map4000100.Count; i++)
                {
                    Tuple<long, DateTime> tuple = map4000100.Peek();
                    if (now.Subtract(tuple.Item2).TotalSeconds < timeout)
                    {
                        break;
                    }
                    map4000100.Dequeue();
                    SendTimeoutMessage($"$iot/response/{gatewaySn}/{deviceSn}/4000100", tuple.Item1);
                }
                Thread.Sleep(10000);
            }
        }

        /// <summary>
        /// 发送 设备执行超时 消息
        /// </summary>
        /// <param name="topic">主题</param>
        /// <param name="requestSn">请求序号</param>
        private static void SendTimeoutMessage(string topic, long requestSn)
        {
            Frame frame = new Frame
            {
                RequestSn = requestSn,
                ErrorCode = (int)ErrorCode.DeviceRequestTimeout
            };
            string message = JsonConvert.SerializeObject(frame);
            mqttService.Send(topic, Encoding.UTF8.GetBytes(message));
            log.Warn($"设备执行超时！\n  MQTT发送交互主题:[{topic}], 消息:[{message}]");
        }

        /// <summary>
        /// 获取时间戳处理
        /// </summary>
        private static void GetTimestampHandle()
        {
            Thread.Sleep(10000);
            // 60秒轮询一次
            while (true)
            {
                string topic = $"$iot/read/{gatewaySn}/0/6000000";
                string message = "{}";
                mqttService.Send(topic, Encoding.UTF8.GetBytes(message));
                log.Info($"获取时间戳\n  MQTT发送交流主题:[{topic}], 消息:[{message}]");
                Thread.Sleep(60000);
            }
        }

        public static void Main(string[] args)
        {
            // JSON默认序列化设置
            JsonConvert.DefaultSettings = () => new JsonSerializerSettings()
            {
                // 首字母小写
                ContractResolver = new CamelCasePropertyNamesContractResolver(),
                // 不显示null值
                NullValueHandling = NullValueHandling.Ignore,
            };
            // 串口初始化
            Task serialPortInit = new Task(() => serialPortService.Start(portName, baudRate, reconnectTime, SerialPortReceiveCallback));
            serialPortInit.Start();
            // MQTT初始化
            Task mqttInit = new Task(() => mqttService.Start(ip, port, username, password, reconnectTime, subscribeTopicArray, MqttReceiveCallback));
            mqttInit.Start();
            // 请求超时处理
            Task requestTimeoutHandle = new Task(() => RequestTimeoutHandle());
            requestTimeoutHandle.Start();
            // 获取时间戳处理
            Task getTimestampHandle = new Task(() => GetTimestampHandle());
            getTimestampHandle.Start();
            Task.WaitAll(serialPortInit);
            Task.WaitAll(mqttInit);
            Task.WaitAll(requestTimeoutHandle);
            Task.WaitAll(getTimestampHandle);
        }

    }
}
