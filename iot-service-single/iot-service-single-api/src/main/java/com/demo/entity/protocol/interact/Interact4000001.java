package com.demo.entity.protocol.interact;

import com.demo.announce.DeviceType;
import com.demo.announce.FieldAnno;
import com.demo.announce.FieldStatusEnum;
import com.demo.announce.ProtocolAnno;
import com.demo.base.ControllerBase;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * <h1>获取网关配置</h1>
 *
 * <p>
 * createDate 2023/11/17 11:17:17
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "获取网关配置")
@ProtocolAnno(code = 4000001, name = "获取网关配置", deviceType = DeviceType.GATEWAY,
        interact = @ProtocolAnno.Interact(response = Interact4000001.Response.class)
)
public class Interact4000001 {

    private Interact4000001() {
    }

    /**
     * 响应
     */
    @Getter
    @Setter
    @Schema(description = "响应", name = "Interact4000001.Response")
    public static class Response extends ToStringBase implements Protocol.Data {

        /**
         * 设备列表
         */
        @Schema(description = "设备列表")
        @FieldAnno(name = "设备列表")
        private List<Device> deviceList;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return ControllerBase.existNull(deviceList);
        }

    }

    /**
     * 设备
     */
    @Getter
    @Setter
    @Schema(description = "设备")
    public static class Device extends ToStringBase {

        /**
         * 设备序号
         */
        @Schema(description = "设备序号")
        @FieldAnno(name = "设备序号")
        private Integer sn;
        /**
         * 设备类型 0网关 1温度计
         *
         * @see DeviceType
         */
        @Schema(description = "设备类型 0网关 1温度计")
        @FieldAnno(name = "设备类型", status = FieldStatusEnum.DEVICE_TYPE)
        private Integer type;

    }

}
