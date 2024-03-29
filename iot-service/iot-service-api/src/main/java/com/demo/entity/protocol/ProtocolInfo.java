package com.demo.entity.protocol;

import cn.z.tool.ClassScanner;
import com.alibaba.fastjson2.annotation.JSONField;
import com.demo.announce.*;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
     * 命令名
     */
    @Schema(description = "命令名")
    private String name;
    /**
     * 协议类型
     */
    @Schema(description = "协议类型")
    private Integer type;
    /**
     * 设备类型
     */
    @Schema(description = "设备类型")
    private Integer deviceType;
    /**
     * 特殊处理
     */
    @Schema(description = "特殊处理")
    private Boolean special;
    /**
     * 事件Map
     */
    @Schema(description = "事件Map")
    private Map<String, Data> eventMap;
    /**
     * 故障Map
     */
    @Schema(description = "故障Map")
    private Map<String, String[]> faultMap;
    /**
     * 请求Map
     */
    @Schema(description = "请求Map")
    private Map<String, Data> requestMap;
    /**
     * 响应Map
     */
    @Schema(description = "响应Map")
    private Map<String, Data> responseMap;
    /**
     * 事件类
     */
    @Schema(description = "事件类")
    @JSONField(serialize = false, deserialize = false)
    private Class<? extends Protocol.Data> eventClass;
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
     * 数据
     */
    @Getter
    @Setter
    @Schema(description = "数据")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data extends ToStringBase {

        /**
         * 字段名
         */
        @Schema(description = "字段名")
        private String name;
        /**
         * 字段类型
         */
        @Schema(description = "字段类型")
        private String type;
        /**
         * 字段特殊类型
         */
        @Schema(description = "字段特殊类型")
        private Integer specialType;
        /**
         * 嵌套
         */
        @Schema(description = "嵌套")
        private Data nest;
        /**
         * 嵌套Map
         */
        @Schema(description = "嵌套Map")
        private Map<String, Data> nestMap;
        /**
         * 字段单位
         */
        @Schema(description = "字段单位")
        private Map.Entry<String, String> unit;
        /**
         * 字段Map
         */
        @Schema(description = "字段Map")
        private Map<String, String> map;
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

    }

    /**
     * 故障
     */
    @Getter
    @Setter
    @Schema(description = "故障")
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Fault extends ToStringBase {

        /**
         * 分组名
         */
        @Schema(description = "分组名")
        private String groupName;
        /**
         * 故障名数组
         */
        @Schema(description = "故障名数组")
        private String[] faultNameArray;

    }

    /**
     * 协议信息 命令代码,ProtocolInfo
     */
    private static final Map<Integer, ProtocolInfo> INFO = ProtocolInfo.get(Protocol.class.getPackage().getName());

    /**
     * 获取协议信息Map
     *
     * @return 命令代码, ProtocolInfo
     */
    public static Map<Integer, ProtocolInfo> get() {
        return INFO;
    }

    /**
     * 获取协议信息
     *
     * @param commandCode 命令代码
     * @return ProtocolInfo
     */
    public static ProtocolInfo get(int commandCode) {
        return INFO.get(commandCode);
    }

    /**
     * 获取协议信息Map
     *
     * @param packageName 包名
     * @return 命令代码, ProtocolInfo
     */
    private static Map<Integer, ProtocolInfo> get(String packageName) {
        Map<Integer, ProtocolInfo> map = new HashMap<>();
        // 包内所有类
        Set<Class<?>> classSet = ClassScanner.getClass(packageName);
        for (Class<?> clazz : classSet) {
            // 含注解的类
            ProtocolClass protocolClass = clazz.getAnnotation(ProtocolClass.class);
            if (protocolClass == null) {
                continue;
            }
            ProtocolInfo protocolInfo = new ProtocolInfo();
            // 命令名
            protocolInfo.setName(protocolClass.name());
            // 协议类型
            protocolInfo.setType(protocolClass.type().getCode());
            // 设备类型
            protocolInfo.setDeviceType(protocolClass.deviceType().getCode());
            // 特殊处理
            if (protocolClass.special()) {
                protocolInfo.setSpecial(true);
            }
            // 事件类
            Class<? extends Protocol.Data> eventClass = protocolClass.eventClass();
            if (eventClass != Protocol.NoData.class) {
                protocolInfo.setEventMap(getDataMap(eventClass));
                protocolInfo.setEventClass(eventClass);
            }
            // 故障Map
            if (protocolClass.faultMap() != FaultMap.NULL) {
                protocolInfo.setFaultMap(protocolClass.faultMap().getMap());
            }
            // 请求类
            Class<? extends Protocol.Data> requestClass = protocolClass.requestClass();
            if (requestClass != Protocol.NoData.class) {
                protocolInfo.setRequestMap(getDataMap(requestClass));
                protocolInfo.setRequestClass(requestClass);
            }
            // 响应类
            Class<? extends Protocol.Data> responseClass = protocolClass.responseClass();
            if (responseClass != Protocol.NoData.class) {
                protocolInfo.setResponseMap(getDataMap(responseClass));
                protocolInfo.setResponseClass(responseClass);
            }
            // 命令代码,协议信息
            map.put(protocolClass.code(), protocolInfo);
        }
        return map;
    }

    /**
     * 获取数据Map
     *
     * @param clazz Protocol.Data
     * @return 字段名, ProtocolInfo.Data
     */
    private static Map<String, ProtocolInfo.Data> getDataMap(Class<?> clazz) {
        Map<String, ProtocolInfo.Data> map = new HashMap<>(getFieldArrayDataMap(clazz.getDeclaredFields()));
        // 父类
        Class<?> superClass = clazz.getSuperclass();
        while (!(superClass == null || superClass == ToStringBase.class || superClass == Object.class)) {
            map.putAll(getFieldArrayDataMap(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        return map;
    }

    /**
     * 获取字段数组数据Map
     *
     * @param fieldArray 字段数组
     * @return 字段名, ProtocolInfo.Data
     */
    private static Map<String, ProtocolInfo.Data> getFieldArrayDataMap(Field[] fieldArray) {
        Map<String, ProtocolInfo.Data> map = new HashMap<>();
        for (Field field : fieldArray) {
            // 含注解的字段
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            if (protocolField == null) {
                continue;
            }
            map.put(field.getName(), getFieldData(field.getGenericType(), protocolField));
        }
        return map;
    }

    /**
     * 获取字段数据
     *
     * @param protocolField ProtocolField
     * @param type          Type
     * @return ProtocolInfo.Data
     */
    private static ProtocolInfo.Data getFieldData(Type type, ProtocolField protocolField) {
        ProtocolInfo.Data data = new ProtocolInfo.Data();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            // 泛型对象 例如List<Integer>
            if (Collection.class.isAssignableFrom((Class<?>) (parameterizedType.getRawType()))) {
                // 集合类型
                data.setType("collection");
                data.setNest(getFieldData(parameterizedType.getActualTypeArguments()[0], protocolField));
            } else {
                // 非集合类型
                data.setType("uncollection");
            }
        } else if (type instanceof GenericArrayType) {
            // 泛型对象数组 例如List<Integer>[]
            data.setType("array");
            data.setNest(getFieldData(((GenericArrayType) type).getGenericComponentType(), protocolField));
        } else if (type instanceof Class) {
            // 普通数组类型、非数组类型 例如int[]、int
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) {
                // 数组类型
                data.setType("array");
                data.setNest(getFieldData(clazz.getComponentType(), protocolField));
            } else {
                // 非数组类型
                data.setType(getTypeName(clazz));
                if ("object".equals(data.getType())) {
                    // 对象类型
                    data.setNestMap(getDataMap(clazz));
                } else {
                    // 普通类型
                    setNormalTypeData(data, protocolField);
                }
            }
        } else {
            // 未知类型
            data.setType("unknown");
        }
        return data;
    }

    /**
     * 设置普通类型数据
     *
     * @param data          ProtocolInfo.Data
     * @param protocolField ProtocolField
     */
    private static void setNormalTypeData(ProtocolInfo.Data data, ProtocolField protocolField) {
        // 字段名
        data.setName(protocolField.name());
        // 字段特殊类型
        if (protocolField.specialType() != FieldSpecialType.DEFAULT) {
            data.setSpecialType(protocolField.specialType().getCode());
        }
        // 字段单位
        if (protocolField.unit() != FieldUnit.NONE) {
            data.setUnit(protocolField.unit().getEntry());
        }
        // 字段Map
        if (protocolField.map() != FieldMap.NULL) {
            data.setMap(protocolField.map().getMap());
        }
        // 最大值
        if (!protocolField.max().isEmpty()) {
            data.setMax(protocolField.max());
        }
        // 最小值
        if (!protocolField.min().isEmpty()) {
            data.setMin(protocolField.min());
        }
        // 最大字符长度
        if (protocolField.maxLength() != Integer.MAX_VALUE) {
            data.setMaxLength(protocolField.maxLength());
        }
        // 最小字符长度
        if (protocolField.minLength() != 0) {
            data.setMinLength(protocolField.minLength());
        }
        // 特殊处理
        if (protocolField.special()) {
            data.setSpecial(true);
        }
        // 备注
        if(!protocolField.comment().isEmpty()){
            data.setComment(protocolField.comment());
        }
    }

    /**
     * 获取字段类型名(基本类型、字符串类型、对象类型)
     *
     * @param fieldType 字段类型
     * @return 字段类型名
     */
    private static String getTypeName(Class<?> fieldType) {
        switch (fieldType.getName()) {
            case "boolean":
            case "java.lang.Boolean": {
                return "boolean";
            }
            case "byte":
            case "java.lang.Byte": {
                return "byte";
            }
            case "char":
            case "java.lang.Character": {
                return "char";
            }
            case "short":
            case "java.lang.Short": {
                return "short";
            }
            case "int":
            case "java.lang.Integer": {
                return "int";
            }
            case "long":
            case "java.lang.Long": {
                return "long";
            }
            case "float":
            case "java.lang.Float": {
                return "float";
            }
            case "double":
            case "java.lang.Double": {
                return "double";
            }
            case "java.lang.String": {
                return "string";
            }
            default: {
                return "object";
            }
        }
    }

}
