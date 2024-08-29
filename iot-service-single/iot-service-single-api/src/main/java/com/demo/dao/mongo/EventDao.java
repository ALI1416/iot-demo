package com.demo.dao.mongo;

import cn.z.id.Id;
import cn.z.mongo.MongoTemp;
import cn.z.mongo.entity.Page;
import cn.z.mongo.entity.Pageable;
import com.demo.base.DaoBase;
import com.demo.constant.MongoConstant;
import com.demo.constant.ReportType;
import com.demo.entity.vo.ProtocolVo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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
                MongoConstant.getEventCollectionName(protocol.getGatewaySn(), protocol.getCreateTime())
        ));
    }

    /**
     * 查询(倒序)
     *
     * @param gatewaySn       网关序号
     * @param deviceSn        设备序号
     * @param commandCode     命令代码
     * @param year            年
     * @param month           月
     * @param createTimeStart 起始时间(包含)
     * @param createTimeEnd   结束时间(不包含)
     * @return List ProtocolVo
     */
    public List<ProtocolVo> find(int gatewaySn, Integer deviceSn, Integer commandCode, int year, int month, Timestamp createTimeStart, Timestamp createTimeEnd) {
        Criteria criteria = new Criteria();
        criteria.and("createTime").gte(createTimeStart).lt(createTimeEnd);
        if (deviceSn != null) {
            criteria.and("deviceSn").is(deviceSn);
        }
        if (commandCode != null) {
            criteria.and("commandCode").is(commandCode);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Query query = Query.query(criteria).with(sort);
        return mongoTemp.find(query, CLAZZ, MongoConstant.getEventCollectionName(gatewaySn, year, month));
    }

    /**
     * 插入报表
     *
     * @param protocol   ProtocolVo
     * @param gatewaySn  网关序号
     * @param year       年
     * @param reportType 报表类型
     */
    public void insertReport(ProtocolVo protocol, int gatewaySn, int year, ReportType reportType) {
        mongoTemp.insert(protocol, MongoConstant.getEventReportCollectionName(gatewaySn, year, reportType));
    }

    /**
     * 构造Query
     *
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @return Query
     */
    public static Query buildQuery(Integer deviceSn, Integer commandCode, Integer month, Integer day, Integer hour, Integer minute) {
        Criteria criteria = new Criteria();
        if (deviceSn != null) {
            criteria.and("deviceSn").is(deviceSn);
        }
        if (commandCode != null) {
            criteria.and("commandCode").is(commandCode);
        }
        if (month != null) {
            criteria.and("month").is(month);
            if (day != null) {
                criteria.and("day").is(day);
                if (hour != null) {
                    criteria.and("hour").is(hour);
                    if (minute != null) {
                        criteria.and("minute").is(minute);
                    }
                }
            }
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return Query.query(criteria).with(sort);
    }

    /**
     * 查询报表(倒序)
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @param reportType  报表类型
     * @return List ProtocolVo
     */
    public List<ProtocolVo> findReport(int gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour, Integer minute, ReportType reportType) {
        Query query = buildQuery(deviceSn, commandCode, month, day, hour, minute);
        return mongoTemp.find(query, CLAZZ, MongoConstant.getEventReportCollectionName(gatewaySn, year, reportType));
    }

    /**
     * 删除报表
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @param reportType  报表类型
     * @return 删除条数
     */
    public long deleteReport(int gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour, Integer minute, ReportType reportType) {
        Query query = buildQuery(deviceSn, commandCode, month, day, hour, minute);
        return mongoTemp.delete(query, MongoConstant.getEventReportCollectionName(gatewaySn, year, reportType));
    }

    /**
     * 分页查询报表
     *
     * @param query      Query
     * @param pageable   Pageable
     * @param gatewaySn  网关序号
     * @param year       年
     * @param reportType 报表类型
     * @return Page ProtocolVo
     */
    public Page<ProtocolVo> findReportPage(Query query, int gatewaySn, int year, ReportType reportType, Pageable pageable) {
        return mongoTemp.findPage(query, pageable, CLAZZ, MongoConstant.getEventReportCollectionName(gatewaySn, year, reportType));
    }

}
