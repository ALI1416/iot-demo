#include <REGX52.H>
#include "UART.H"
#include "TIMER0.H"
#include "DS18B20.H"
#include "OneWire.H"
#include "DELAY.H"
#include "AT24C02.h"

// 串口接收数据数组长度
#define UART_RECEIVE_SIZE 8
// 串口接收数据数组
unsigned char UartReceiveData[UART_RECEIVE_SIZE];
// 串口下一个接收数据存放下标
unsigned char UartReceiveIndex = 0;
// 串口接收状态
// 0 未接收
// 1 接收中
// 2 被定时器中断函数打断
// 3 接收完成(数据要在UART_RECEIVE_SIZE毫秒内发送完成，否则无效)
unsigned char UartReceiveStatus = 0;

// 温度转换最大值(秒)
unsigned char TEMP_CONVERT_MAX = 60;
// 温度转换最小值(秒)
unsigned char TEMP_CONVERT_MIN = 1;
// 温度转换间隔(秒)
unsigned char TempConvertTime = 1;
// 温度转换状态
// 0 不可转换
// 1 可转换
unsigned char TempConvertStatus = 1;

// 串口发送最大值(秒)
unsigned char UART_SEND_MAX = 60;
// 串口发送最小值(秒)
unsigned char UART_SEND_MIN = 1;
// 串口发送间隔(秒)
unsigned char UartSendTime = 10;
// 串口发送状态
// 0 不可发送
// 1 可发送
unsigned char UartSendStatus = 0;

// 温度最大值(℃)(实际为125)
char TEMP_MAX = 99;
// 温度最小值(℃)(实际为-55)
char TEMP_MIN = -55;
// 温度*10000
long Temp;
// 温度整数部分(char)(Temp / 10000)
char TempInt;

// 高温警报(℃)
char TempAlertHigh = 30;
// 低温警报(℃)
char TempAlertLow = 20;
// 温度警报状态
// 0 无警报
// bit0(0x01) 温度过高
// bit1(0x02) 温度过低
unsigned char TempAlertStatus = 0;

void main()
{
  // 存储器里面的温度转换间隔
  char StorageConvert;
  // 存储器里面的串口发送间隔
  char StorageSend;
  // 存储器里面的高温警报
  char StorageHigh;
  // 存储器里面的低温警报
  char StorageLow;
  Timer0_Init();
  UartInit();
  // 读取存储器里面的温度设置
  StorageConvert = AT24C02_ReadByte(0);
  StorageSend = AT24C02_ReadByte(1);
  StorageHigh = AT24C02_ReadByte(2);
  StorageLow = AT24C02_ReadByte(3);
  // 存储器里的数据正确，才保存
  if (
      // 温度转换间隔>=温度转换最小值 <=温度转换最大值
      StorageConvert >= TEMP_CONVERT_MIN && StorageConvert <= TEMP_CONVERT_MAX
      // 串口发送间隔>=串口发送最小值 <=串口发送最大值
      && StorageSend >= UART_SEND_MIN && StorageSend <= UART_SEND_MAX
      // 串口发送间隔>=温度转换间隔
      && StorageSend >= StorageConvert
      // 高温警报<=温度最大值 低温警报>=温度最小值
      && StorageHigh <= TEMP_MAX && StorageLow >= TEMP_MIN
      // 高温警报>低温警报
      && StorageHigh > StorageLow)
  {
    TempAlertHigh = StorageHigh;
    TempAlertLow = StorageLow;
    TempConvertTime = StorageConvert;
    UartSendTime = StorageSend;
  }
  // 上电先转换一次温度，防止第一次读数据错误
  DS18B20_ConvertT();
  // 等待转换完成
  delayMs(500);
  while (1)
  {
    // 数据交互格式
    // 事件(单片机 --> 网关):0x00 ~ 0x3F
    //   温度计事件:0x00
    //     温度x10000(32bit)
    // 故障(单片机 --> 网关):0x40 ~ 0x7F
    //   温度计故障:0x40
    //     温度警报状态(8bit)
    // 交互(网关 --> 单片机):请求:0x80 ~ 0xBF 响应:0xC0 ~ 0xFF
    //   设置温度计配置
    //     请求:0x80
    //       T温度转换间隔 U串口发送间隔 H高温警报 L低温警报(32bit)
    //     响应:0xC0
    //       是否成功(8bit)
    //         0x00 成功
    //         0x01 失败
    //   获取温度计配置
    //     请求:0x81
    //     响应:0xC1
    //       T温度转换间隔 U串口发送间隔 H高温警报 L低温警报(32bit)
    /* 数据接收完成 */
    if (UartReceiveStatus == 3)
    {
      // 数据头
      unsigned char header = UartReceiveData[0];
      // 设置温度计配置请求:0x80
      if (header == 0x80)
      {
        // T温度转换间隔
        StorageConvert = UartReceiveData[1];
        // U串口发送间隔
        StorageSend = UartReceiveData[2];
        // H高温警报
        StorageHigh = UartReceiveData[3];
        // L低温警报
        StorageLow = UartReceiveData[4];
        // 数据正确
        if (
            // 温度转换间隔>=温度转换最小值 <=温度转换最大值
            StorageConvert >= TEMP_CONVERT_MIN && StorageConvert <= TEMP_CONVERT_MAX
            // 串口发送间隔>=串口发送最小值 <=串口发送最大值
            && StorageSend >= UART_SEND_MIN && StorageSend <= UART_SEND_MAX
            // 串口发送间隔>=温度转换间隔
            && StorageSend >= StorageConvert
            // 高温警报<=温度最大值 低温警报>=温度最小值
            && StorageHigh <= TEMP_MAX && StorageLow >= TEMP_MIN
            // 高温警报>低温警报
            && StorageHigh > StorageLow)
        {
          TempAlertHigh = StorageHigh;
          TempAlertLow = StorageLow;
          UartSendTime = StorageSend;
          TempConvertTime = StorageConvert;
          // 写入数据到存储器
          AT24C02_WriteByte(0, TempConvertTime);
          delayMs(5);
          AT24C02_WriteByte(1, UartSendTime);
          delayMs(5);
          AT24C02_WriteByte(2, TempAlertHigh);
          delayMs(5);
          AT24C02_WriteByte(3, TempAlertLow);
          delayMs(5);
          // 设置温度计配置响应:0xC0
          UartSendByte(0xC0);
          // 0x00 成功
          UartSendByte(0x00);
        }
        // 数据错误
        else
        {
          // 设置温度计配置响应:0xC0
          UartSendByte(0xC0);
          // 0x01 失败
          UartSendByte(0x01);
        }
      }
      // 获取温度计配置请求:0x81
      if (header == 0x81)
      {
        // 获取温度计配置响应:0xC1
        UartSendByte(0xC1);
        // 温度转换间隔
        UartSendByte(TempConvertTime);
        // 串口发送间隔
        UartSendByte(UartSendTime);
        // 高温警报
        UartSendByte(TempAlertHigh);
        // 低温警报
        UartSendByte(TempAlertLow);
      }
      // 重置状态和下标
      UartReceiveIndex = 0;
      UartReceiveStatus = 0;
    }
    /* 温度转换时间到 */
    if (TempConvertStatus == 1)
    {
      // 温度转换
      DS18B20_ConvertT();
      Temp = DS18B20_ReadT();
      TempInt = (char)(Temp / 10000);
      // 高温还是低温
      if (TempInt >= TempAlertHigh)
      {
        // 温度过高
        TempAlertStatus = 0x01;
      }
      else if (TempInt < TempAlertLow)
      {
        // 温度过低
        TempAlertStatus = 0x02;
      }
      else
      {
        // 正常
        TempAlertStatus = 0;
      }
      TempConvertStatus = 0;
    }
    /* 串口发送时间到 */
    if (UartSendStatus == 1)
    {
      unsigned char t0 = Temp >> 24;
      unsigned char t1 = (Temp & 0x00FF0000) >> 16;
      unsigned char t2 = (Temp & 0x0000FF00) >> 8;
      unsigned char t3 = Temp & 0x000000FF;
      // 温度计事件:0x00
      UartSendByte(0x00);
      // 温度x10000
      UartSendByte(t0);
      UartSendByte(t1);
      UartSendByte(t2);
      UartSendByte(t3);
      if (TempAlertStatus != 0)
      {
        delayMs(10);
        // 温度计故障:0x40
        UartSendByte(0x40);
        // 温度警报状态
        UartSendByte(TempAlertStatus);
      }
      UartSendStatus = 0;
    }
  }
}

/**
 * 串口中断
 */
void UART_Routine() interrupt 4
{
  /* 接收中断 */
  if (RI == 1)
  {
    // 放满了，重头开始
    if (UartReceiveIndex == UART_RECEIVE_SIZE)
    {
      // 重置状态和下标
      UartReceiveIndex = 0;
      UartReceiveStatus = 0;
    }
    // 放入数据
    UartReceiveData[UartReceiveIndex++] = SBUF;
    // 接收中
    UartReceiveStatus = 1;
    // 重置
    RI = 0;
  }
}

/**
 * 定时器0中断回调函数(每1ms)
 */
void Timer0_Routine() interrupt 1
{
  // 静态，防止被销毁
  static unsigned int UART_Count,          //
      Temp_Convert_Count, UART_Send_Count; //
  // 复位
  TL0 = 0x66;
  TH0 = 0xFC;
  /* 串口接收 每隔UART_RECEIVE_SIZE毫秒 */
  if ((++UART_Count) >= UART_RECEIVE_SIZE)
  {
    UART_Count = 0;
    if (UartReceiveStatus == 1)
    {
      // 接收中，置为打断
      UartReceiveStatus = 2;
    }
    else if (UartReceiveStatus == 2)
    {
      // 打断，置为接收完成
      UartReceiveStatus = 3;
    }
  }
  /* 温度转换 每隔UartSendTime秒 */
  if ((++Temp_Convert_Count) >= 1000 * TempConvertTime)
  {
    Temp_Convert_Count = 0;
    TempConvertStatus = 1;
  }
  /* 温度串口发送 每隔UartSendTime秒 */
  if ((++UART_Send_Count) >= 1000 * UartSendTime)
  {
    UART_Send_Count = 0;
    UartSendStatus = 1;
  }
}
