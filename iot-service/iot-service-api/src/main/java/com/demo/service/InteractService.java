package com.demo.service;

import cn.z.id.Id;
import cn.z.mqtt.MqttTemp;
import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSON;
import com.demo.base.ServiceBase;
import com.demo.constant.FormatConstant;
import com.demo.constant.MongoConstant;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.InteractDao;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <h1>互动</h1>
 *
 * <p>
 * createDate 2023/11/14 14:59:06
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class InteractService extends ServiceBase {

    private final InteractDao interactDao;
    private final MqttTemp mqttTemp;
    private final WebSocketTemp webSocketTemp;

    /**
     * 转JSON byte[]
     *
     * @param object Object(为null返回byte[0])
     * @return byte[]
     */
    private static byte[] toJsonBytes(Object object) {
        if (object == null) {
            return new byte[0];
        } else {
            return JSON.toJSONBytes(object, FormatConstant.DATE);
        }
    }

    /**
     * 插入并发送
     *
     * @param base ProtocolVo
     * @return ok:T,e:null
     */
    public ProtocolVo insertAndSend(ProtocolVo base) {
        // 插入
        ProtocolVo result = interactDao.insert(base);
        if (result != null) {
            // 发送到嵌入式
            mqttTemp.send(
                    MqttConstant.REQUEST_PREFIX + result.getGatewaySn() + "/" + result.getDeviceSn() + "/" + result.getCommandCode() + "/" + result.getId(),
                    toJsonBytes(result.getRequest())
            );
        }
        return result;
    }

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.RESPONSE_PREFIX + "+/+/+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(value = HeaderEnum.TOPIC_PART, index = 3) long id,
            @Header(value = HeaderEnum.TOPIC_PART, index = 4) int errorCode,
            @Header(HeaderEnum.MSG) String responseJson
    ) {
        log.info("主题:[{}] ,消息:[{}]", topic, responseJson);
        // 响应JSON处理
        responseJsonHandle(gatewaySn, deviceSn, commandCode, id, errorCode, responseJson);
    }

    /**
     * 响应JSON处理
     */
    private void responseJsonHandle(int gatewaySn, int deviceSn, int commandCode, long id, int errorCode, String responseJson) {
        // 参数修正
        if (responseJson.isEmpty()) {
            responseJson = null;
        }
        // 响应转换
        Map.Entry<Integer, Protocol.Data> responseMap = Protocol.responseConvert(commandCode, responseJson);
        // 协议格式错误或转换失败或数据检查未通过
        if (responseMap == null || (responseMap.getValue() != null && responseMap.getValue().dataCheckNotPass())) {
            log.warn("协议格式错误或转换失败或数据检查未通过 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, responseJson);
            return;
        }
        // 单向互动
        if (id == 0) {
            ProtocolVo base = new ProtocolVo();
            base.setGatewaySn(gatewaySn);
            base.setDeviceSn(deviceSn);
            base.setCommandCode(commandCode);
            base.setErrorCode(errorCode);
            base.setResponse(responseMap.getValue());
            ProtocolVo result = interactDao.insert2(base);
            if (result != null) {
                log.info("单向互动响应接收成功:[{}]", result);
            } else {
                log.warn("单向互动响应接收失败:[{}]", base);
            }
            return;
        }
        // 双向互动
        ProtocolVo base = interactDao.findById(id, gatewaySn + MongoConstant.INTERACT + "_" + DateUtils.getYyyy(Id.timestamp(id)));
        if (base == null) {
            log.warn("网关序号:[{}]不存在id:[{}] ,设备序号:[{}] ,命令代码:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, id, deviceSn, commandCode, errorCode, responseJson);
            return;
        }
        if (base.getErrorCode() != null) {
            log.warn("消息重复 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,id:[{}] ,错误代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, id, errorCode, responseJson);
            return;
        }
        if (base.getDeviceSn() != deviceSn) {
            log.warn("设备序号[{}]与原数据[{}]不符 ,网关序号:[{}] ,命令代码:[{}] ,id:[{}] ,错误代码:[{}] ,消息:[{}]", deviceSn, base.getDeviceSn(), gatewaySn, commandCode, id, errorCode, responseJson);
            return;
        }
        if (base.getCommandCode() != commandCode) {
            log.warn("命令代码[{}]与原数据[{}]不符 ,网关序号:[{}] ,设备序号:[{}] ,id:[{}] ,错误代码:[{}] ,消息:[{}]", commandCode, base.getCommandCode(), gatewaySn, deviceSn, id, errorCode, responseJson);
            return;
        }
        base.setGatewaySn(gatewaySn);
        base.setErrorCode(errorCode);
        base.setResponse(responseMap.getValue());
        // 更新
        ProtocolVo result = interactDao.save(base);
        if (result != null) {
            log.info("双向互动响应接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.INTERACT_PREFIX + gatewaySn + "/" + deviceSn + "/" + commandCode, result.toWebString());
        } else {
            log.info("双向互动响应接收失败:[{}]", base);
        }
    }

}
