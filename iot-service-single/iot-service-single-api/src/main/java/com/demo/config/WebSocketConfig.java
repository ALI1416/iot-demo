package com.demo.config;

import cn.z.spring.tool.FastJsonMessageConverter;
import com.demo.constant.FormatConstant;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

/**
 * <h1>WebSocket配置</h1>
 *
 * <p>
 * createDate 2021/12/16 10:05:53
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 前缀
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 前缀
                .setAllowedOriginPatterns("*") // 启用跨域
        ;
    }

    /**
     * 映射
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        ThreadPoolTaskScheduler heartbeat = new ThreadPoolTaskScheduler();
        heartbeat.setPoolSize(1);
        heartbeat.setThreadNamePrefix("ws-heartbeat-");
        heartbeat.initialize();
        registry.setApplicationDestinationPrefixes("/app") // 客户端--->服务端
                .setUserDestinationPrefix("/user/") // 客户端<--->客户端
                .enableSimpleBroker("/topic", "/queue") // 服务端--->客户端(广播、队列)
                .setHeartbeatValue(new long[]{10000, 10000}) // 心跳
                .setTaskScheduler(heartbeat) // 心跳定时器
        ;
    }

    /**
     * 消息转换器
     */
    @Override
    public boolean configureMessageConverters(List<MessageConverter> converters) {
        converters.add(new StringMessageConverter());
        converters.add(new ByteArrayMessageConverter());
        converters.add(new FastJsonMessageConverter(FormatConstant.DATE, FormatConstant.JSON_READER_FEATURE, FormatConstant.JSON_WRITER_FEATURE));
        return false;
    }

}
