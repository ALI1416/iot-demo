package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * <h1>@Async配置</h1>
 *
 * <p>
 * createDate 2023/12/29 11:40:12
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Configuration
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 线程池数量
     */
    @Override
    public Executor getAsyncExecutor() {
        return Executors.newScheduledThreadPool(10);
    }

}
