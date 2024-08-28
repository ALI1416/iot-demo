package com.demo.entity.protocol;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.demo.constant.ProtocolType;
import com.demo.base.MongoEntityBase;
import com.demo.base.ToStringBase;
import com.demo.constant.InteractType;
import com.demo.entity.vo.ProtocolVo;
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
import java.util.function.Function;

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
     * 交互类型
     *
     * @see InteractType
     */
    @Schema(description = "交互类型")
    private Integer type;
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
     * 交互-错误代码
     */
    @Schema(description = "交互-错误代码")
    private Integer errorCode;
    /**
     * 故障详情-分组号
     */
    @Schema(description = "故障详情-分组号")
    private Integer groupNumber;
    /**
     * 故障详情-比特位
     */
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
    @Schema(description = "数据", name = "Protocol.Data")
    public interface Data extends Serializable {

        /**
         * 数据检查未通过
         *
         * @return 数据检查是否通过
         */
        boolean dataCheckNotPass();

    }

    /**
     * 默认数据
     */
    @Schema(description = "默认数据", name = "Protocol.DefaultData")
    public static class DefaultData extends ToStringBase implements Data {

        /**
         * 数据检查通过
         *
         * @return 通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return false;
        }

    }

    /**
     * 报表处理
     */
    @Schema(description = "报表处理", name = "Protocol.ReportHandle")
    public interface ReportHandle {

        /**
         * 分钟报表处理
         *
         * @param list 全部数据
         * @return 分钟报表
         */
        ProtocolVo minute(List<ProtocolVo> list);

        /**
         * 小时报表处理
         *
         * @param list 分钟报表
         * @return 小时报表
         */
        ProtocolVo hour(List<ProtocolVo> list);

        /**
         * 日报表处理
         *
         * @param list 小时报表
         * @return 日报表
         */
        ProtocolVo day(List<ProtocolVo> list);

        /**
         * 月报表处理
         *
         * @param list 日报表
         * @return 月报表
         */
        ProtocolVo month(List<ProtocolVo> list);

    }

    /**
     * 默认报表处理
     */
    @Schema(description = "默认报表处理", name = "Protocol.DefaultReportHandle")
    public static class DefaultReportHandle implements ReportHandle {

        /**
         * 分钟报表处理
         *
         * @param list 全部数据
         * @return 分钟报表
         */
        @Override
        public ProtocolVo minute(List<ProtocolVo> list) {
            return null;
        }

        /**
         * 小时报表处理
         *
         * @param list 分钟报表
         * @return 小时报表
         */
        @Override
        public ProtocolVo hour(List<ProtocolVo> list) {
            return null;
        }

        /**
         * 日报表处理
         *
         * @param list 小时报表
         * @return 日报表
         */
        @Override
        public ProtocolVo day(List<ProtocolVo> list) {
            return null;
        }

        /**
         * 月报表处理
         *
         * @param list 日报表
         * @return 月报表
         */
        @Override
        public ProtocolVo month(List<ProtocolVo> list) {
            return null;
        }

    }

    /**
     * 故障
     */
    @Schema(description = "故障", name = "Protocol.Fault")
    public interface Fault {

        /**
         * 故障数组
         *
         * @return 故障数组
         */
        ProtocolInfo.FaultInfo[] fault();

    }

    /**
     * 默认故障
     */
    @Schema(description = "默认故障", name = "Protocol.DefaultFault")
    public static class DefaultFault implements Fault {

        /**
         * 故障数组
         *
         * @return 故障数组
         */
        @Override
        public ProtocolInfo.FaultInfo[] fault() {
            return new ProtocolInfo.FaultInfo[0];
        }

    }

    /**
     * 故障详情
     */
    @Getter
    @Setter
    @Schema(description = "故障详情", name = "Protocol.FaultDetail")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaultDetail extends ToStringBase {

        /**
         * 分组号
         */
        @Schema(description = "分组号")
        private Integer groupNumber;
        /**
         * 比特位
         */
        @Schema(description = "比特位")
        private Integer bits;

    }

    /**
     * 事件转换
     *
     * @param commandCode 命令代码
     * @param event       事件
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> eventConvert(int commandCode, JSONObject event) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.EVENT
                || (info.getEvent() == null) != (event == null)
        ) {
            return null;
        }
        Data data = null;
        if (event != null) {
            try {
                data = event.to(info.getEventClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType().getCode(), data);
    }

    /**
     * 请求转换
     *
     * @param commandCode 命令代码
     * @param request     请求
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> requestConvert(int commandCode, JSONObject request) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.INTERACT
                || (info.getRequest() == null) != (request == null)
        ) {
            return null;
        }
        Data data = null;
        if (request != null) {
            try {
                data = request.to(info.getRequestClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType().getCode(), data);
    }

    /**
     * 响应转换
     *
     * @param commandCode 命令代码
     * @param response    响应
     * @return 设备类型, Protocol.Data<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, Data> responseConvert(int commandCode, JSONObject response) {
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.INTERACT
                || (info.getResponse() == null) != (response == null)
        ) {
            return null;
        }
        Data data = null;
        if (response != null) {
            try {
                data = response.to(info.getResponseClass());
            } catch (Exception ignored) {
                return null;
            }
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType().getCode(), data);
    }

    /**
     * 故障转换
     *
     * @param commandCode 命令代码
     * @param fault       故障
     * @return 设备类型, 故障列表<br>
     * 协议格式错误或转换失败返回null
     */
    public static Map.Entry<Integer, List<Integer>> faultConvert(int commandCode, JSONArray fault) {
        if (fault == null || fault.isEmpty()) {
            return null;
        }
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.FAULT
        ) {
            return null;
        }
        List<Integer> faultList;
        try {
            faultList = fault.toList(Integer.class);
        } catch (Exception ignored) {
            return null;
        }
        if (info.getFault().length != faultList.size()) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(info.getDeviceType().getCode(), faultList);
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

    /**
     * 获取事件分钟报表
     *
     * @param commandCode 命令代码
     * @param list        全部数据
     * @return 分钟报表
     */
    public static ProtocolVo getEventReportMinute(int commandCode, List<ProtocolVo> list) {
        if (list == null) {
            return null;
        }
        ProtocolInfo info = ProtocolInfo.get(commandCode);
        if (info == null
                || info.getType() != ProtocolType.EVENT
        ) {
            return null;
        }
        Function<List<ProtocolVo>, ProtocolVo> function = info.getEventMinuteFunction();
        if (function == null) {
            return null;
        }
        return function.apply(list);
    }

}
