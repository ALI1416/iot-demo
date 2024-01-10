package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>获取网关通信地址</h1>
 *
 * <p>
 * createDate 2023/11/10 17:49:15
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取网关通信地址")
@ProtocolClass(code = 40000, name = "获取网关通信地址", type = ProtocolType.INTERACT, deviceType = DeviceType.GATEWAY, responseClass = Interact40000.Response.class)
public class Interact40000 {

    private Interact40000() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应")
    public static class Response extends Interact30000.Request implements Protocol.Data {
    }

}
