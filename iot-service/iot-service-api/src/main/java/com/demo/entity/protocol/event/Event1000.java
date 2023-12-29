package com.demo.entity.protocol.event;

import com.demo.announce.DeviceType;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * <h1>心跳</h1>
 *
 * <p>
 * createDate 2023/12/13 14:37:37
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "心跳")
@ProtocolClass(code = 1000, name = "心跳", type = ProtocolType.EVENT, deviceType = DeviceType.GATEWAY)
public class Event1000 {
}
