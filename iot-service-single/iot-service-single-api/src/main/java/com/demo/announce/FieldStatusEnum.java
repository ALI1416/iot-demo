package com.demo.announce;

import com.demo.entity.protocol.ProtocolInfo;
import lombok.Getter;

/**
 * <h1>字段状态枚举</h1>
 *
 * <p>
 * createDate 2024/01/18 09:42:56
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldStatusEnum {

    /**
     * 空
     */
    NULL(new ProtocolInfo.FieldStatus(null, null)),
    /**
     * 设备类型 0网关 1温度计
     *
     * @see DeviceType
     */
    DEVICE_TYPE(
            new ProtocolInfo.FieldStatus("0", "网关"),
            new ProtocolInfo.FieldStatus("1", "温度计")
    ),
    ;

    /**
     * 字段状态
     */
    private final ProtocolInfo.FieldStatus[] status;

    FieldStatusEnum(ProtocolInfo.FieldStatus... status) {
        this.status = status;
    }

}
