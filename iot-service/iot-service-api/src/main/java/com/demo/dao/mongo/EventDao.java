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

import java.util.List;


/**
 * <h1>事件</h1>
 *
 * <p>
 * createDate 2023/11/13 17:23:16
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class EventDao extends DaoBase {

    private static final Class<ProtocolVo> CLAZZ = ProtocolVo.class;
    private final MongoTemp mongoTemp;

    private String getCollectionName(ProtocolVo base) {
        return base.getGatewaySn() + MongoConstant.EVENT + "_" + DateUtils.getYyyyMm(base.getCreateTime().getTime());
    }

    /**
     * 插入
     *
     * @param base ProtocolVo(自动填充id/createTime)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo base) {
        base.setId(Id.next());
        base.setCreateTime(Clock.timestamp());
        // [网关序号]_event_[yyyyMM]
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(base, getCollectionName(base)));
    }

    /**
     * 插入报表
     *
     * @param base           ProtocolVo
     * @param collectionName 集合名
     * @return ok:T,e:null
     */
    public ProtocolVo insertReport(ProtocolVo base, String collectionName) {
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(base, collectionName));
    }

    /**
     * 查询指定字段的不同值
     *
     * @param query          Query
     * @param collectionName 集合名
     * @return List T
     */
    public <T> List<T> findDistinct(Query query, String collectionName, String fieldName, Class<T> fieldClazz) {
        return mongoTemp.findDistinct(query, CLAZZ, collectionName, fieldName, fieldClazz);
    }

    /**
     * 查询
     *
     * @param query          Query
     * @param collectionName 集合名
     * @return List ProtocolVo
     */
    public List<ProtocolVo> find(Query query, String collectionName) {
        return mongoTemp.find(query, CLAZZ, collectionName);
    }

}
