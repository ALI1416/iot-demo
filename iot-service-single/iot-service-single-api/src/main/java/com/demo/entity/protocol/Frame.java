package com.demo.entity.protocol;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
@NoArgsConstructor
public class Frame extends ToStringBase {

    /**
     * 时间戳
     */
    // @Schema(description = "时间戳")
    // private Timestamp timestamp;
    /**
     * 请求序号
     */
    @Schema(description = "请求序号")
    private Long requestSn;
    /**
     * 错误代码
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
     * 响应
     */
    @Schema(description = "响应")
    private JSONObject response;

    public Frame(Frame frame) {
        if (frame != null) {
            // this.timestamp = frame.timestamp;
            this.requestSn = frame.requestSn;
            this.errorCode = frame.errorCode;
            this.event = frame.event;
            this.fault = frame.fault;
            this.response = frame.response;
        }
    }

    public Frame(byte[] bytes) {
        this(JSON.parseObject(bytes, Frame.class));
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

}
