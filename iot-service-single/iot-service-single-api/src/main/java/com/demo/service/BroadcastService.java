package com.demo.service;

import cn.z.clock.Clock;
import cn.z.mqtt.MqttTemp;
import cn.z.websocket.WebSocketTemp;
import com.alibaba.fastjson2.JSON;
import com.demo.base.ServiceBase;
import com.demo.constant.MqttConstant;
import com.demo.constant.WsConstant;
import com.demo.dao.mongo.BroadcastDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.broadcast.Broadcast5000000;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * <h1>广播</h1>
 *
 * <p>
 * createDate 2024/11/18 16:13:05
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class BroadcastService extends ServiceBase {

    private final BroadcastDao broadcastDao;
    private final MqttTemp mqttTemp;
    private final WebSocketTemp webSocketTemp;

    /**
     * 校时广播(每隔12小时的10分20秒)
     *
     * @see Broadcast5000000
     */
    @Async
    @Scheduled(cron = "3 * * * * *")
    // @Scheduled(cron = "20 10 0/12 * * *")
    public void automaticTiming() {
        ProtocolVo protocol = new ProtocolVo();
        protocol.setCommandCode(5000000);
        Broadcast5000000.Broadcast broadcast = new Broadcast5000000.Broadcast();
        broadcast.setTimestamp(Clock.now());
        protocol.setBroadcast(broadcast);
        ProtocolVo result = insertAndSend(protocol);
        if (result != null) {
            log.info("校时广播发送成功:[{}]", result);
        } else {
            log.warn("校时广播发送失败:[{}]", protocol);
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
        ProtocolVo result = broadcastDao.insert(protocol);
        if (result != null) {
            // 发送到嵌入式
            Frame.Broadcast broadcast = new Frame.Broadcast();
            broadcast.setBroadcast(protocol.getBroadcast());
            String topic = MqttConstant.getBroadcastTopic(protocol.getCommandCode());
            mqttTemp.send(topic, JSON.toJSONBytes(broadcast));
            log.info("发送广播主题:[{}] ,消息:[{}]", topic, broadcast);
            // WebSocket广播模式
            webSocketTemp.send(WsConstant.getBroadcastTopic(protocol.getCommandCode()), result.toWebString());
        }
        return result;
    }

}
