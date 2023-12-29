package com.demo.entity.protocol;

import cn.z.tool.ClassScanner;
import com.alibaba.fastjson2.annotation.JSONField;
import com.demo.announce.FieldUnit;
import com.demo.announce.ProtocolClass;
import com.demo.announce.ProtocolField;
import com.demo.base.ToStringBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.AbstractMap;
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
     * 数据类
     */
    @Schema(description = "数据类")
    @JSONField(serialize = false, deserialize = false)
    private Class<? extends Protocol.Data> dataClass;

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
         * 字段单位 符号,名称
         */
        @Schema(description = "字段单位 符号,名称")
        private Map.Entry<String, String> unit;

        /**
         * 设置字段单位
         *
         * @param unit 符号,名称
         */
        public void setUnit(Map.Entry<String, String> unit) {
            this.unit = unit;
        }

        /**
         * 设置字段单位
         *
         * @param fieldUnit FieldUnit
         */
        public void setUnit(FieldUnit fieldUnit) {
            this.unit = new AbstractMap.SimpleEntry<>(fieldUnit.getSymbol(), fieldUnit.getName());
        }

        /**
         * 构造函数
         *
         * @param name      字段名
         * @param type      字段类型
         * @param fieldUnit FieldUnit
         */
        public Data(String name, String type, FieldUnit fieldUnit) {
            this.name = name;
            this.type = type;
            setUnit(fieldUnit);
        }

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
                protocolInfo.setDataClass(eventClass);
            }
            // 故障类
            Class<? extends Protocol.Fault> faultClass = protocolClass.faultClass();
            if (faultClass != Protocol.NoFault.class) {
                protocolInfo.setFaultMap(getFaultMap(faultClass));
            }
            // 请求类
            Class<? extends Protocol.Data> requestClass = protocolClass.requestClass();
            if (requestClass != Protocol.NoData.class) {
                protocolInfo.setRequestMap(getDataMap(requestClass));
                protocolInfo.setDataClass(requestClass);
            }
            // 响应类
            Class<? extends Protocol.Data> responseClass = protocolClass.responseClass();
            if (responseClass != Protocol.NoData.class) {
                protocolInfo.setResponseMap(getDataMap(responseClass));
                protocolInfo.setDataClass(responseClass);
            }
            // 命令代码,协议信息
            map.put(protocolClass.code(), protocolInfo);
        }
        return map;
    }

    /**
     * 获取数据Map
     *
     * @param dataClass Protocol.Data
     * @return 字段名, ProtocolInfo.Data
     */
    private static Map<String, ProtocolInfo.Data> getDataMap(Class<? extends Protocol.Data> dataClass) {
        Map<String, ProtocolInfo.Data> map = new HashMap<>(getFieldsDataMap(dataClass.getDeclaredFields()));
        // 父类
        Class<?> superClass = dataClass.getSuperclass();
        while (!(superClass == null || superClass == ToStringBase.class || superClass == Object.class)) {
            map.putAll(getFieldsDataMap(superClass.getDeclaredFields()));
            superClass = superClass.getSuperclass();
        }
        return map;
    }

    /**
     * 获取字段数据Map
     *
     * @param fields 字段数组
     * @return 字段名, ProtocolInfo.Data
     */
    private static Map<String, ProtocolInfo.Data> getFieldsDataMap(Field[] fields) {
        Map<String, ProtocolInfo.Data> map = new HashMap<>();
        for (Field field : fields) {
            // 含注解的字段
            ProtocolField protocolField = field.getAnnotation(ProtocolField.class);
            if (protocolField == null) {
                continue;
            }
            ProtocolInfo.Data data = new ProtocolInfo.Data();
            data.setName(protocolField.name());
            data.setType(getTypeName(field.getType()));
            if (protocolField.unit() != FieldUnit.NONE) {
                data.setUnit(protocolField.unit());
            }
            map.put(field.getName(), data);
        }
        return map;
    }

    /**
     * 获取字段类型
     *
     * @param clazz Class
     * @return 字段类型
     */
    private static String getTypeName(Class<?> clazz) {
        // TODO 支持其他类型 数组 对象
        switch (clazz.getName()) {
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
            default: {
                return "object";
            }
        }
    }

    /**
     * 获取故障Map
     *
     * @param faultClass Protocol.Fault
     * @return 分组名, 故障名数组
     */
    private static Map<String, String[]> getFaultMap(Class<? extends Protocol.Fault> faultClass) {
        try {
            /**
             * 调用方法
             * {@link Protocol.Fault#getFaultInfoList()}
             */
            Fault[] faultInfoList = (Fault[])
                    faultClass.getMethod("getFaultInfoList")
                            .invoke(faultClass.getConstructor().newInstance());
            Map<String, String[]> map = new HashMap<>();
            for (Fault fault : faultInfoList) {
                map.put(fault.getGroupName(), fault.getFaultNameArray());
            }
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
