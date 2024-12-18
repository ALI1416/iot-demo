package com.demo.service;

import cn.z.mongo.entity.Page;
import cn.z.mongo.entity.Pageable;
import cn.z.mqtt.MqttTemp;
import com.alibaba.fastjson2.JSON;
import com.demo.base.ServiceBase;
import com.demo.constant.ErrorCode;
import com.demo.constant.MqttConstant;
import com.demo.dao.mongo.InteractDao;
import com.demo.entity.protocol.Frame;
import com.demo.entity.vo.ProtocolVo;
import com.demo.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <h1>交互</h1>
 *
 * <p>
 * createDate 2023/11/14 14:59:06
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Service
@Slf4j
@AllArgsConstructor
public class InteractService extends ServiceBase {

    private final InteractDao interactDao;
    private final MqttTemp mqttTemp;

    /**
     * 插入并发送
     *
     * @param protocol ProtocolVo
     * @return ok:T,e:null
     */
    public ProtocolVo insertAndSend(ProtocolVo protocol) {
        // 插入
        ProtocolVo result = interactDao.insert(protocol);
        if (result != null) {
            // 发送到嵌入式
            Frame.Request request = new Frame.Request();
            request.setRequestSn(result.getId());
            request.setRequest(protocol.getRequest());
            mqttTemp.send(
                    MqttConstant.getRequestTopic(protocol.getGatewaySn(), protocol.getDeviceSn(), protocol.getCommandCode()),
                    JSON.toJSONBytes(request)
            );
        }
        return result;
    }

    /**
     * 分页查询
     *
     * @param protocol ProtocolVo
     * @return Page ProtocolVo
     */
    public Page<ProtocolVo> findPage(ProtocolVo protocol) {
        Query query = Query.query(buildCriteria(protocol));
        Pageable pageable = buildPage(protocol.getPages(), protocol.getRows(), protocol.getOrderBy());
        return interactDao.findPage(query, protocol.getGatewaySn(), protocol.getYear(), pageable);
    }

    /**
     * 构造Criteria
     *
     * @param protocol ProtocolVo
     * @return Criteria
     */
    public static Criteria buildCriteria(ProtocolVo protocol) {
        Criteria criteria = new Criteria();
        if (protocol.getDeviceSn() != null) {
            criteria.and("deviceSn").is(protocol.getDeviceSn());
        }
        if (protocol.getCommandCode() != null) {
            criteria.and("commandCode").is(protocol.getCommandCode());
        }
        if (protocol.getType() != null) {
            criteria.and("type").is(protocol.getType());
        }
        if (protocol.getErrorCode() != null) {
            criteria.and("errorCode").is(protocol.getErrorCode());
        }
        return criteria;
    }

    /**
     * 获取最后一条成功消息
     *
     * @param gatewaySn   网关序号
     * @param deviceSn    设备序号
     * @param commandCode 命令代码
     * @return ProtocolVo
     */
    public ProtocolVo findLast(int gatewaySn, int deviceSn, int commandCode) {
        ProtocolVo protocol = new ProtocolVo();
        protocol.setPages(1);
        protocol.setRows(1);
        protocol.setOrderBy("id desc");
        protocol.setGatewaySn(gatewaySn);
        protocol.setDeviceSn(deviceSn);
        protocol.setCommandCode(commandCode);
        protocol.setErrorCode(ErrorCode.NO_ERROR.getCode());
        protocol.setYear(DateUtils.getYear());
        List<ProtocolVo> list = findPage(protocol).getList();
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
