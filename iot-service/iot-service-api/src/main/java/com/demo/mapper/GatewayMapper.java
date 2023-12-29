package com.demo.mapper;

import com.demo.entity.vo.GatewayVo;

import java.util.List;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2023/11/30 15:12:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public interface GatewayMapper {

    /**
     * 插入
     *
     * @param gateway id,sn
     * @return 执行成功数量
     */
    int insert(GatewayVo gateway);

    /**
     * 查询所有
     *
     * @return List GatewayVo
     */
    List<GatewayVo> findAll();

    /**
     * 查询所有(含设备)
     *
     * @return List GatewayVo
     */
    List<GatewayVo> findAllAndDevice();

}
