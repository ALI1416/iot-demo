package com.demo.dao.mongo;

import cn.z.clock.Clock;
import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import cn.z.mongo.entity.Page;
import cn.z.mongo.entity.Pageable;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Query;
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

    /**
     * 插入
     *
     * @param protocol ProtocolVo(自动填充id, createTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo protocol) {
        protocol.setId(Id.next());
        protocol.setCreateTime(Id.newTimestamp(protocol.getId()));
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(protocol,
                MongoConstant.getInteractCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

    /**
     * 插入2
     *
     * @param protocol ProtocolVo(自动填充id, updateTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo insert2(ProtocolVo protocol) {
        protocol.setId(Id.next());
        protocol.setUpdateTime(Clock.timestamp());
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(protocol,
                MongoConstant.getInteractCollectionName(protocol.getGatewaySn(), protocol.getUpdateTime())
        ));
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
     * @param protocol ProtocolVo(自动填充updateTime 必须gatewaySn)
     * @return ok:T,e:null
     */
    public ProtocolVo save(ProtocolVo protocol) {
        protocol.setUpdateTime(Clock.timestamp());
        return tryAnyNoTransactionReturnT(() -> mongoTemp.save(protocol,
                MongoConstant.getInteractCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

    /**
     * 分页查询
     *
     * @param query     Query
     * @param pageable  Pageable
     * @param gatewaySn 网关序号
     * @param year      年
     * @return Page ProtocolVo
     */
    public Page<ProtocolVo> findPage(Query query, int gatewaySn, int year, Pageable pageable) {
        return mongoTemp.findPage(query, pageable, CLAZZ, MongoConstant.getInteractCollectionName(gatewaySn, year));
    }

}
