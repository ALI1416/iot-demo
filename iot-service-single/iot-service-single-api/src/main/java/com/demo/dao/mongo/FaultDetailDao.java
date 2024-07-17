package com.demo.dao.mongo;

import cn.z.clock.Clock;
import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
     * @param protocol ProtocolVo(自动填充id, createTime, updateTime)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo protocol) {
        protocol.setId(Id.next());
        protocol.setCreateTime(Id.newTimestamp(protocol.getId()));
        protocol.setUpdateTime(protocol.getCreateTime());
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(protocol,
                MongoConstant.getFaultDetailCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

    /**
     * 更新
     *
     * @param protocol ProtocolVo(自动更新updateTime 必须gatewaySn, createTime)
     * @return ok:T,e:null
     */
    public ProtocolVo save(ProtocolVo protocol) {
        protocol.setUpdateTime(Clock.timestamp());
        return tryAnyNoTransactionReturnT(() -> mongoTemp.save(protocol,
                MongoConstant.getFaultDetailCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

    /**
     * 查询第一个
     *
     * @param query      Query
     * @param gatewaySn  网关序号
     * @param createTime 创建时间
     * @return ProtocolVo
     */
    public ProtocolVo findOne(Query query, int gatewaySn, Timestamp createTime) {
        return mongoTemp.findOne(query, CLAZZ, MongoConstant.getFaultDetailCollectionName(gatewaySn, createTime));
    }

    /**
     * 查询报表
     *
     * @param query     Query
     * @param gatewaySn 网关序号
     * @param year      年
     * @return List ProtocolVo
     */
    public List<ProtocolVo> findReport(Query query, int gatewaySn, int year) {
        return mongoTemp.find(query, CLAZZ, MongoConstant.getFaultDetailCollectionName(gatewaySn, year));
    }

}
