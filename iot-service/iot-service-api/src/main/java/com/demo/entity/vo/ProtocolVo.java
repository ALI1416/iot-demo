package com.demo.entity.vo;

import com.alibaba.fastjson2.JSONObject;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Transient;

/**
 * <h1>协议</h1>
 *
 * <p>
 * createDate 2023/11/13 17:19:10
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "协议")
public class ProtocolVo extends Protocol {

    /**
     * 请求JSON
     */
    @Transient
    @Schema(description = "请求JSON")
    private JSONObject requestJson;

}
