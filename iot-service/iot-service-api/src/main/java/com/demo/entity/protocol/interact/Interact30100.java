package com.demo.entity.protocol.interact;

import com.demo.announce.*;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设置温度计参数</h1>
 *
 * <p>
 * createDate 2023/11/15 10:07:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "设置温度计参数")
@ProtocolClass(code = 30100, name = "设置温度计参数", type = ProtocolType.INTERACT, deviceType = DeviceType.THERMOMETER, requestClass = Interact30100.Request.class)
public class Interact30100 {

    private Interact30100() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求")
    public static class Request extends ToStringBase implements Protocol.Data {

        /**
         * 刷新间隔(s 秒)
         */
        @Schema(description = "刷新间隔(s 秒)")
        @ProtocolField(name = "刷新间隔", unit = FieldUnit.SECOND)
        private Integer intervalRefresh;
        /**
         * 推送间隔(s 秒)
         */
        @Schema(description = "推送间隔(s 秒)")
        @ProtocolField(name = "推送间隔", unit = FieldUnit.SECOND)
        private Integer intervalPush;
        /**
         * 温度上限(℃ 摄氏度)
         */
        @Schema(description = "温度上限(℃ 摄氏度)")
        @ProtocolField(name = "温度上限", unit = FieldUnit.TEMPERATURE)
        private Integer temperatureMax;
        /**
         * 温度下限(℃ 摄氏度)
         */
        @Schema(description = "温度下限(℃ 摄氏度)")
        @ProtocolField(name = "温度下限", unit = FieldUnit.TEMPERATURE)
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
