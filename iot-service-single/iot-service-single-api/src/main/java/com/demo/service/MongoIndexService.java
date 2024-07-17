package com.demo.service;

import com.demo.base.ServiceBase;
import com.demo.constant.MongoConstant;
import com.demo.constant.ReportType;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
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
@Slf4j
@Service
@AllArgsConstructor
public class MongoIndexService extends ServiceBase {

    private final MongoTemplate mongoTemplate;

    private static final Bson COMMAND_CODE = Indexes.ascending("commandCode");
    private static final Bson DEVICE_SN = Indexes.ascending("deviceSn");
    private static final Bson TYPE = Indexes.ascending("type");
    private static final Bson MONTH = Indexes.ascending("month");
    private static final Bson DAY = Indexes.ascending("day");
    private static final Bson HOUR = Indexes.ascending("hour");
    private static final Bson MINUTE = Indexes.ascending("minute");
    private static final IndexOptions UNIQUE = new IndexOptions().unique(true);
    private static final Bson REPORT_EVENT_MONTH = Indexes.compoundIndex(COMMAND_CODE, DEVICE_SN, MONTH);
    private static final Bson REPORT_EVENT_DAY = Indexes.compoundIndex(COMMAND_CODE, DEVICE_SN, MONTH, DAY);
    private static final Bson REPORT_EVENT_HOUR = Indexes.compoundIndex(COMMAND_CODE, DEVICE_SN, MONTH, DAY, HOUR);
    private static final Bson REPORT_EVENT_MINUTE = Indexes.compoundIndex(COMMAND_CODE, DEVICE_SN, MONTH, DAY, HOUR, MINUTE);

    /**
     * 更新索引(每天的0时1分2秒)
     */
    @Async
    @PostConstruct
    // @Scheduled(cron = "2 * * * * *")
    @Scheduled(cron = "2 1 0 * * *")
    public void updateIndex() {
        Set<String> collectionNameSet = mongoTemplate.getCollectionNames();
        for (String collectionName : collectionNameSet) {
            MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
            if (collectionName.contains(MongoConstant.INTERACT) ||
                    collectionName.contains(MongoConstant.FAULT) ||
                    collectionName.contains(MongoConstant.FAULT_DETAIL)
            ) {
                // 互动 故障 故障详情
                collection.createIndex(COMMAND_CODE);
                collection.createIndex(DEVICE_SN);
            } else if (collectionName.contains(MongoConstant.EVENT)) {
                // 事件
                collection.createIndex(COMMAND_CODE);
                collection.createIndex(DEVICE_SN);
                reportEventIndex(collectionName, collection);
            }
        }
    }

    /**
     * 事件报表新增索引
     *
     * @param collectionName 集合名
     * @param collection     MongoCollection Document
     */
    private void reportEventIndex(String collectionName, MongoCollection<Document> collection) {
        if (collectionName.contains(MongoConstant.MONTH)) {
            // 月报表
            collection.createIndex(MONTH);
            try {
                collection.createIndex(REPORT_EVENT_MONTH, UNIQUE);
            } catch (Exception e) {
                log.error("事件月报表唯一索引创建错误！已执行查找并删除重复数据并重建索引。", e);
                findAndDeleteDuplicateReport(collection, ReportType.MONTH);
                collection.createIndex(REPORT_EVENT_MONTH, UNIQUE);
            }
        } else if (collectionName.contains(MongoConstant.DAY)) {
            // 日报表
            collection.createIndex(MONTH);
            collection.createIndex(DAY);
            try {
                collection.createIndex(REPORT_EVENT_DAY, UNIQUE);
            } catch (Exception e) {
                log.error("事件日报表唯一索引创建错误！已执行查找并删除重复数据并重建索引。", e);
                findAndDeleteDuplicateReport(collection, ReportType.DAY);
                collection.createIndex(REPORT_EVENT_DAY, UNIQUE);
            }
        } else if (collectionName.contains(MongoConstant.HOUR)) {
            // 小时报表
            collection.createIndex(MONTH);
            collection.createIndex(DAY);
            collection.createIndex(HOUR);
            try {
                collection.createIndex(REPORT_EVENT_HOUR, UNIQUE);
            } catch (Exception e) {
                log.error("事件小时报表唯一索引创建错误！已执行查找并删除重复数据并重建索引。", e);
                findAndDeleteDuplicateReport(collection, ReportType.HOUR);
                collection.createIndex(REPORT_EVENT_HOUR, UNIQUE);
            }
        } else if (collectionName.contains(MongoConstant.MINUTE)) {
            // 分钟报表
            collection.createIndex(MONTH);
            collection.createIndex(DAY);
            collection.createIndex(HOUR);
            collection.createIndex(MINUTE);
            try {
                collection.createIndex(REPORT_EVENT_MINUTE, UNIQUE);
            } catch (Exception e) {
                log.error("事件分钟报表唯一索引创建错误！已执行查找并删除重复数据并重建索引。", e);
                findAndDeleteDuplicateReport(collection, ReportType.MINUTE);
                collection.createIndex(REPORT_EVENT_MINUTE, UNIQUE);
            }
        }
    }

    private static final Document GROUP_REPORT_MONTH = Document.parse("""
            {
                $group: {
                    _id: {
                        commandCode: "$commandCode",
                        deviceSn: "$deviceSn",
                        month: "$month"
                    },
                    count: {
                        $sum: 1
                    },
                    row: {
                        $addToSet: "$$ROOT"
                    }
                }
            }
            """);
    private static final Document GROUP_REPORT_DAY = Document.parse("""
            {
                $group: {
                    _id: {
                        commandCode: "$commandCode",
                        deviceSn: "$deviceSn",
                        month: "$month",
                        day: "$day"
                    },
                    count: {
                        $sum: 1
                    },
                    row: {
                        $addToSet: "$_id"
                    }
                }
            }
            """);
    private static final Document GROUP_REPORT_HOUR = Document.parse("""
            {
                $group: {
                    _id: {
                        commandCode: "$commandCode",
                        deviceSn: "$deviceSn",
                        month: "$month",
                        day: "$day",
                        hour: "$hour"
                    },
                    count: {
                        $sum: 1
                    },
                    row: {
                        $addToSet: "$$ROOT"
                    }
                }
            }
            """);
    private static final Document GROUP_REPORT_MINUTE = Document.parse("""
            {
                $group: {
                    _id: {
                        commandCode: "$commandCode",
                        deviceSn: "$deviceSn",
                        month: "$month",
                        day: "$day",
                        minute: "$minute"
                    },
                    count: {
                        $sum: 1
                    },
                    row: {
                        $addToSet: "$$ROOT"
                    }
                }
            }
            """);
    private static final Document MATCH = Document.parse("""
            {
                $match: {
                    count: {
                        $gt: 1
                    }
                }
            }
            """);
    private static final List<Document> DUPLICATE_REPORT_MONTH = List.of(GROUP_REPORT_MONTH, MATCH);
    private static final List<Document> DUPLICATE_REPORT_DAY = List.of(GROUP_REPORT_DAY, MATCH);
    private static final List<Document> DUPLICATE_REPORT_HOUR = List.of(GROUP_REPORT_HOUR, MATCH);
    private static final List<Document> DUPLICATE_REPORT_MINUTE = List.of(GROUP_REPORT_MINUTE, MATCH);

    /**
     * 报表查找并删除重复数据
     *
     * @param collection MongoCollection Document
     * @param reportType 报表类型
     */
    private void findAndDeleteDuplicateReport(MongoCollection<Document> collection, ReportType reportType) {
        List<Document> pipeline;
        switch (reportType) {
            case MONTH -> pipeline = DUPLICATE_REPORT_MONTH;
            case DAY -> pipeline = DUPLICATE_REPORT_DAY;
            case HOUR -> pipeline = DUPLICATE_REPORT_HOUR;
            case MINUTE -> pipeline = DUPLICATE_REPORT_MINUTE;
            default -> {
                return;
            }
        }
        findAndDeleteDuplicate(collection, pipeline);
    }

    /**
     * 查找并删除重复数据
     *
     * @param collection MongoCollection Document
     * @param pipeline   pipeline
     */
    private void findAndDeleteDuplicate(MongoCollection<Document> collection, List<Document> pipeline) {
        AggregateIterable<Document> aggregate = collection.aggregate(pipeline);
        for (Document document : aggregate) {
            List<Document> row = (List<Document>) document.get("row");
            for (int i = 0; i < row.size() - 1; i++) {
                Document delete = new Document();
                delete.append("_id", row.get(i).get("_id"));
                collection.deleteMany(delete);
            }
        }
    }

}
