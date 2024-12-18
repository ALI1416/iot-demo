package com.demo.service;

import cn.z.mqtt.MqttTemp;
import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.tool.ThreadPool;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.demo.base.ServiceBase;
import com.demo.constant.ErrorCode;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.CommunicationDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <h1>交流订阅</h1>
 *
 * <p>
 * createDate 2024/11/19 10:32:55
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class CommunicationSubscribeService extends ServiceBase {

    private final CommunicationDao communicationDao;
    private final MqttTemp mqttTemp;
    private final WebSocketTemp webSocketTemp;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.READ + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(value = HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String msg,
            @Header(HeaderEnum.MSG) Frame frame
    ) {
        log.info("主题:[{}] ,消息:[{}]", topic, msg);
        // 读取JSON处理
        readJsonHandle(gatewaySn, deviceSn, commandCode, frame.getRead());
    }

    /**
     * 读取JSON处理
     */
    private void readJsonHandle(int gatewaySn, int deviceSn, int commandCode, JSONObject read) {
        ProtocolVo protocol = new ProtocolVo();
        protocol.setGatewaySn(gatewaySn);
        protocol.setDeviceSn(deviceSn);
        protocol.setCommandCode(commandCode);
        // 读取转换
        Map.Entry<Integer, Protocol.Data> readMap = Protocol.readConvert(commandCode, read);
        // 协议格式错误或转换失败
        if (readMap == null) {
            log.warn("协议格式错误或转换失败 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, read);
            protocol.setErrorCode(ErrorCode.MQTT_MESSAGE_ERROR.getCode());
        } else {
            // 数据检查未通过
            Protocol.Data readData = readMap.getValue();
            if (readData != null && readData.dataCheckNotPass()) {
                log.warn("数据检查未通过 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, read);
                protocol.setErrorCode(ErrorCode.MQTT_MESSAGE_ERROR.getCode());
            } else {
                protocol.setErrorCode(ErrorCode.NO_ERROR.getCode());
                protocol.setRead(readData);
                protocol.setWrite(Protocol.getCommunicationWrite(commandCode, readData));
            }
        }
        ProtocolVo result = insertAndSend(protocol);
        if (result != null) {
            log.info("交流成功:[{}]", result);
        } else {
            log.warn("交流失败:[{}]", protocol);
        }
    }

    /**
     * 插入并发送
     *
     * @param protocol ProtocolVo
     * @return ok:T,e:null
     */
    public ProtocolVo insertAndSend(ProtocolVo protocol) {
        // 插入
        ProtocolVo result = communicationDao.insert(protocol);
        if (result != null) {
            // 发送到嵌入式
            Frame.Write write = new Frame.Write();
            write.setErrorCode(protocol.getErrorCode());
            write.setWrite(protocol.getWrite());
            // 不能在同一个线程内使用
            ThreadPool.execute(() -> mqttTemp.send(
                    MqttConstant.getWriteTopic(protocol.getGatewaySn(), protocol.getDeviceSn(), protocol.getCommandCode()),
                    JSON.toJSONBytes(write)
            ));
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.getCommunicationTopic(protocol.getGatewaySn(), protocol.getDeviceSn(), protocol.getCommandCode()), result.toWebString());
        }
        return result;
    }

}
