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
    Class<? extends com.demo.entity.protocol.Protocol.Data> event() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 事件分钟报表
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> eventMinute() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 事件小时报表
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> eventHour() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 事件日报表
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> eventDay() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 事件月报表
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> eventMonth() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 事件报表处理
     */
    Class<? extends com.demo.entity.protocol.Protocol.EventReportHandle> eventReportHandle() default com.demo.entity.protocol.Protocol.DefaultEventReportHandle.class;

    /**
     * 故障
     */
    Class<? extends com.demo.entity.protocol.Protocol.Fault> fault() default com.demo.entity.protocol.Protocol.DefaultFault.class;

    /**
     * 请求
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> request() default com.demo.entity.protocol.Protocol.DefaultData.class;

    /**
     * 响应
     */
    Class<? extends com.demo.entity.protocol.Protocol.Data> response() default com.demo.entity.protocol.Protocol.DefaultData.class;

}
