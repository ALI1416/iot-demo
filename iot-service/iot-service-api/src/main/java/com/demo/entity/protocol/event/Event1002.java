package com.demo.entity.protocol.event;

import com.demo.announce.*;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>温湿度</h1>
 *
 * <p>
 * createDate 2023/11/10 17:12:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "温湿度")
@ProtocolClass(code = 1002, name = "温湿度", type = ProtocolType.EVENT, deviceType = DeviceType.THERMO_HYGRO_METER, eventClass = Event1002.Event.class)
public class Event1002 {

    private Event1002() {
    }

    /**
     * 事件
     */
    @Getter
    @Setter
    @Schema(description = "事件")
    public static class Event extends ToStringBase implements Protocol.Data {

        /**
         * 温度(m℃ 毫摄氏度)
         */
        @Schema(description = "温度(m℃ 毫摄氏度)")
        @ProtocolField(name = "温度", unit = FieldUnit.CENTIGRADE_N3)
        private Integer temperature;
        /**
         * 湿度(%% 万分之)
         */
        @Schema(description = "湿度(%% 万分之)")
        @ProtocolField(name = "湿度", unit = FieldUnit.PERCENTAGE_N2)
        private Integer humidity;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(temperature, humidity);
        }

    }

}
