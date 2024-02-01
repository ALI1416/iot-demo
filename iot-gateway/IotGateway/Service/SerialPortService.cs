using System;
using System.IO.Ports;
using System.Threading.Tasks;

namespace IotGateway.Service
{

    /// <summary>
    /// 串口服务
    /// </summary>
    public class SerialPortService
    {

        /// <summary>
        /// 已启动
        /// </summary>
        private bool isStarted = false;
        /// <summary>
        /// 串口
        /// </summary>
        private SerialPort serialPort;
        /// <summary>
        /// 接收消息回调函数&lt;消息>
        /// </summary>
        private Action<byte[]> receiveCallback;

        /// <summary>
        /// 启动
        /// </summary>
        /// <param name="portName">端口名</param>
        /// <param name="baudRate">波特率</param>
        /// <param name="reconnectTime">重连时间(秒，&lt;=0不重连)</param>
        /// <param name="receiveCallback">接收消息回调函数&lt;消息></param>
        public void Start(string portName, int baudRate, int reconnectTime, Action<byte[]> receiveCallback)
        {
            serialPort = new SerialPort(portName, baudRate);
            try
            {
                // 打开串口
                serialPort.Open();
            }
            catch
            {
            }
            this.receiveCallback = receiveCallback;
            // 接收消息
            serialPort.DataReceived += Receive;
            isStarted = true;
            // 重连
            if (reconnectTime > 0)
            {
                Reconnect(reconnectTime);
            }
        }

        /// <summary>
        /// 重连
        /// </summary>
        /// <param name="reconnectTime">重连时间(秒)</param>
        private async void Reconnect(int reconnectTime)
        {
            while (isStarted)
            {
                if (!serialPort.IsOpen)
                {
                    try
                    {
                        // 打开串口
                        serialPort.Open();
                    }
                    catch
                    {
                    }
                }
                // 延时
                await Task.Delay(reconnectTime * 1000);
            }
        }

        /// <summary>
        /// 关闭
        /// </summary>
        public void Close()
        {
            isStarted = false;
            if (serialPort != null)
            {
                serialPort.DataReceived -= Receive;
                serialPort.Close();
            }
        }

        /// <summary>
        /// 已开启
        /// </summary>
        /// <returns>已开启</returns>
        public bool IsOpen()
        {
            return serialPort.IsOpen;
        }

        /// <summary>
        /// 接收消息
        /// </summary>
        /// <param name="sender">SerialPort</param>
        /// <param name="e">SerialDataReceivedEventArgs</param>
        private void Receive(object sender, SerialDataReceivedEventArgs e)
        {
            int length = serialPort.BytesToRead;
            if (length > 0)
            {
                byte[] buffer = new byte[length];
                serialPort.Read(buffer, 0, length);
                receiveCallback(buffer);
            }
        }

        /// <summary>
        /// 发送消息
        /// </summary>
        /// <param name="data">消息</param>
        /// <returns>是否发送成功</returns>
        public bool Send(byte[] data)
        {
            if (serialPort.IsOpen)
            {
                try
                {
                    // 发送消息
                    serialPort.Write(data, 0, data.Length);
                    return true;
                }
                catch
                {
                }
            }
            return false;
        }

    }
}
