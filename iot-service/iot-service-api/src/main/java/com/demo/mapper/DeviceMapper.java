package com.demo.mapper;

import com.demo.entity.vo.DeviceVo;

import java.util.List;

/**
 * <h1>设备</h1>
 *
 * <p>
 * createDate 2023/11/30 16:53:36
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public interface DeviceMapper {

    /**
     * 插入
     *
     * @param device id,gateway_id,sn,type
     * @return 执行成功数量
     */
    int insert(DeviceVo device);

    /**
     * 查询，通过gatewayId
     *
     * @param gatewayId gatewayId
     * @return List DeviceVo
     */
    List<DeviceVo> findByGatewayId(long gatewayId);

}
