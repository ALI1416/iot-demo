package com.demo.entity.vo;

import com.demo.entity.po.Gateway;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2023/11/30 16:45:01
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "网关")
public class GatewayVo extends Gateway {

    /**
     * 设备列表
     */
    @Schema(description = "设备列表")
    private List<DeviceVo> deviceList;
    /**
     * 上一次在线时间-否定
     */
    @Schema(description = "上一次在线时间-否定")
    private Timestamp lastOnlineTimeNot;
    /**
     * 上一次在线时间-结束
     */
    @Schema(description = "上一次在线时间-结束")
    private Timestamp lastOnlineTimeEnd;

}
