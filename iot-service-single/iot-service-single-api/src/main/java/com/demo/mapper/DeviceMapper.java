package com.demo.mapper;

import com.demo.entity.vo.DeviceVo;
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
     * @param device id,gatewayId,sn,name,type
     * @return 执行成功数量
     */
    int insert(DeviceVo device);

    /**
     * 查询唯一键
     *
     * @param device id,sn(至少1个)
     * @return DeviceVo
     */
    DeviceVo findOne(DeviceVo device);

    /**
     * 查询
     *
     * @param device DeviceVo
     * @return List DeviceVo
     */
    List<DeviceVo> find(DeviceVo device);

    /**
     * 查询通过gatewaySn
     *
     * @param gatewaySn gatewaySn
     * @return List DeviceVo
     */
    List<DeviceVo> findByGatewaySn(int gatewaySn);

}
