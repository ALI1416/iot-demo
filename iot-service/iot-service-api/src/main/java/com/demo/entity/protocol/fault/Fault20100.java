package com.demo.entity.protocol.fault;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import com.demo.entity.protocol.Protocol;
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
@ProtocolClass(code = 20100, name = "温度计故障", type = ProtocolType.FAULT, deviceType = DeviceType.THERMOMETER, faultClass = Fault20100.Fault.class)
public class Fault20100 {

    private Fault20100() {
    }

    public static class Fault implements Protocol.Fault {

        /**
         * 获取故障信息列表
         */
        @Override
        public ProtocolInfo.Fault[] getFaultInfoList() {
            return FAULT_NAME_ARRAY;
        }

        /**
         * 故障名称数组(1组)
         */
        private static final ProtocolInfo.Fault[] FAULT_NAME_ARRAY = new ProtocolInfo.Fault[]{
                new ProtocolInfo.Fault("报警", new String[]{
                        "温度过高",
                        "温度过低",
                }),
        };

    }

}