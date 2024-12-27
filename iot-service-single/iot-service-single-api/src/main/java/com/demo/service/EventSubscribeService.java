package com.demo.service;

import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSONObject;
import com.demo.base.ServiceBase;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.EventDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <h1>事件订阅</h1>
 *
 * <p>
 * createDate 2024/04/11 17:41:53
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class EventSubscribeService extends ServiceBase {

    private final EventDao eventDao;
    private final WebSocketTemp webSocketTemp;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.EVENT + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(value = HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String msg,
            @Header(HeaderEnum.MSG) Frame frame
    ) {
        log.info("收到事件主题:[{}] ,消息:[{}]", topic, msg);
        // 事件JSON处理
        eventJsonHandle(gatewaySn, deviceSn, commandCode, frame.getEvent());
    }

    /**
     * 事件JSON处理
     */
    private void eventJsonHandle(int gatewaySn, int deviceSn, int commandCode, JSONObject event) {
        // 事件转换
        Map.Entry<Integer, Protocol.Data> eventMap = Protocol.eventConvert(commandCode, event);
        // 协议格式错误或转换失败或数据检查未通过
        if (eventMap == null || (eventMap.getValue() != null && eventMap.getValue().dataCheckNotPass())) {
            log.warn("协议格式错误或转换失败或数据检查未通过 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, event);
            return;
        }
        // 网关序号、设备序号、设备类型不匹配
        // if (GatewayService.notValid(gatewaySn, deviceSn, eventMap.getKey())) {
        //     log.warn("网关序号、设备序号、设备类型不匹配 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, event);
        //     return;
        // }
        ProtocolVo protocol = new ProtocolVo();
        protocol.setGatewaySn(gatewaySn);
        protocol.setDeviceSn(deviceSn);
        protocol.setCommandCode(commandCode);
        protocol.setEvent(eventMap.getValue());
        // 插入
        ProtocolVo result = eventDao.insert(protocol);
        if (result != null) {
            log.info("事件接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.getEventTopic(gatewaySn, deviceSn, commandCode), result.toWebString());
            // 插入消息
            EventService.addMsg(gatewaySn, deviceSn, commandCode, result);
        } else {
            log.warn("事件接收失败:[{}]", protocol);
        }
    }

}
