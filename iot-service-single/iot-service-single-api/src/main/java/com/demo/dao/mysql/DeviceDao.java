package com.demo.dao.mysql;

import cn.z.id.Id;
import com.demo.base.DaoBase;
import com.demo.entity.vo.DeviceVo;
import com.demo.mapper.DeviceMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>设备</h1>
 *
 * <p>
 * createDate 2023/11/30 17:08:36
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class DeviceDao extends DaoBase {

    private final DeviceMapper deviceMapper;

    /**
     * 插入
     *
     * @param device gatewayId,sn,name,type
     * @return ok:id,e:0
     */
    public long insert(DeviceVo device) {
        device.setId(Id.next());
        if (tryEq1(() -> deviceMapper.insert(device))) {
            return device.getId();
        }
        return 0L;
    }

    /**
     * 查询通过id
     *
     * @param id id
     * @return DeviceVo
     */
    public DeviceVo findById(long id) {
        DeviceVo deviceVo = new DeviceVo();
        deviceVo.setId(id);
        return deviceMapper.findOne(deviceVo);
    }

    /**
     * 查询通过sn
     *
     * @param sn sn
     * @return DeviceVo
     */
    public DeviceVo findBySn(int sn) {
        DeviceVo deviceVo = new DeviceVo();
        deviceVo.setSn(sn);
        return deviceMapper.findOne(deviceVo);
    }

    /**
     * 查询
     *
     * @param device DeviceVo
     * @return List DeviceVo
     */
    public List<DeviceVo> find(DeviceVo device) {
        return deviceMapper.find(device);
    }

    /**
     * 查询通过gatewaySn
     *
     * @param gatewaySn gatewaySn
     * @return List DeviceVo
     */
    public List<DeviceVo> findByGatewaySn(int gatewaySn) {
        return deviceMapper.findByGatewaySn(gatewaySn);
    }

}
