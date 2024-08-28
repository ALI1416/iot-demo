package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolAnno;
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
@ProtocolAnno(code = 40000, name = "获取通信地址", deviceType = DeviceType.GATEWAY,
        interact = @ProtocolAnno.Interact(response = Interact40000.Response.class)
)
public class Interact40000 {

    private Interact40000() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应", name = "Interact40000.Response")
    public static class Response extends Interact30000.Request implements Protocol.Data {
    }

}
