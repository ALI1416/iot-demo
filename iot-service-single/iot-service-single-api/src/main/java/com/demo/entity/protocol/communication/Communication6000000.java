package com.demo.entity.protocol.communication;

import cn.z.clock.Clock;
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
 * <h1>获取时间戳</h1>
 *
 * <p>
 * createDate 2024/11/18 11:04:42
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取时间戳")
@ProtocolAnno(code = 6000000, name = "获取时间戳", deviceType = DeviceType.GATEWAY,
        communication = @ProtocolAnno.Communication(
                write = Communication6000000.Write.class,
                writeHandle = Communication6000000.WriteHandle.class)
)
public class Communication6000000 {

    private Communication6000000() {
    }

    /**
     * 写入
     */
    @Getter
    @Setter
    @Schema(description = "写入", name = "Communication6000000.Write")
    public static class Write extends ToStringBase implements Protocol.Data {

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

    /**
     * 写入处理
     */
    @Schema(description = "写入处理", name = "Communication6000000.WriteHandle")
    public static class WriteHandle implements Protocol.WriteHandle {

        /**
         * 写入处理
         *
         * @param read 读取
         * @return 写入
         */
        @Override
        public Protocol.Data handle(Protocol.Data read) {
            Write write = new Write();
            write.setTimestamp(Clock.now());
            return write;
        }

    }

}
