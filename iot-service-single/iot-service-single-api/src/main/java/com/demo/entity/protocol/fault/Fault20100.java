package com.demo.entity.protocol.fault;

import com.demo.announce.DeviceType;
import com.demo.announce.FaultEnum;
import com.demo.announce.Protocol;
import com.demo.announce.ProtocolType;
import com.demo.entity.protocol.ProtocolInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>温度计故障</h1>
 *
 * <p>
 * createDate 2023/12/11 11:02:38
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "温度计故障")
@Protocol(code = 20100, name = "温度计故障", type = ProtocolType.FAULT, deviceType = DeviceType.THERMOMETER, fault = FaultEnum.FAULT_20100)
public class Fault20100 {

    private Fault20100() {
    }

    // 1组
    public static final ProtocolInfo.FaultInfo[] FAULT = new ProtocolInfo.FaultInfo[]{
            new ProtocolInfo.FaultInfo("警报", new String[]{
                    "温度过高",
                    "温度过低",
            })
    };

}