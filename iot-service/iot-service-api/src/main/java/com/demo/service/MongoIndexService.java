package com.demo.service;

import cn.z.mongo.MongoTemp;
import com.demo.base.ServiceBase;
import com.demo.constant.MongoConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * <h1>MongoDB索引</h1>
 *
 * <p>
 * createDate 2023/12/07 13:59:54
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@AllArgsConstructor
public class MongoIndexService extends ServiceBase {

    private final MongoTemp mongoTemp;

    private static final IndexDefinition CREATE_ID = new Index("createId", Sort.Direction.ASC);
    private static final IndexDefinition COMMAND_CODE = new Index("commandCode", Sort.Direction.ASC);
    private static final IndexDefinition DEVICE_SN = new Index("deviceSn", Sort.Direction.ASC);
    private static final IndexDefinition MONTH = new Index("month", Sort.Direction.ASC);
    private static final IndexDefinition DAY = new Index("day", Sort.Direction.ASC);
    private static final IndexDefinition HOUR = new Index("hour", Sort.Direction.ASC);
    private static final IndexDefinition MINUTE = new Index("minute", Sort.Direction.ASC);
    private static final IndexDefinition GROUP_NUMBER = new Index("groupNumber", Sort.Direction.ASC);
    private static final IndexDefinition BITS = new Index("bits", Sort.Direction.ASC);

    /**
     * 更新索引(每天的0时1分2秒)
     */
    @Async
    @PostConstruct
    // @Scheduled(cron = "2 * * * * *")
    @Scheduled(cron = "2 1 0 * * *")
    public void updateIndex() {
        Set<String> collectionNameSet = mongoTemp.getCollectionName();
        for (String collectionName : collectionNameSet) {
            if (collectionName.contains(MongoConstant.INTERACT)) {
                // 互动
                mongoTemp.addCollectionIndex(collectionName, CREATE_ID);
                mongoTemp.addCollectionIndex(collectionName, COMMAND_CODE);
                mongoTemp.addCollectionIndex(collectionName, DEVICE_SN);
            } else if (collectionName.contains(MongoConstant.FAULT)) {
                // 故障
                mongoTemp.addCollectionIndex(collectionName, COMMAND_CODE);
                mongoTemp.addCollectionIndex(collectionName, DEVICE_SN);
            } else if (collectionName.contains(MongoConstant.FAULT_DETAIL)) {
                // 故障详情
                mongoTemp.addCollectionIndex(collectionName, COMMAND_CODE);
                mongoTemp.addCollectionIndex(collectionName, DEVICE_SN);
                mongoTemp.addCollectionIndex(collectionName, GROUP_NUMBER);
                mongoTemp.addCollectionIndex(collectionName, BITS);
            } else if (collectionName.contains(MongoConstant.EVENT)) {
                // 事件
                mongoTemp.addCollectionIndex(collectionName, COMMAND_CODE);
                mongoTemp.addCollectionIndex(collectionName, DEVICE_SN);
                if (collectionName.contains(MongoConstant.MONTH)) {
                    // 月报表
                    mongoTemp.addCollectionIndex(collectionName, MONTH);
                } else if (collectionName.contains(MongoConstant.DAY)) {
                    // 日报表
                    mongoTemp.addCollectionIndex(collectionName, MONTH);
                    mongoTemp.addCollectionIndex(collectionName, DAY);
                } else if (collectionName.contains(MongoConstant.HOUR)) {
                    // 小时报表
                    mongoTemp.addCollectionIndex(collectionName, MONTH);
                    mongoTemp.addCollectionIndex(collectionName, DAY);
                    mongoTemp.addCollectionIndex(collectionName, HOUR);
                } else if (collectionName.contains(MongoConstant.MINUTE)) {
                    // 分钟报表
                    mongoTemp.addCollectionIndex(collectionName, MONTH);
                    mongoTemp.addCollectionIndex(collectionName, DAY);
                    mongoTemp.addCollectionIndex(collectionName, HOUR);
                    mongoTemp.addCollectionIndex(collectionName, MINUTE);
                }
            }
        }
        // 登录日志
        mongoTemp.addCollectionIndex("login_log", CREATE_ID);
    }

}
