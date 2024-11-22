package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.FieldAnno;
import com.demo.announce.FieldUnitEnum;
import com.demo.announce.ProtocolAnno;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
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
@ProtocolAnno(code = 3000100, name = "设置温度计配置", deviceType = DeviceType.THERMOMETER, special = true,
        interact = @ProtocolAnno.Interact(request = Interact3000100.Request.class)
)
public class Interact3000100 {

    private Interact3000100() {
    }

    /**
     * 请求
     */
    @Getter
    @Setter
    @Schema(description = "请求", name = "Interact3000100.Request")
    public static class Request extends ToStringBase implements Protocol.Data {

        /**
         * 刷新间隔(s 秒)
         */
        @Schema(description = "刷新间隔(s 秒)")
        @FieldAnno(name = "刷新间隔", unit = FieldUnitEnum.SECOND, min = "1", max = "60")
        private Integer intervalRefresh;
        /**
         * 推送间隔(s 秒)
         */
        @Schema(description = "推送间隔(s 秒)")
        @FieldAnno(name = "推送间隔", unit = FieldUnitEnum.SECOND, min = "1", max = "60", special = true)
        private Integer intervalPush;
        /**
         * 温度上限(℃ 摄氏度)
         */
        @Schema(description = "温度上限(℃ 摄氏度)")
        @FieldAnno(name = "温度上限", unit = FieldUnitEnum.TEMPERATURE, min = "-55", max = "99", special = true)
        private Integer temperatureMax;
        /**
         * 温度下限(℃ 摄氏度)
         */
        @Schema(description = "温度下限(℃ 摄氏度)")
        @FieldAnno(name = "温度下限", unit = FieldUnitEnum.TEMPERATURE, min = "-55", max = "99")
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
