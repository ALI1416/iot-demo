package com.demo.entity.po;

import com.demo.announce.DeviceType;
import com.demo.base.EntityBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>设备</h1>
 *
 * <p>
 * createDate 2023/11/30 14:35:11
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "设备")
public class Device extends EntityBase {

    /**
     * 网关id
     */
    @Schema(description = "网关id")
    private Long gatewayId;
    /**
     * 设备序号
     */
    @Schema(description = "设备序号")
    private Integer sn;
    /**
     * 设备名称
     */
    @Schema(description = "设备名称")
    private String name;
    /**
     * 设备类型 0网关 1温度计
     *
     * @see DeviceType
     */
    @Schema(description = "设备类型 0网关 1温度计")
    private Integer type;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String comment;

}
