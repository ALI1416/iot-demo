package com.demo.entity.protocol.broadcast;

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
 * <h1>校时广播</h1>
 *
 * <p>
 * createDate 2024/11/15 17:18:48
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "校时广播")
@ProtocolAnno(code = 50000, name = "校时广播", deviceType = DeviceType.GATEWAY,
        broadcast = @ProtocolAnno.Broadcast(broadcast = Broadcast50000.Broadcast.class)
)
public class Broadcast50000 {

    private Broadcast50000() {
    }

    /**
     * 校时广播
     */
    @Getter
    @Setter
    @Schema(description = "校时广播", name = "Broadcast50000.Broadcast")
    public static class Broadcast extends ToStringBase implements Protocol.Data {

        /**
         * 毫秒时间戳
         */
        @Schema(description = "毫秒时间戳")
        @FieldAnno(name = "毫秒时间戳")
        private Long timestamp;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(
                    timestamp
            );
        }

    }

}
