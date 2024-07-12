#ifndef __UART_H__
#define __UART_H__

void UartInit();
void UartSendByte(unsigned char byte);
void UartSendString(unsigned char *str);

#endif