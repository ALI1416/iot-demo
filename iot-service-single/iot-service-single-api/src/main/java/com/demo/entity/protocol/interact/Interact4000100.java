package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolAnno;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>获取温度计配置</h1>
 *
 * <p>
 * createDate 2023/11/15 10:07:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取温度计配置")
@ProtocolAnno(code = 4000100, name = "获取温度计配置", deviceType = DeviceType.THERMOMETER,
        interact = @ProtocolAnno.Interact(response = Interact4000100.Response.class)
)
public class Interact4000100 {

    private Interact4000100() {
    }

    /**
     * 响应
     */
    @Schema(description = "响应", name = "Interact4000100.Response")
    public static class Response extends Interact3000100.Request implements Protocol.Data {
    }

}
