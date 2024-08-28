package com.demo.announce;

import com.demo.entity.protocol.Protocol;

import java.lang.annotation.*;

/**
 * <h1>协议注解</h1>
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
public @interface ProtocolAnno {

    /**
     * 命令代码
     */
    int code();

    /**
     * 命令名
     */
    String name();

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
    Event event() default @Event(
            event = Protocol.DefaultData.class,
            reportMinute = Protocol.DefaultData.class,
            reportHour = Protocol.DefaultData.class,
            reportDay = Protocol.DefaultData.class,
            reportMonth = Protocol.DefaultData.class,
            reportHandle = Protocol.DefaultReportHandle.class
    );

    /**
     * 故障
     */
    Fault fault() default @Fault(fault = Protocol.DefaultFault.class);

    /**
     * 交互
     */
    Interact interact() default @Interact();

    /**
     * 事件
     */
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Event {

        /**
         * 全部数据类
         */
        Class<? extends Protocol.Data> event();

        /**
         * 分钟报表类
         */
        Class<? extends Protocol.Data> reportMinute();

        /**
         * 小时报表类
         */
        Class<? extends Protocol.Data> reportHour();

        /**
         * 日报表类
         */
        Class<? extends Protocol.Data> reportDay();

        /**
         * 月报表类
         */
        Class<? extends Protocol.Data> reportMonth();

        /**
         * 报表处理类
         */
        Class<? extends Protocol.ReportHandle> reportHandle();

    }

    /**
     * 故障
     */
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Fault {

        /**
         * 故障类
         */
        Class<? extends Protocol.Fault> fault();

    }

    /**
     * 交互
     */
    @Target({ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface Interact {

        /**
         * 请求类
         */
        Class<? extends Protocol.Data> request() default Protocol.DefaultData.class;

        /**
         * 响应类
         */
        Class<? extends Protocol.Data> response() default Protocol.DefaultData.class;

    }

}
