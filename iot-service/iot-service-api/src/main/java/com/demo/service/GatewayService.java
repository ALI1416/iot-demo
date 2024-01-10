package com.demo.service;

import com.demo.announce.DeviceType;
import com.demo.base.ServiceBase;
import com.demo.constant.Constant;
import com.demo.dao.mysql.GatewayDao;
import com.demo.entity.vo.DeviceVo;
import com.demo.entity.vo.GatewayVo;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2023/11/30 17:13:41
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class GatewayService extends ServiceBase {

    private final GatewayDao gatewayDao;

    /**
     * 本地缓存 网关序号,设备序号,设备类型
     */
    private final Map<Integer, Map<Integer, Integer>> localCache = new HashMap<>();

    /**
     * 插入
     *
     * @param gateway sn
     * @return ok:id,e:0
     */
    @Transactional
    public long insert(GatewayVo gateway) {
        return gatewayDao.insert(gateway);
    }

    /**
     * 获取本地缓存
     *
     * @return Map 网关序号,设备序号,设备类型
     */
    public Map<Integer, Map<Integer, Integer>> getLocalCache() {
        return localCache;
    }

    /**
     * 获取网关序号
     *
     * @return 网关序号列表
     */
    public Set<Integer> getGatewaySn() {
        return localCache.keySet();
    }

    /**
     * 存在网关序号
     *
     * @param gatewaySn 网关序号
     * @return 存在网关序号
     */
    public boolean existGatewaySn(int gatewaySn) {
        return localCache.containsKey(gatewaySn);
    }

    /**
     * 获取设备Map
     *
     * @param gatewaySn 网关序号
     * @return 设备Map
     */
    public Map<Integer, Integer> getDevice(int gatewaySn) {
        return localCache.get(gatewaySn);
    }

    /**
     * 网关序号、设备序号、设备类型不匹配
     *
     * @param gatewaySn  网关序号
     * @param deviceSn   设备序号
     * @param deviceType 设备类型
     * @return 网关序号、设备序号、设备类型不匹配
     */
    public boolean notValid(int gatewaySn, int deviceSn, int deviceType) {
        Map<Integer, Integer> deviceMap = localCache.get(gatewaySn);
        if (deviceMap == null) {
            return false;
        }
        // 网关
        if (deviceSn == 0 && deviceType == DeviceType.GATEWAY.getCode()) {
            return false;
        }
        return deviceType != deviceMap.get(deviceSn);
    }

    /**
     * 更新缓存
     */
    @Async
    @PostConstruct
    @Scheduled(cron = Constant.UPDATE_CRON)
    public void updateCache() {
        for (GatewayVo gateway : gatewayDao.findAllAndDevice()) {
            Map<Integer, Integer> deviceMap = new HashMap<>();
            for (DeviceVo device : gateway.getDeviceList()) {
                deviceMap.put(device.getSn(), device.getType());
            }
            localCache.put(gateway.getSn(), deviceMap);
        }
    }

}
