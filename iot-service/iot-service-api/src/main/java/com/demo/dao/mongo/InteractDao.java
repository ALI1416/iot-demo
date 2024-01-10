package com.demo.dao.mongo;

import cn.z.clock.Clock;
import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <h1>互动</h1>
 *
 * <p>
 * createDate 2023/11/14 14:59:35
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class InteractDao extends DaoBase {

    private static final Class<ProtocolVo> CLAZZ = ProtocolVo.class;
    private final MongoTemp mongoTemp;

    private String getCollectionName(ProtocolVo base) {
        return base.getGatewaySn() + MongoConstant.INTERACT + "_" + DateUtils.getYyyy(base.getCreateTime().getTime());
    }

    private String getCollectionName2(ProtocolVo base) {
        return base.getGatewaySn() + MongoConstant.INTERACT + "_" + DateUtils.getYyyy(base.getUpdateTime().getTime());
    }

    /**
     * 插入
     *
     * @param base ProtocolVo(自动填充id/createTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo base) {
        base.setId(Id.next());
        base.setCreateTime(Clock.timestamp());
        // [网关序号]_interact_[yyyy]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(base, getCollectionName(base)));
    }

    /**
     * 插入2
     *
     * @param base ProtocolVo(自动填充id/updateTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo insert2(ProtocolVo base) {
        base.setId(Id.next());
        base.setUpdateTime(Clock.timestamp());
        // [网关序号]_interact_[yyyy]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(base, getCollectionName2(base)));
    }

    /**
     * 查询通过id
     *
     * @param id             id
     * @param collectionName 集合名
     * @return ProtocolVo
     */
    public ProtocolVo findById(long id, String collectionName) {
        return mongoTemp.findById(id, CLAZZ, collectionName);
    }

    /**
     * 更新
     *
     * @param base ProtocolVo(自动填充updateTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo save(ProtocolVo base) {
        base.setUpdateTime(Clock.timestamp());
        // [网关序号]_interact_[yyyy]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.save(base, getCollectionName(base)));
    }

}
