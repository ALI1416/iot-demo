package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolField;
import com.demo.announce.ProtocolType;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设置网关通信地址</h1>
 *
 * <p>
 * createDate 2023/11/10 17:49:15
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "设置网关通信地址")
@ProtocolClass(code = 30000, name = "设置网关通信地址", type = ProtocolType.INTERACT, deviceType = DeviceType.GATEWAY, requestClass = Interact30000.Request.class)
public class Interact30000 {

    private Interact30000() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求")
    public static class Request extends ToStringBase implements Protocol.Data {

        /**
         * URI
         */
        @Schema(description = "URI")
        @ProtocolField(name = "URI")
        private String uri;
        /**
         * 用户名
         */
        @Schema(description = "用户名")
        @ProtocolField(name = "用户名")
        private String username;
        /**
         * 密码
         */
        @Schema(description = "密码")
        @ProtocolField(name = "密码")
        private String password;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(uri, username, password);
        }

    }

}
