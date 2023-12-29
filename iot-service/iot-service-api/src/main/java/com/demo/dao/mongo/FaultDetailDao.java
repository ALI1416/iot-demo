package com.demo.dao.mongo;

import cn.z.clock.Clock;
import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * <h1>故障详情</h1>
 *
 * <p>
 * createDate 2023/12/07 17:36:19
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class FaultDetailDao extends DaoBase {

    private static final Class<ProtocolVo> CLAZZ = ProtocolVo.class;
    private final MongoTemp mongoTemp;

    /**
     * 插入
     *
     * @param base ProtocolVo(自动填充id/createTime/updateTime)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo base) {
        base.setId(Id.next());
        base.setCreateTime(Clock.timestamp());
        base.setUpdateTime(base.getCreateTime());
        // [网关序号]_fault_detail_[yyyy]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(base, base.getGatewaySn() + MongoConstant.FAULT_DETAIL + "_" + DateUtils.getYyyy(base.getCreateTime().getTime())));
    }

    /**
     * 更新
     *
     * @param base ProtocolVo(自动更新updateTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo save(ProtocolVo base) {
        base.setUpdateTime(Clock.timestamp());
        // [网关序号]_fault_detail_[yyyy]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.save(base, base.getGatewaySn() + MongoConstant.FAULT_DETAIL + "_" + DateUtils.getYyyy(base.getCreateTime().getTime())));
    }

    /**
     * 查询第一个
     *
     * @param query          Query
     * @param collectionName 集合名
     * @return ProtocolVo
     */
    public ProtocolVo findOne(Query query, String collectionName) {
        return mongoTemp.findOne(query, CLAZZ, collectionName);
    }

}
