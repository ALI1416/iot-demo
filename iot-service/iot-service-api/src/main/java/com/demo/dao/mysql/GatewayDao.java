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
     * @param gateway sn
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
     * 查询所有
     *
     * @return List GatewayVo
     */
    public List<GatewayVo> findAll() {
        return gatewayMapper.findAll();
    }

    /**
     * 查询所有(含设备)
     *
     * @return List GatewayVo
     */
    public List<GatewayVo> findAllAndDevice() {
        return gatewayMapper.findAllAndDevice();
    }

}
