package com.demo.service;

import cn.z.util.TimestampUtils;
import com.demo.base.ServiceBase;
import com.demo.dao.mongo.FaultDetailDao;
import com.demo.entity.pojo.GlobalException;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>故障详情</h1>
 *
 * <p>
 * createDate 2023/12/11 15:37:49
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class FaultDetailService extends ServiceBase {

    private final FaultDetailDao faultDetailDao;

    /**
     * 查询2分钟内最后一条
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @param groupNumber 分组号
     * @param bits        比特位
     * @param createTime  创建时间
     * @return ProtocolVo
     */
    public ProtocolVo findLastOne(int gatewaySn, int deviceSn, int commandCode, int groupNumber, int bits, Timestamp createTime) {
        Criteria criteria = new Criteria();
        criteria.and("deviceSn").is(deviceSn);
        criteria.and("commandCode").is(commandCode);
        criteria.and("groupNumber").is(groupNumber);
        criteria.and("bits").is(bits);
        criteria.and("updateTime").gte(new Timestamp(createTime.getTime() - 2 * TimestampUtils.MILLS_OF_MINUTE));
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Query query = Query.query(criteria).with(sort);
        return faultDetailDao.findOne(query, gatewaySn, createTime);
    }

    /**
     * 查询报表(时间段跨度不可超过1年)
     *
     * @param gatewaySn     网关序号
     * @param createTime    创建时间(包含)
     * @param createTimeEnd 创建时间结束(包含)
     * @return List ProtocolVo
     */
    public List<ProtocolVo> findReport(int gatewaySn, Timestamp createTime, Timestamp createTimeEnd) {
        int year = DateUtils.getYear(createTime);
        int yearEnd = DateUtils.getYear(createTimeEnd);
        int yearDiff = yearEnd - year;
        if (yearDiff == 0) {
            // 未跨年
            Query query = Query.query(Criteria.where("createTime").gte(createTime).lte(createTimeEnd));
            return faultDetailDao.findReport(query, gatewaySn, year);
        }
        if (yearDiff == 1) {
            // 跨1年
            Query query = Query.query(Criteria.where("createTime").gte(createTime));
            List<ProtocolVo> list = new ArrayList<>(faultDetailDao.findReport(query, gatewaySn, year));
            Query queryEnd = Query.query(Criteria.where("createTime").lte(createTimeEnd));
            list.addAll(faultDetailDao.findReport(queryEnd, gatewaySn, yearEnd));
            return list;
        } else {
            throw new GlobalException("时间段跨度不可超过1年！");
        }
    }

}
