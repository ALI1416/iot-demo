package com.demo.entity.protocol.interact;

import com.demo.announce.*;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设置温湿度报警</h1>
 *
 * <p>
 * createDate 2023/11/15 10:07:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "设置温湿度报警")
@ProtocolClass(code = 3002, name = "设置温湿度报警", type = ProtocolType.INTERACT, deviceType = DeviceType.THERMO_HYGRO_METER, requestClass = Interact3002.Request.class)
public class Interact3002 {

    private Interact3002() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求")
    public static class Request extends ToStringBase implements Protocol.Data {

        /**
         * 温度上限(m℃ 毫摄氏度)
         */
        @Schema(description = "温度上限(m℃ 毫摄氏度)")
        @ProtocolField(name = "温度上限", unit = FieldUnit.CENTIGRADE_N3)
        private Integer temperatureMax;
        /**
         * 温度下限(m℃ 毫摄氏度)
         */
        @Schema(description = "温度下限(m℃ 毫摄氏度)")
        @ProtocolField(name = "温度下限", unit = FieldUnit.CENTIGRADE_N3)
        private Integer temperatureMin;
        /**
         * 湿度上限(%% 万分之)
         */
        @Schema(description = "湿度上限(%% 万分之)")
        @ProtocolField(name = "湿度上限", unit = FieldUnit.PERCENTAGE_N2)
        private Integer humidityMax;
        /**
         * 湿度下限(%% 万分之)
         */
        @Schema(description = "湿度下限(%% 万分之)")
        @ProtocolField(name = "湿度下限", unit = FieldUnit.PERCENTAGE_N2)
        private Integer humidityMin;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(temperatureMax, temperatureMin, humidityMax, humidityMin);
        }

    }

}
