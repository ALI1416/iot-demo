package com.demo.service;

import cn.z.mqtt.MqttTemp;
import com.alibaba.fastjson2.JSON;
import com.demo.base.ServiceBase;
import com.demo.constant.MqttConstant;
import com.demo.dao.mongo.InteractDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    /**
     * 插入并发送
     *
     * @param protocol ProtocolVo
     * @return ok:T,e:null
     */
    public ProtocolVo insertAndSend(ProtocolVo protocol) {
        // 插入
        ProtocolVo result = interactDao.insert(protocol);
        if (result != null) {
            // 发送到嵌入式
            Frame.Request request = new Frame.Request();
            request.setRequestSn(result.getId());
            request.setRequest(protocol.getRequest());
            mqttTemp.send(
                    MqttConstant.getRequestTopic(result.getGatewaySn(), result.getDeviceSn(), result.getCommandCode()),
                    JSON.toJSONBytes(request)
            );
        }
        return result;
    }

}
