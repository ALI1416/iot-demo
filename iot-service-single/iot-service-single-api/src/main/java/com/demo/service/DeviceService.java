package com.demo.service;

import com.demo.base.ServiceBase;
import com.demo.dao.mysql.DeviceDao;
import com.demo.entity.pojo.PageInfo;
import com.demo.entity.vo.DeviceVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h1>设备</h1>
 *
 * <p>
 * createDate 2023/11/30 17:21:12
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class DeviceService extends ServiceBase {

    private final DeviceDao deviceDao;

    /**
     * 插入
     *
     * @param device gatewayId,sn,name,type
     * @return ok:id,e:0
     */
    @Transactional
    public long insert(DeviceVo device) {
        return deviceDao.insert(device);
    }

    /**
     * 查询通过id
     *
     * @param id id
     * @return DeviceVo
     */
    public DeviceVo findById(long id) {
        return deviceDao.findById(id);
    }

    /**
     * 查询通过sn
     *
     * @param sn sn
     * @return DeviceVo
     */
    public DeviceVo findBySn(int sn) {
        return deviceDao.findBySn(sn);
    }

    /**
     * 查询通过gatewaySn
     *
     * @param gatewaySn gatewaySn
     * @return List DeviceVo
     */
    public List<DeviceVo> findByGatewaySn(int gatewaySn) {
        return deviceDao.findByGatewaySn(gatewaySn);
    }

    /**
     * 查询
     *
     * @param device DeviceVo
     * @return PageInfo DeviceVo
     */
    public PageInfo<DeviceVo> find(DeviceVo device) {
        return pagination(() -> deviceDao.find(device), device.getPages(), device.getRows(), device.getOrderBy());
    }

}
