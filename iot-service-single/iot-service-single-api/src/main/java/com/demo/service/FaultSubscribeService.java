package com.demo.service;

import cn.z.mqtt.annotation.Header;
import cn.z.mqtt.annotation.HeaderEnum;
import cn.z.mqtt.annotation.Subscribe;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSONArray;
import com.demo.base.ServiceBase;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.FaultDao;
import com.demo.dao.mongo.FaultDetailDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * <h1>故障订阅</h1>
 *
 * <p>
 * createDate 2024/04/11 17:42:06
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class FaultSubscribeService extends ServiceBase {

    private final FaultDao faultDao;
    private final FaultDetailDao faultDetailDao;
    private final WebSocketTemp webSocketTemp;
    private final FaultDetailService faultDetailService;

    /**
     * MQTT订阅
     */
    @Subscribe(MqttConstant.FAULT + "+/+/+")
    public void subscribe(
            @Header(HeaderEnum.TOPIC) String topic,
            @Header(value = HeaderEnum.TOPIC_PART) int gatewaySn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 1) int deviceSn,
            @Header(value = HeaderEnum.TOPIC_PART, index = 2) int commandCode,
            @Header(HeaderEnum.MSG) String msg,
            @Header(HeaderEnum.MSG) Frame frame
    ) {
        log.info("收到故障主题:[{}] ,消息:[{}]", topic, msg);
        // 故障数组处理
        faultArrayHandle(gatewaySn, deviceSn, commandCode, frame.getFault());
    }

    /**
     * 故障数组处理
     */
    private void faultArrayHandle(int gatewaySn, int deviceSn, int commandCode, JSONArray fault) {
        // 协议格式错误或转换失败
        Map.Entry<Integer, List<Integer>> faultListMap = Protocol.faultConvert(commandCode, fault);
        if (faultListMap == null) {
            log.warn("协议格式错误或转换失败 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, fault);
            return;
        }
        // 网关序号、设备序号、设备类型不匹配
        // if (GatewayService.notValid(gatewaySn, deviceSn, faultListMap.getKey())) {
        //     log.warn("网关序号、设备序号、设备类型不匹配 网关序号:[{}] ,设备序号:[{}] ,命令代码:[{}] ,消息:[{}]", gatewaySn, deviceSn, commandCode, fault);
        //     return;
        // }
        ProtocolVo protocol = new ProtocolVo();
        protocol.setGatewaySn(gatewaySn);
        protocol.setDeviceSn(deviceSn);
        protocol.setCommandCode(commandCode);
        protocol.setFaultList(faultListMap.getValue());
        protocol.setFaultDetailList(Protocol.faultDetailConvert(faultListMap.getValue()));
        // 插入
        ProtocolVo result = faultDao.insert(protocol);
        if (result != null) {
            log.info("故障接收成功:[{}]", result);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.getFaultTopic(gatewaySn), result.toWebString());
            // 故障详情处理
            faultDetailHandle(result);
        } else {
            log.warn("故障接收失败:[{}]", protocol);
        }
    }

    /**
     * 故障详情处理
     */
    private void faultDetailHandle(ProtocolVo protocol) {
        int gatewaySn = protocol.getGatewaySn();
        int deviceSn = protocol.getDeviceSn();
        int commandCode = protocol.getCommandCode();
        Timestamp createTime = protocol.getCreateTime();
        // 故障详情处理
        ProtocolVo result;
        for (Protocol.FaultDetail faultDetail : protocol.getFaultDetailList()) {
            ProtocolVo lastOne = faultDetailService.findLastOne(gatewaySn, deviceSn, commandCode, faultDetail.getGroupNumber(), faultDetail.getBits(), createTime);
            if (lastOne == null) {
                // 没有历史记录 新增
                ProtocolVo newFaultDetail = new ProtocolVo();
                newFaultDetail.setGatewaySn(gatewaySn);
                newFaultDetail.setDeviceSn(deviceSn);
                newFaultDetail.setCommandCode(commandCode);
                newFaultDetail.setGroupNumber(faultDetail.getGroupNumber());
                newFaultDetail.setBits(faultDetail.getBits());
                result = faultDetailDao.insert(newFaultDetail);
            } else {
                lastOne.setGatewaySn(gatewaySn);
                // 有历史记录 更新
                result = faultDetailDao.save(lastOne);
            }
            if (result != null) {
                log.info("故障详情处理成功:[{}]", result);
            } else {
                log.info("故障详情处理失败:[{}]", protocol);
            }
        }
    }

}
