package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>获取温度计参数</h1>
 *
 * <p>
 * createDate 2023/11/15 10:07:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取温度计参数")
@ProtocolClass(code = 40100, name = "获取温度计参数", type = ProtocolType.INTERACT, deviceType = DeviceType.THERMOMETER, responseClass = Interact40100.Response.class)
public class Interact40100 {

    private Interact40100() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应")
    public static class Response extends Interact30100.Request implements Protocol.Data {
    }

}
