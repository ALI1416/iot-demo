package com.demo.announce;

import com.demo.entity.protocol.ProtocolInfo;
import com.demo.entity.protocol.fault.Fault20100;
import lombok.Getter;

/**
 * <h1>故障枚举</h1>
 *
 * <p>
 * createDate 2024/01/18 11:43:24
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FaultEnum {

    /**
     * 温度计故障
     */
    FAULT_20100(Fault20100.FAULT),

    /**
     * 空
     */
    NULL(null);

    /**
     * 故障信息
     */
    private final ProtocolInfo.FaultInfo[] faultInfo;

    FaultEnum(ProtocolInfo.FaultInfo[] faultInfo) {
        this.faultInfo = faultInfo;
    }

}
