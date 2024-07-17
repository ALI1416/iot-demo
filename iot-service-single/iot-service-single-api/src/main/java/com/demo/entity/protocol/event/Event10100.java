package com.demo.entity.protocol.event;

import com.demo.announce.*;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>温度计事件</h1>
 *
 * <p>
 * createDate 2023/11/10 17:12:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "温度计事件")
@Protocol(code = 10100, name = "温度计事件", type = ProtocolType.EVENT, deviceType = DeviceType.THERMOMETER, event = Event10100.Event.class)
public class Event10100 {

    private Event10100() {
    }

    /**
     * 温度计事件
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件", name = "Event10100.Event")
    public static class Event extends ToStringBase implements com.demo.entity.protocol.Protocol.Data {

        /**
         * 温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "温度(0.0001℃ 0.0001摄氏度)")
        @Field(name = "温度", type = FieldType.INSTANT, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperature;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(temperature);
        }

    }

}
