package com.demo.service;

import com.demo.base.ServiceBase;
import com.demo.dao.mysql.DeviceDao;
import com.demo.entity.vo.DeviceVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param device deviceId,sn
     * @return ok:id,e:0
     */
    @Transactional
    public long insert(DeviceVo device) {
        return deviceDao.insert(device);
    }

}
