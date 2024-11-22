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
@ProtocolAnno(code = 4000000, name = "获取通信地址", deviceType = DeviceType.GATEWAY,
        interact = @ProtocolAnno.Interact(response = Interact4000000.Response.class)
)
public class Interact4000000 {

    private Interact4000000() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应", name = "Interact4000000.Response")
    public static class Response extends Interact3000000.Request implements Protocol.Data {
    }

}
