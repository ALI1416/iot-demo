package com.demo.dao.mongo;

import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * <h1>交流</h1>
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
public class CommunicationDao extends DaoBase {

    private final MongoTemp mongoTemp;

    /**
     * 插入
     *
     * @param protocol ProtocolVo(自动填充id, createTime)
     * @return ok:T,e:null
     */
    public ProtocolVo insert(ProtocolVo protocol) {
        protocol.setId(Id.next());
        protocol.setCreateTime(Id.newTimestamp(protocol.getId()));
        return tryAnyNoTransactionReturnT(() -> mongoTemp.insert(protocol,
                MongoConstant.getCommunicationCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

}
