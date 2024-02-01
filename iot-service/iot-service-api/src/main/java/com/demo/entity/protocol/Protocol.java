package com.demo.entity.protocol;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.demo.announce.ProtocolType;
import com.demo.base.MongoEntityBase;
import com.demo.base.ToStringBase;
import com.demo.util.BitUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>协议</h1>
 *
 * <p>
 * createDate 2023/11/10 17:44:43
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "协议")
public class Protocol extends MongoEntityBase {

    /**
     * token
     */
    @Schema(description = "token")
    private String token;
    /**
     * 网关序号
     */
    @Transient
    @Schema(description = "网关序号")
    private Integer gatewaySn;
    /**
     * 设备序号
     */
    @Indexed
    @Schema(description = "设备序号")
    private Integer deviceSn;
    /**
     * 命令代码
     */
    @Indexed
    @Schema(description = "命令代码")
    private Integer commandCode;
    /**
     * 事件
     */
    @Schema(description = "事件")
    private Data event;
    /**
     * 故障列表
     */
    @Schema(description = "故障列表")
    private List<Integer> faultList;
    /**
     * 故障详情列表
     */
    @Schema(description = "故障详情列表")
    private List<FaultDetail> faultDetailList;
    /**
     * 请求
     */
    @Schema(description = "请求")
    private Data request;
    /**
     * 响应
     */
    @Schema(description = "响应")
    private Data response;
    /**
     * 错误代码
     */
    @Schema(description = "错误代码")
    private Integer errorCode;
    /**
     * 故障详情-分组号
     */
    @Indexed
    @Schema(description = "故障详情-分组号")
    private Integer groupNumber;
    /**
     * 故障详情-比特位
     */
    @Indexed
    @Schema(description = "故障详情-比特位")
    private Integer bits;

    /**
     * 报表-年
     */
    @Transient
    @Schema(description = "报表-年")
    private Integer year;
    /**
     * 报表-月
     */
    @Schema(description = "报表-月")
    private Integer month;
    /**
     * 报表-日
     */
    @Schema(description = "报表-日")
    private Integer day;
    /**
     * 报表-小时
     */
    @Schema(description = "报表-小时")
    private Integer hour;
    /**
     * 报表-分钟
     */
    @Schema(description = "报表-分钟")
    private Integer minute;

    /**
     * 数据
     */
    @Schema(description = "数据")
    public interface Data extends Serializable {

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        boolean dataCheckNotPass();

    }

    /**
     * 没有数据
     */
    @Schema(description = "没有数据")
    public static class NoData implements Data {

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return false;
        }

    }

    /**
     * 故障详情
     */
    @Getter
    @Setter
    @Schema(description = "故障详情")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaultDetail extends ToStringBase {

        /**
         * 分组号
         */
        @Indexed
        @Schema(description = "分组号")
        private Integer groupNumber;
        /**
         * 比特位
         */
        @Indexed
        @Schema(description = "比特位")
        private Integer bits;

    }

    /**
     * 事件转换
     *
     * @param commandCode 命令代码
     * @param eventJson   事件JSON
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> eventConvert(int commandCode, String eventJson) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.EVENT.getCode()
                || (info.getEventMap() == null) != (eventJson == null)
        ) {
            return null;
        }
        Data data = null;
        if (eventJson != null) {
            try {
                data = JSON.parseObject(eventJson, info.getEventClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType(), data);
    }

    /**
     * 请求转换
     *
     * @param commandCode 命令代码
     * @param requestJson 请求JSON
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> requestConvert(int commandCode, JSONObject requestJson) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.INTERACT.getCode()
                || (info.getRequestMap() == null) != (requestJson == null)
        ) {
            return null;
        }
        Data data = null;
        if (requestJson != null) {
            try {
                data = requestJson.to(info.getRequestClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType(), data);
    }

    /**
     * 响应转换
     *
     * @param commandCode  命令代码
     * @param responseJson 响应JSON
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> responseConvert(int commandCode, String responseJson) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.INTERACT.getCode()
                || (info.getResponseMap() == null) != (responseJson == null)
        ) {
            return null;
        }
        Data data = null;
        if (responseJson != null) {
            try {
                data = JSON.parseObject(responseJson, info.getResponseClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType(), data);
    }

    /**
     * 故障转换
     *
     * @param commandCode 命令代码
     * @param faultArray  故障数组
     * @return 设备类型, List Integer<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, List<Integer>> faultConvert(int commandCode, String faultArray) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.FAULT.getCode()
        ) {
            return null;
        }
        List<Integer> faultList;
        try {
            faultList = JSON.parseArray(faultArray, Integer.class);
        } catch (Exception ignored) {
            return null;
        }
        if (info.getFaultMap().size() != faultList.size()) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType(), faultList);
    }

    /**
     * 故障详情转换
     *
     * @param faultList 故障列表
     * @return List Protocol.FaultDetail<br>
     * 没有故障返回空数组
     */
    public static List<FaultDetail> faultDetailConvert(List<Integer> faultList) {
        List<FaultDetail> faultDetailList = new ArrayList<>();
        for (int i = 0; i < faultList.size(); i++) {
            if (faultList.get(i) == 0) {
                continue;
            }
            boolean[] bits = BitUtils.getBits(faultList.get(i));
            for (int j = 0; j < bits.length; j++) {
                if (bits[j]) {
                    faultDetailList.add(new FaultDetail(i, j));
                }
            }
        }
        return faultDetailList;
    }

}
