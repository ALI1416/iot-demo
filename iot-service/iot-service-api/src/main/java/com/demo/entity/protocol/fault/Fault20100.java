package com.demo.entity.protocol.fault;

import com.demo.announce.DeviceType;
import com.demo.announce.FaultMap;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

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
@ProtocolClass(code = 20100, name = "温度计故障", type = ProtocolType.FAULT, deviceType = DeviceType.THERMOMETER, faultMap = FaultMap.FAULT_20100)
public class Fault20100 {

    private Fault20100() {
    }

    public static final Map<String, String[]> MAP = new HashMap<>();

    // 1组
    static {
        MAP.put("报警", new String[]{
                "温度过高",
                "温度过低",
        });
    }

}