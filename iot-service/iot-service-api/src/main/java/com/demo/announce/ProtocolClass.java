package com.demo.announce;

import com.demo.entity.protocol.Protocol;

import java.lang.annotation.*;

/**
 * <h1>协议类</h1>
 *
 * <p>
 * createDate 2023/12/13 11:02:45
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProtocolClass {

    /**
     * 命令代码
     */
    int code();

    /**
     * 命令名
     */
    String name();

    /**
     * 协议类型
     */
    ProtocolType type();

    /**
     * 设备类型
     */
    DeviceType deviceType();

    /**
     * 特殊处理
     */
    boolean special() default false;

    /**
     * 事件类
     */
    Class<? extends Protocol.Data> eventClass() default Protocol.NoData.class;

    /**
     * 故障Map
     */
    FaultMap faultMap() default FaultMap.NULL;

    /**
     * 请求类
     */
    Class<? extends Protocol.Data> requestClass() default Protocol.NoData.class;

    /**
     * 响应类
     */
    Class<? extends Protocol.Data> responseClass() default Protocol.NoData.class;

}
