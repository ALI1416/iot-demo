package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.FieldAnno;
import com.demo.announce.ProtocolAnno;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
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
@ProtocolAnno(code = 3000000, name = "设置通信地址", deviceType = DeviceType.GATEWAY,
        interact = @ProtocolAnno.Interact(request = Interact3000000.Request.class)
)
public class Interact3000000 {

    private Interact3000000() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求", name = "Interact3000000.Request")
    public static class Request extends ToStringBase implements Protocol.Data {

        /**
         * URI
         */
        @Schema(description = "URI")
        @FieldAnno(name = "URI")
        private String uri;
        /**
         * 用户名
         */
        @Schema(description = "用户名")
        @FieldAnno(name = "用户名")
        private String username;
        /**
         * 密码
         */
        @Schema(description = "密码")
        @FieldAnno(name = "密码")
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
