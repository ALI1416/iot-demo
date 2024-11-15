package com.demo.service;

import cn.z.id.Id;
import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSONObject;
import com.demo.base.ServiceBase;
import com.demo.constant.InteractType;
import com.demo.constant.MongoConstant;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.InteractDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <h1>交互订阅</h1>
 *
 * <p>
 * createDate 2024/04/11 17:42:19
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class InteractSubscribeService extends ServiceBase {

    private final InteractDao interactDao;
    private final WebSocketTemp webSocketTemp;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.RESPONSE_PREFIX + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(value = HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String msg,
            @Header(HeaderEnum.MSG) Frame frame
    ) {
        log.info("主题:[{}] ,消息:[{}]", topic, msg);
        // 响应JSON处理
        responseJsonHandle(gatewaySn, deviceSn, commandCode, frame.getRequestSn(), frame.getErrorCode(), frame.getResponse());
    }

    /**
     * 响应JSON处理
     */
    private void responseJsonHandle(int gatewaySn, int deviceSn, int commandCode, Long requestSn, Integer errorCode, JSONObject response) {
        if (requestSn == null || errorCode == null) {
            log.warn("协议帧错误 请求序号:[{}] ,错误代码:[{}] ,网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", requestSn, errorCode, gatewaySn, deviceSn, commandCode, response);
            return;
        }
        // 响应转换
        Map.Entry<Integer, Protocol.Data> responseMap = Protocol.responseConvert(commandCode, response);
        // 协议格式错误或转换失败
        if (responseMap == null) {
            log.warn("协议格式错误或转换失败 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,请求序号:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, requestSn, errorCode, response);
            return;
        }
        // 数据检查未通过
        Protocol.Data responseData = responseMap.getValue();
        if (responseData != null && responseData.dataCheckNotPass()) {
            log.warn("数据检查未通过 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,请求序号:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, requestSn, errorCode, response);
            return;
        }
        // 单向交互
        if (requestSn == 0) {
            ProtocolVo protocol = new ProtocolVo();
            protocol.setGatewaySn(gatewaySn);
            protocol.setDeviceSn(deviceSn);
            protocol.setCommandCode(commandCode);
            protocol.setType(InteractType.SINGLE.getCode());
            protocol.setErrorCode(errorCode);
            protocol.setResponse(responseData);
            ProtocolVo result = interactDao.insert2(protocol);
            if (result != null) {
                log.info("单向交互响应接收成功:[{}]", result);
                // WebSocket广播模式
                webSocketTemp.send(WsConstant.getInteractTopic(gatewaySn, deviceSn, commandCode), result.toWebString());
            } else {
                log.warn("单向交互响应接收失败:[{}]", protocol);
            }
            return;
        }
        // 双向交互
        ProtocolVo protocol = interactDao.findById(requestSn, MongoConstant.getInteractCollectionName(gatewaySn, Id.timestamp(requestSn)));
        if (protocol == null) {
            log.warn("网关序号:[{}]不存在请求序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, requestSn, deviceSn, commandCode, errorCode, response);
            return;
        }
        if (protocol.getErrorCode() != null) {
            log.warn("消息重复 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,请求序号:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, requestSn, errorCode, response);
            return;
        }
        if (protocol.getDeviceSn() != deviceSn) {
            log.warn("设备序号[{}]与原数据[{}]不符 ,网关序号:[{}] ,命令代码:[{}] ,请求序号:[{}] ,错误代码:[{}] ,消息:[{}]", deviceSn, protocol.getDeviceSn(), gatewaySn, commandCode, requestSn, errorCode, response);
            return;
        }
        if (protocol.getCommandCode() != commandCode) {
            log.warn("命令代码[{}]与原数据[{}]不符 ,网关序号:[{}] ,设备序号:[{}] ,请求序号:[{}] ,错误代码:[{}] ,消息:[{}]", commandCode, protocol.getCommandCode(), gatewaySn, deviceSn, requestSn, errorCode, response);
            return;
        }
        protocol.setGatewaySn(gatewaySn);
        protocol.setErrorCode(errorCode);
        protocol.setResponse(responseData);
        // 更新
        ProtocolVo result = interactDao.save(protocol);
        if (result != null) {
            log.info("双向交互响应接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.getInteractTopic(gatewaySn, deviceSn, commandCode), result.toWebString());
        } else {
            log.info("双向交互响应接收失败:[{}]", protocol);
        }
    }

}
