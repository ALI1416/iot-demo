package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>获取通信地址</h1>
 *
 * <p>
 * createDate 2023/11/10 17:49:15
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取通信地址")
@ProtocolClass(code = 4001, name = "获取通信地址", type = ProtocolType.INTERACT, deviceType = DeviceType.GATEWAY, responseClass = Interact4001.Response.class)
public class Interact4001 {

    private Interact4001() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应")
    public static class Response extends Interact3001.Request implements Protocol.Data {
    }

}
