package com.demo.entity.protocol.interact;

import com.demo.announce.*;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设置温度计配置</h1>
 *
 * <p>
 * createDate 2023/11/15 10:07:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "设置温度计配置")
@Protocol(code = 30100, name = "设置温度计配置", type = ProtocolType.INTERACT, deviceType = DeviceType.THERMOMETER, request = Interact30100.Request.class, special = true)
public class Interact30100 {

    private Interact30100() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求", name = "Interact30100.Request")
    public static class Request extends ToStringBase implements com.demo.entity.protocol.Protocol.Data {

        /**
         * 刷新间隔(s 秒)
         */
        @Schema(description = "刷新间隔(s 秒)")
        @Field(name = "刷新间隔", unit = FieldUnitEnum.SECOND, min = "1", max = "60")
        private Integer intervalRefresh;
        /**
         * 推送间隔(s 秒)
         */
        @Schema(description = "推送间隔(s 秒)")
        @Field(name = "推送间隔", unit = FieldUnitEnum.SECOND, min = "1", max = "60", special = true)
        private Integer intervalPush;
        /**
         * 温度上限(℃ 摄氏度)
         */
        @Schema(description = "温度上限(℃ 摄氏度)")
        @Field(name = "温度上限", unit = FieldUnitEnum.TEMPERATURE, min = "-55", max = "99", special = true)
        private Integer temperatureMax;
        /**
         * 温度下限(℃ 摄氏度)
         */
        @Schema(description = "温度下限(℃ 摄氏度)")
        @Field(name = "温度下限", unit = FieldUnitEnum.TEMPERATURE, min = "-55", max = "99")
        private Integer temperatureMin;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(intervalRefresh, intervalPush, temperatureMax, temperatureMin);
        }

    }

}
