package com.demo.entity.protocol.event;

import com.demo.announce.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <h1>温度计事件</h1>
 *
 * <p>
 * createDate 2023/11/10 17:12:31
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Schema(description = "温度计事件")
@Protocol(code = 10100, name = "温度计事件", type = ProtocolType.EVENT, deviceType = DeviceType.THERMOMETER,
        event = Event10100.Event.class, eventMinute = Event10100.EventMinute.class, eventHour = Event10100.EventHourDayMonth.class, eventDay = Event10100.EventHourDayMonth.class, eventMonth = Event10100.EventHourDayMonth.class)
public class Event10100 {

    private Event10100() {
    }

    /**
     * 温度计事件
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件", name = "Event10100.Event")
    public static class Event implements com.demo.entity.protocol.Protocol.Data {

        /**
         * 温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "温度(0.0001℃ 0.0001摄氏度)")
        @Field(name = "温度", type = FieldType.INSTANT, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperature;

        /**
         * 数据检查未通过
         *
         * @return 数据检查未通过
         */
        @Override
        public boolean dataCheckNotPass() {
            return temperature == null;
        }

    }

    /**
     * 温度计事件报表-分钟
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件报表-分钟", name = "Event10100.EventMinute")
    public static class EventMinute extends com.demo.entity.protocol.Protocol.Default {

        /**
         * 平均温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "平均温度(0.0001℃ 0.0001摄氏度)")
        @Field(name = "平均温度", type = FieldType.AVG, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureAvg;

    }

    /**
     * 温度计事件报表-小时-日-月
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件报表-小时-日-月", name = "Event10100.EventHourDayMonth")
    public static class EventHourDayMonth extends EventMinute {

        /**
         * 最高温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "最高温度(0.0001℃ 0.0001摄氏度)")
        @Field(name = "最高温度", type = FieldType.MAX, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureMax;
        /**
         * 最低温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "最低温度(0.0001℃ 0.0001摄氏度)")
        @Field(name = "最低温度", type = FieldType.MIN, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureMin;

    }

}
