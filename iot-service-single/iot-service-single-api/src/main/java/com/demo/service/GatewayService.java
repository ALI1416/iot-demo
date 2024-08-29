package com.demo.service;

import com.demo.announce.DeviceType;
import com.demo.base.ServiceBase;
import com.demo.dao.mysql.GatewayDao;
import com.demo.entity.pojo.PageInfo;
import com.demo.entity.vo.DeviceVo;
import com.demo.entity.vo.GatewayVo;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    /**
     * 网关表
     */
    private static final List<GatewayVo> TABLE = new ArrayList<>();

    private final GatewayDao gatewayDao;

    /**
     * 插入
     *
     * @param gateway sn,name
     * @return ok:id,e:0
     */
    @Transactional
    public long insert(GatewayVo gateway) {
        return gatewayDao.insert(gateway);
    }

    /**
     * 查询通过id
     *
     * @param id id
     * @return GatewayVo
     */
    public GatewayVo findById(long id) {
        return gatewayDao.findById(id);
    }

    /**
     * 查询通过sn
     *
     * @param sn sn
     * @return GatewayVo
     */
    public GatewayVo findBySn(int sn) {
        return gatewayDao.findBySn(sn);
    }

    /**
     * 查询
     *
     * @param gateway GatewayVo
     * @return PageInfo GatewayVo
     */
    public PageInfo<GatewayVo> find(GatewayVo gateway) {
        return pagination(() -> gatewayDao.find(gateway), gateway.getPages(), gateway.getRows(), gateway.getOrderBy());
    }

    /**
     * 获取网关表
     *
     * @return 网关表
     */
    public static List<GatewayVo> getTable() {
        return TABLE;
    }

    /**
     * 获取网关序号列表
     *
     * @return 网关序号列表
     */
    public static List<Integer> getGatewaySnList() {
        return TABLE.stream().map(GatewayVo::getSn).toList();
    }

    /**
     * 获取网关
     *
     * @param gatewaySn 网关序号
     * @return 网关
     */
    public static GatewayVo get(int gatewaySn) {
        return TABLE.stream().filter(g -> g.getSn() == gatewaySn).findFirst().orElse(null);
    }

    /**
     * 存在网关
     *
     * @param gatewaySn 网关序号
     * @return 存在网关
     */
    public static boolean exist(int gatewaySn) {
        return TABLE.stream().anyMatch(g -> g.getSn() == gatewaySn);
    }

    /**
     * 存在设备
     *
     * @param gatewaySn 网关序号
     * @param deviceSn  设备序号
     * @return 存在设备
     */
    public static boolean existDevice(int gatewaySn, int deviceSn) {
        return getDeviceList(gatewaySn).stream().anyMatch(g -> g.getSn() == deviceSn);
    }

    /**
     * 网关序号、设备序号、设备类型不匹配
     *
     * @param gatewaySn  网关序号
     * @param deviceSn   设备序号
     * @param deviceType 设备类型
     * @return 网关序号、设备序号、设备类型不匹配
     */
    public static boolean notValid(int gatewaySn, int deviceSn, int deviceType) {
        if (deviceType == DeviceType.GATEWAY.getCode()) {
            return false;
        }
        DeviceVo device = getDevice(gatewaySn, deviceSn);
        if (device == null) {
            return true;
        }
        return deviceType != device.getType();
    }

    /**
     * 获取设备列表
     *
     * @param gatewaySn 网关序号
     * @return 设备列表
     */
    public static List<DeviceVo> getDeviceList(int gatewaySn) {
        return TABLE.stream().filter(g -> g.getSn() == gatewaySn).findFirst().map(GatewayVo::getDeviceList).orElse(Collections.emptyList());
    }

    /**
     * 获取设备
     *
     * @param gateway  网关
     * @param deviceSn 设备序号
     * @return 设备
     */
    public static DeviceVo getDevice(GatewayVo gateway, int deviceSn) {
        if (gateway == null) {
            return null;
        }
        return gateway.getDeviceList().stream().filter(g -> g.getSn() == deviceSn).findFirst().orElse(null);
    }

    /**
     * 获取设备
     *
     * @param gatewaySn 网关序号
     * @param deviceSn  设备序号
     * @return 设备
     */
    public static DeviceVo getDevice(int gatewaySn, int deviceSn) {
        return getDevice(get(gatewaySn), deviceSn);
    }

    /**
     * 获取设备列表
     *
     * @param gateway    网关
     * @param deviceType 设备类型
     * @return 设备列表
     */
    public static List<DeviceVo> getDeviceList(GatewayVo gateway, DeviceType deviceType) {
        if (gateway == null || deviceType == null) {
            return Collections.emptyList();
        }
        return gateway.getDeviceList().stream().filter(g -> g.getType() == deviceType.getCode()).toList();
    }

    /**
     * 获取设备列表
     *
     * @param gatewaySn  网关序号
     * @param deviceType 设备类型
     * @return 设备列表
     */
    public static List<DeviceVo> getDeviceList(int gatewaySn, DeviceType deviceType) {
        if (deviceType == null) {
            return Collections.emptyList();
        }
        return getDeviceList(get(gatewaySn), deviceType);
    }

    /**
     * 更新网关表(每隔2小时的2分3秒)
     */
    @Async
    // @Scheduled(cron = "3 * * * * *")
    @Scheduled(cron = "3 2 0/2 * * *")
    @PostConstruct
    public void updateTable() {
        TABLE.clear();
        TABLE.addAll(gatewayDao.findAllAndDevice());
    }

}
