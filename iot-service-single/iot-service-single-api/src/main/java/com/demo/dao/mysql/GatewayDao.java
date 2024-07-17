package com.demo.dao.mysql;

import cn.z.id.Id;
import com.demo.base.DaoBase;
import com.demo.entity.vo.GatewayVo;
import com.demo.mapper.GatewayMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2023/11/30 17:11:28
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class GatewayDao extends DaoBase {

    private final GatewayMapper gatewayMapper;

    /**
     * 插入
     *
     * @param gateway sn,name
     * @return ok:id,e:0
     */
    public long insert(GatewayVo gateway) {
        gateway.setId(Id.next());
        if (tryEq1(() -> gatewayMapper.insert(gateway))) {
            return gateway.getId();
        }
        return 0L;
    }

    /**
     * 查询所有(含设备)
     *
     * @return List GatewayVo
     */
    public List<GatewayVo> findAllAndDevice() {
        return gatewayMapper.findAllAndDevice();
    }

    /**
     * 查询通过id
     *
     * @param id id
     * @return GatewayVo
     */
    public GatewayVo findById(long id) {
        GatewayVo gatewayVo = new GatewayVo();
        gatewayVo.setId(id);
        return gatewayMapper.findOne(gatewayVo);
    }

    /**
     * 查询通过sn
     *
     * @param sn sn
     * @return GatewayVo
     */
    public GatewayVo findBySn(int sn) {
        GatewayVo gatewayVo = new GatewayVo();
        gatewayVo.setSn(sn);
        return gatewayMapper.findOne(gatewayVo);
    }

    /**
     * 查询
     *
     * @param gateway GatewayVo
     * @return List GatewayVo
     */
    public List<GatewayVo> find(GatewayVo gateway) {
        return gatewayMapper.find(gateway);
    }

}
