package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.Field;
import com.demo.announce.Protocol;
import com.demo.announce.ProtocolType;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设置通信地址</h1>
 *
 * <p>
 * createDate 2023/11/10 17:49:15
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "设置通信地址")
@Protocol(code = 30000, name = "设置通信地址", type = ProtocolType.INTERACT, deviceType = DeviceType.GATEWAY, request = Interact30000.Request.class)
public class Interact30000 {

    private Interact30000() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求")
    public static class Request extends ToStringBase implements com.demo.entity.protocol.Protocol.Data {

        /**
         * URI
         */
        @Schema(description = "URI")
        @Field(name = "URI")
        private String uri;
        /**
         * 用户名
         */
        @Schema(description = "用户名")
        @Field(name = "用户名")
        private String username;
        /**
         * 密码
         */
        @Schema(description = "密码")
        @Field(name = "密码")
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
