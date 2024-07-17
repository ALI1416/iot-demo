package com.demo.announce;

import java.lang.annotation.*;

/**
 * <h1>协议</h1>
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
public @interface Protocol {

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
     * 备注
     */
    String comment() default "";

    /**
     * 事件
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> event() default com.demo.entity.protocol.Protocol.NoData.class;

    /**
     * 故障
     */
    FaultEnum fault() default FaultEnum.NULL;

    /**
     * 请求
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> request() default com.demo.entity.protocol.Protocol.NoData.class;

    /**
     * 响应
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> response() default com.demo.entity.protocol.Protocol.NoData.class;

}
