package com.demo.entity.protocol;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.demo.base.ToStringBase;
import com.demo.constant.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <h1>协议帧</h1>
 *
 * <p>
 * createDate 2024/04/09 14:51:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "协议帧")
public class Frame extends ToStringBase {

    /**
     * 请求序号
     */
    @Schema(description = "请求序号")
    private Long requestSn;
    /**
     * 错误代码
     *
     * @see ErrorCode
     */
    @Schema(description = "错误代码")
    private Integer errorCode;
    /**
     * 事件
     */
    @Schema(description = "事件")
    private JSONObject event;
    /**
     * 故障
     */
    @Schema(description = "故障")
    private JSONArray fault;
    /**
     * 请求
     */
    @Schema(description = "请求")
    private JSONObject request;
    /**
     * 响应
     */
    @Schema(description = "响应")
    private JSONObject response;
    /**
     * 广播
     */
    @Schema(description = "广播")
    private JSONObject broadcast;
    /**
     * 读取
     */
    @Schema(description = "读取")
    private JSONObject read;
    /**
     * 写入
     */
    @Schema(description = "写入")
    private JSONObject write;

    public Frame() {
    }

    public Frame(Frame frame) {
        if (frame != null) {
            this.requestSn = frame.requestSn;
            this.errorCode = frame.errorCode;
            this.event = frame.event;
            this.fault = frame.fault;
            this.request = frame.request;
            this.response = frame.response;
            this.broadcast = frame.broadcast;
            this.read = frame.read;
            this.write = frame.write;
        }
    }

    public Frame(byte[] bytes) {
        this(JSON.parseObject(bytes, Frame.class));
    }

    /**
     * 事件
     */
    @Getter
    @Setter
    @Schema(description = "事件", name = "Frame.Event")
    public static class Event extends ToStringBase {

        /**
         * 事件
         */
        @Schema(description = "事件")
        private Protocol.Data event;

    }

    /**
     * 故障
     */
    @Getter
    @Setter
    @Schema(description = "故障", name = "Frame.Fault")
    public static class Fault extends ToStringBase {

        /**
         * 故障
         */
        @Schema(description = "故障")
        private List<Integer> fault;

    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求", name = "Frame.Request")
    public static class Request extends ToStringBase {

        /**
         * 请求序号
         */
        @Schema(description = "请求序号")
        private Long requestSn;
        /**
         * 请求
         */
        @Schema(description = "请求")
        private Protocol.Data request;

    }

    /**
     * 响应
     */
    @Getter
    @Setter
    @Schema(description = "响应", name = "Frame.Response")
    public static class Response extends ToStringBase {

        /**
         * 请求序号
         */
        @Schema(description = "请求序号")
        private Long requestSn;
        /**
         * 错误代码
         *
         * @see ErrorCode
         */
        @Schema(description = "错误代码")
        private Integer errorCode;
        /**
         * 响应
         */
        @Schema(description = "响应")
        private Protocol.Data response;

    }

    /**
     * 广播
     */
    @Getter
    @Setter
    @Schema(description = "广播", name = "Frame.Broadcast")
    public static class Broadcast extends ToStringBase {

        /**
         * 广播
         */
        @Schema(description = "广播")
        private Protocol.Data broadcast;

    }

    /**
     * 读取
     */
    @Getter
    @Setter
    @Schema(description = "读取", name = "Frame.Read")
    public static class Read extends ToStringBase {

        /**
         * 读取
         */
        @Schema(description = "读取")
        private Protocol.Data read;

    }

    /**
     * 写入
     */
    @Getter
    @Setter
    @Schema(description = "写入", name = "Frame.Write")
    public static class Write extends ToStringBase {

        /**
         * 错误代码
         *
         * @see ErrorCode
         */
        @Schema(description = "错误代码")
        private Integer errorCode;
        /**
         * 写入
         */
        @Schema(description = "写入")
        private Protocol.Data write;

    }

}
