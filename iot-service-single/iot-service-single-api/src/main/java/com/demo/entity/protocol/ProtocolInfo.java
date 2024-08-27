package com.demo.entity.protocol;

import cn.z.tool.ClassScanner;
import com.alibaba.fastjson2.annotation.JSONField;
import com.demo.announce.*;
import com.demo.base.ToStringBase;
import com.demo.entity.pojo.GlobalException;
import com.demo.entity.vo.ProtocolVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

/**
 * <h1>协议信息</h1>
 *
 * <p>
 * createDate 2023/12/13 14:56:17
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "协议信息")
public class ProtocolInfo extends ToStringBase {

    /**
     * 命令代码
     */
    @Schema(description = "命令代码")
    private Integer commandCode;
    /**
     * 命令名
     */
    @Schema(description = "命令名")
    private String name;
    /**
     * 协议类型
     */
    @Schema(description = "协议类型")
    private ProtocolType type;
    /**
     * 设备类型
     */
    @Schema(description = "设备类型")
    private DeviceType deviceType;
    /**
     * 特殊处理
     */
    @Schema(description = "特殊处理")
    private Boolean special;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String comment;

    /**
     * 事件
     */
    @Schema(description = "事件")
    private List<FieldInfo> event;
    /**
     * 事件分钟报表
     */
    @Schema(description = "事件分钟报表")
    private List<FieldInfo> eventMinute;
    /**
     * 事件小时报表
     */
    @Schema(description = "事件小时报表")
    private List<FieldInfo> eventHour;
    /**
     * 事件日报表
     */
    @Schema(description = "事件日报表")
    private List<FieldInfo> eventDay;
    /**
     * 事件月报表
     */
    @Schema(description = "事件月报表")
    private List<FieldInfo> eventMonth;
    /**
     * 故障
     */
    @Schema(description = "故障")
    private FaultInfo[] fault;
    /**
     * 请求
     */
    @Schema(description = "请求")
    private List<FieldInfo> request;
    /**
     * 响应
     */
    @Schema(description = "响应")
    private List<FieldInfo> response;

    /**
     * 事件类
     */
    @Schema(description = "事件类")
    @JSONField(serialize = false, deserialize = false)
    private Class<? extends Protocol.Data> eventClass;
    /**
     * 事件分钟报表方法
     */
    @Schema(description = "事件分钟报表方法")
    @JSONField(serialize = false, deserialize = false)
    private Function<List<ProtocolVo>, ProtocolVo> eventMinuteFunction;
    /**
     * 事件小时报表方法
     */
    @Schema(description = "事件小时报表方法")
    @JSONField(serialize = false, deserialize = false)
    private Function<List<ProtocolVo>, ProtocolVo> eventHourFunction;
    /**
     * 事件日报表方法
     */
    @Schema(description = "事件日报表方法")
    @JSONField(serialize = false, deserialize = false)
    private Function<List<ProtocolVo>, ProtocolVo> eventDayFunction;
    /**
     * 事件月报表方法
     */
    @Schema(description = "事件月报表方法")
    @JSONField(serialize = false, deserialize = false)
    private Function<List<ProtocolVo>, ProtocolVo> eventMonthFunction;
    /**
     * 请求类
     */
    @Schema(description = "请求类")
    @JSONField(serialize = false, deserialize = false)
    private Class<? extends Protocol.Data> requestClass;
    /**
     * 响应类
     */
    @Schema(description = "响应类")
    @JSONField(serialize = false, deserialize = false)
    private Class<? extends Protocol.Data> responseClass;

    /**
     * 字段信息
     */
    @Getter
    @Setter
    @Schema(description = "字段信息", name = "ProtocolInfo.FieldInfo")
    public static class FieldInfo extends ToStringBase {

        /**
         * 字段键
         */
        @Schema(description = "字段键")
        private String key;
        /**
         * 字段名
         */
        @Schema(description = "字段名")
        private String name;
        /**
         * 字段类型
         */
        @Schema(description = "字段类型")
        private FieldType type;
        /**
         * 字段数据类型
         */
        @Schema(description = "字段数据类型")
        private FieldDataType dataType;
        /**
         * 字段特殊数据类型
         */
        @Schema(description = "字段特殊数据类型")
        private FieldSpecialDataType specialDataType;
        /**
         * 字段单位
         *
         * @see FieldUnit
         */
        @Schema(description = "字段单位")
        private FieldUnit unit;
        /**
         * 推荐字段单位
         *
         * @see FieldUnit
         */
        @Schema(description = "推荐字段单位")
        private FieldUnit recommend;
        /**
         * 推荐字段单位转换-乘以
         */
        @Schema(description = "推荐字段单位转换-乘以")
        private Integer multiply;
        /**
         * 推荐字段单位转换-除以
         */
        @Schema(description = "推荐字段单位转换-除以")
        private Integer divide;
        /**
         * 字段状态
         *
         * @see FieldStatus
         */
        @Schema(description = "字段状态")
        private FieldStatus[] status;
        /**
         * 最大值
         */
        @Schema(description = "最大值")
        private String max;
        /**
         * 最小值
         */
        @Schema(description = "最小值")
        private String min;
        /**
         * 最大字符长度
         */
        @Schema(description = "最大字符长度")
        private Integer maxLength;
        /**
         * 最小字符长度
         */
        @Schema(description = "最小字符长度")
        private Integer minLength;
        /**
         * 特殊处理
         */
        @Schema(description = "特殊处理")
        private Boolean special;
        /**
         * 备注
         */
        @Schema(description = "备注")
        private String comment;

        /**
         * 子信息
         */
        @Schema(description = "子信息")
        private FieldInfo child;
        /**
         * 子信息列表
         */
        @Schema(description = "子信息列表")
        private List<FieldInfo> children;

    }

    /**
     * 故障信息
     */
    @Getter
    @Setter
    @Schema(description = "故障信息", name = "ProtocolInfo.FaultInfo")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FaultInfo extends ToStringBase {

        /**
         * 分组名
         */
        @Schema(description = "分组名")
        private String groupName;
        /**
         * 故障名
         */
        @Schema(description = "故障名")
        private String[] name;

    }

    /**
     * 字段单位
     */
    @Getter
    @Setter
    @Schema(description = "字段单位", name = "ProtocolInfo.FieldUnit")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldUnit extends ToStringBase {

        /**
         * 符号
         */
        @Schema(description = "符号")
        private String symbol;
        /**
         * 名称
         */
        @Schema(description = "名称")
        private String name;

    }

    /**
     * 字段状态
     */
    @Getter
    @Setter
    @Schema(description = "字段状态", name = "ProtocolInfo.FieldStatus")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldStatus extends ToStringBase {

        /**
         * 值
         */
        @Schema(description = "值")
        private String value;
        /**
         * 名称
         */
        @Schema(description = "名称")
        private String name;

    }

    /**
     * 协议表
     */
    private static final List<ProtocolInfo> TABLE = ProtocolInfo.getTable(Protocol.class.getPackage().getName());

    /**
     * 获取协议信息列表
     *
     * @return 协议信息列表
     */
    public static List<ProtocolInfo> getTable() {
        return TABLE;
    }

    /**
     * 获取协议信息
     *
     * @param commandCode 命令代码
     * @return 协议信息
     */
    public static ProtocolInfo get(int commandCode) {
        return TABLE.stream().filter(p -> p.commandCode == commandCode).findFirst().orElse(null);
    }

    /**
     * 获取协议信息列表
     *
     * @param packageName 包名
     * @return 协议信息列表
     */
    private static List<ProtocolInfo> getTable(String packageName) {
        List<ProtocolInfo> list = new ArrayList<>();
        // 包内所有类
        Set<Class<?>> classSet = ClassScanner.getClass(packageName);
        for (Class<?> clazz : classSet) {
            // 含注解的类
            com.demo.announce.Protocol protocol = clazz.getAnnotation(com.demo.announce.Protocol.class);
            if (protocol == null) {
                continue;
            }
            ProtocolInfo protocolInfo = new ProtocolInfo();
            // 命令代码
            protocolInfo.setCommandCode(protocol.code());
            // 命令名
            protocolInfo.setName(protocol.name());
            // 协议类型
            protocolInfo.setType(protocol.type());
            // 设备类型
            protocolInfo.setDeviceType(protocol.deviceType());
            // 特殊处理
            if (protocol.special()) {
                protocolInfo.setSpecial(true);
            }
            // 备注
            if (!protocol.comment().isEmpty()) {
                protocolInfo.setComment(protocol.comment());
            }
            // 事件
            Class<? extends Protocol.Data> eventClass = protocol.event();
            if (eventClass != Protocol.DefaultData.class) {
                protocolInfo.setEvent(getFieldInfo(eventClass));
                protocolInfo.setEventClass(eventClass);
            }
            // 事件分钟报表
            Class<? extends Protocol.Data> eventMinuteClass = protocol.eventMinute();
            if (eventMinuteClass != Protocol.DefaultData.class) {
                protocolInfo.setEventMinute(getFieldInfo(eventMinuteClass));
            }
            // 事件小时报表
            Class<? extends Protocol.Data> eventHourClass = protocol.eventHour();
            if (eventHourClass != Protocol.DefaultData.class) {
                protocolInfo.setEventHour(getFieldInfo(eventHourClass));
            }
            // 事件日报表
            Class<? extends Protocol.Data> eventDayClass = protocol.eventDay();
            if (eventDayClass != Protocol.DefaultData.class) {
                protocolInfo.setEventDay(getFieldInfo(eventDayClass));
            }
            // 事件月报表
            Class<? extends Protocol.Data> eventMonthClass = protocol.eventMonth();
            if (eventMonthClass != Protocol.DefaultData.class) {
                protocolInfo.setEventMonth(getFieldInfo(eventMonthClass));
            }
            // 事件报表处理
            Class<? extends Protocol.EventReportHandle> eventReportHandleClass = protocol.eventReportHandle();
            if (eventReportHandleClass != Protocol.EventReportHandle.class) {
                try {
                    Protocol.EventReportHandle handle = eventReportHandleClass.getConstructor().newInstance();
                    protocolInfo.setEventMinuteFunction(handle::minute);
                    protocolInfo.setEventHourFunction(handle::hour);
                    protocolInfo.setEventDayFunction(handle::day);
                    protocolInfo.setEventMonthFunction(handle::month);
                } catch (Exception ignored) {
                }
            }
            // 故障
            Class<? extends Protocol.Fault> faultClass = protocol.fault();
            if (faultClass != Protocol.DefaultFault.class) {
                try {
                    protocolInfo.setFault(faultClass.getConstructor().newInstance().fault());
                } catch (Exception ignored) {
                }
            }
            // 请求
            Class<? extends Protocol.Data> requestClass = protocol.request();
            if (requestClass != Protocol.DefaultData.class) {
                protocolInfo.setRequest(getFieldInfo(requestClass));
                protocolInfo.setRequestClass(requestClass);
            }
            // 响应
            Class<? extends Protocol.Data> responseClass = protocol.response();
            if (responseClass != Protocol.DefaultData.class) {
                protocolInfo.setResponse(getFieldInfo(responseClass));
                protocolInfo.setResponseClass(responseClass);
            }
            list.add(protocolInfo);
        }
        // 排序
        list.sort(Comparator.comparingInt(v -> v.commandCode));
        return list;
    }

    /**
     * 获取字段信息列表
     *
     * @param clazz Class
     * @return 字段信息列表
     */
    private static List<FieldInfo> getFieldInfo(Class<?> clazz) {
        List<FieldInfo> list = new ArrayList<>(getFieldArrayFieldInfo(clazz.getDeclaredFields()));
        // 父类
        Class<?> superClass = clazz.getSuperclass();
        while (!(superClass == null || superClass == ToStringBase.class || superClass == Object.class)) {
            list.addAll(getFieldArrayFieldInfo(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        return list;
    }

    /**
     * 获取字段数组的字段信息列表
     *
     * @param fieldArray 字段数组
     * @return 字段信息列表
     */
    private static List<FieldInfo> getFieldArrayFieldInfo(java.lang.reflect.Field[] fieldArray) {
        List<FieldInfo> list = new ArrayList<>();
        for (java.lang.reflect.Field field : fieldArray) {
            // 含注解的字段
            Field field1 = field.getAnnotation(Field.class);
            if (field1 == null) {
                continue;
            }
            list.add(getFieldInfo(field.getName(), field.getGenericType(), field1));
        }
        return list;
    }

    /**
     * 获取字段信息
     *
     * @param key   字段键
     * @param type  Type
     * @param field Field
     * @return 字段信息
     */
    private static FieldInfo getFieldInfo(String key, Type type, Field field) {
        FieldInfo fieldInfo = new FieldInfo();
        // 字段键
        fieldInfo.setKey(key);
        // 字段名
        fieldInfo.setName(field.name());
        if (type instanceof ParameterizedType parameterizedType) {
            // 泛型 例如List<Integer>
            if (Collection.class.isAssignableFrom((Class<?>) (parameterizedType.getRawType()))) {
                // 集合类型
                fieldInfo.setDataType(FieldDataType.ARRAY);
                fieldInfo.setChild(getFieldInfo(null, parameterizedType.getActualTypeArguments()[0], field));
            } else {
                // 非集合类型
                throw new GlobalException("字段 [" + key + "] 泛型形参类型 [ " + type + " ] 为 非集合类型");
            }
        } else if (type instanceof GenericArrayType genericArrayType) {
            // 泛型数组 例如List<Integer>[]
            fieldInfo.setDataType(FieldDataType.ARRAY);
            fieldInfo.setChild(getFieldInfo(null, genericArrayType.getGenericComponentType(), field));
        } else if (type instanceof Class<?> clazz) {
            // 普通数组类型、非数组类型 例如int[]、int
            if (clazz.isArray()) {
                // 数组类型
                fieldInfo.setDataType(FieldDataType.ARRAY);
                fieldInfo.setChild(getFieldInfo(null, clazz.getComponentType(), field));
            } else {
                // 非数组类型
                fieldInfo.setDataType(getDataType(clazz));
                if (fieldInfo.getDataType() == FieldDataType.OBJECT) {
                    // 对象类型
                    fieldInfo.setChildren(getFieldInfo(clazz));
                } else {
                    // 普通类型
                    setNormalTypeData(fieldInfo, field);
                }
            }
        } else {
            // 未知类型
            throw new GlobalException("字段 [" + key + "] 类型 [ " + type + " ]为 未知类型");
        }
        return fieldInfo;
    }

    /**
     * 设置普通类型数据
     *
     * @param fieldInfo ProtocolInfo.Data
     * @param field     ProtocolField
     */
    private static void setNormalTypeData(FieldInfo fieldInfo, Field field) {
        // 字段类型
        if (field.type() != FieldType.NULL) {
            fieldInfo.setType(field.type());
        }
        // 字段特殊数据类型
        if (field.specialDataType() != FieldSpecialDataType.DEFAULT) {
            fieldInfo.setSpecialDataType(field.specialDataType());
        }
        // 字段单位
        if (field.unit() != FieldUnitEnum.NONE) {
            fieldInfo.setUnit(field.unit().getFieldUnit());
        }
        // 推荐字段单位
        if (field.recommend() != FieldUnitEnum.NONE) {
            fieldInfo.setRecommend(field.recommend().getFieldUnit());
        }
        // 推荐字段单位转换-乘以
        if (field.multiply() != 1) {
            fieldInfo.setMultiply(field.multiply());
        }
        // 推荐字段单位转换-除以
        if (field.divide() != 1) {
            fieldInfo.setDivide(field.divide());
        }
        // 字段状态
        if (field.status() != FieldStatusEnum.NULL) {
            fieldInfo.setStatus(field.status().getStatus());
        }
        // 最大值
        if (!field.max().isEmpty()) {
            fieldInfo.setMax(field.max());
        }
        // 最小值
        if (!field.min().isEmpty()) {
            fieldInfo.setMin(field.min());
        }
        // 最大字符长度
        if (field.maxLength() != Integer.MAX_VALUE) {
            fieldInfo.setMaxLength(field.maxLength());
        }
        // 最小字符长度
        if (field.minLength() != 0) {
            fieldInfo.setMinLength(field.minLength());
        }
        // 特殊处理
        if (field.special()) {
            fieldInfo.setSpecial(true);
        }
        // 备注
        if (!field.comment().isEmpty()) {
            fieldInfo.setComment(field.comment());
        }
    }

    /**
     * 获取字段数据类型(基本类型、字符串类型、对象类型)
     *
     * @param fieldType 字段类型
     * @return 字段数据类型
     */
    private static FieldDataType getDataType(Class<?> fieldType) {
        switch (fieldType.getName()) {
            case "boolean", "java.lang.Boolean" -> {
                return FieldDataType.BOOLEAN;
            }
            case "byte", "java.lang.Byte" -> {
                return FieldDataType.BYTE;
            }
            case "char", "java.lang.Character" -> {
                return FieldDataType.CHAR;
            }
            case "short", "java.lang.Short" -> {
                return FieldDataType.SHORT;
            }
            case "int", "java.lang.Integer" -> {
                return FieldDataType.INT;
            }
            case "long", "java.lang.Long" -> {
                return FieldDataType.LONG;
            }
            case "float", "java.lang.Float" -> {
                return FieldDataType.FLOAT;
            }
            case "double", "java.lang.Double" -> {
                return FieldDataType.DOUBLE;
            }
            case "java.lang.String" -> {
                return FieldDataType.STRING;
            }
            default -> {
                return FieldDataType.OBJECT;
            }
        }
    }

}
