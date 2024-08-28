package com.demo.entity.protocol.event;

import com.demo.announce.*;
import com.demo.base.ToStringBase;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;
import java.util.stream.IntStream;

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
@ProtocolAnno(code = 10100, name = "温度计事件", deviceType = DeviceType.THERMOMETER,
        event = @ProtocolAnno.Event(event = Event10100.Event.class, reportMinute = Event10100.ReportMinute.class,
                reportHour = Event10100.ReportHourDayMonth.class, reportDay = Event10100.ReportHourDayMonth.class,
                reportMonth = Event10100.ReportHourDayMonth.class, reportHandle = Event10100.ReportHandle.class)
)
public class Event10100 {

    private Event10100() {
    }

    /**
     * 温度计事件
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件", name = "Event10100.Event")
    public static class Event extends ToStringBase implements Protocol.Data {

        /**
         * 温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "温度(0.0001℃ 0.0001摄氏度)")
        @FieldAnno(name = "温度", type = FieldType.INSTANT, unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
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
     * 温度计事件分钟报表
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件分钟报表", name = "Event10100.ReportMinute")
    public static class ReportMinute extends Protocol.DefaultData {

        /**
         * 平均温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "平均温度(0.0001℃ 0.0001摄氏度)")
        @FieldAnno(name = "平均温度", unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureAvg;

    }

    /**
     * 温度计事件小时/日/月报表
     */
    @Getter
    @Setter
    @Schema(description = "温度计事件小时/日/月报表", name = "Event10100.ReportHourDayMonth")
    public static class ReportHourDayMonth extends ReportMinute {

        /**
         * 最高温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "最高温度(0.0001℃ 0.0001摄氏度)")
        @FieldAnno(name = "最高温度", unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureMax;
        /**
         * 最低温度(0.0001℃ 0.0001摄氏度)
         */
        @Schema(description = "最低温度(0.0001℃ 0.0001摄氏度)")
        @FieldAnno(name = "最低温度", unit = FieldUnitEnum.TEMPERATURE_N4, recommend = FieldUnitEnum.TEMPERATURE, divide = 10000)
        private Integer temperatureMin;

    }

    /**
     * 温度计事件报表处理
     */
    public static class ReportHandle implements Protocol.ReportHandle {

        /**
         * 分钟报表处理
         *
         * @param list 全部数据
         * @return 分钟报表
         */
        @Override
        public ProtocolVo minute(List<ProtocolVo> list) {
            ProtocolVo data = list.get(0);
            // 设置月、日、小时、分钟
            Calendar c = new Calendar.Builder().setInstant(data.getCreateTime()).build();
            data.setMonth(c.get(Calendar.MONTH) + 1);
            data.setDay(c.get(Calendar.DAY_OF_MONTH));
            data.setHour(c.get(Calendar.HOUR_OF_DAY));
            data.setMinute(c.get(Calendar.MINUTE));
            // 删除创建时间
            data.setCreateTime(null);
            // 计算
            ReportMinute event = new ReportMinute();
            event.setTemperatureAvg((int) Math.round(list.stream().mapToInt(m -> ((Event) m.getEvent()).temperature).average().getAsDouble()));
            data.setEvent(event);
            return data;
        }

        /**
         * 小时报表处理
         *
         * @param list 分钟报表
         * @return 小时报表
         */
        @Override
        public ProtocolVo hour(List<ProtocolVo> list) {
            ProtocolVo data = list.get(0);
            // 删除分钟
            data.setMinute(null);
            // 计算
            ReportHourDayMonth event = new ReportHourDayMonth();
            int[] intArray = list.stream().mapToInt(m -> ((ReportMinute) m.getEvent()).temperatureAvg).toArray();
            event.setTemperatureAvg((int) Math.round(IntStream.of(intArray).average().getAsDouble()));
            event.setTemperatureMax(IntStream.of(intArray).max().getAsInt());
            event.setTemperatureMin(IntStream.of(intArray).min().getAsInt());
            data.setEvent(event);
            return data;
        }

        /**
         * 日报表处理
         *
         * @param list 小时报表
         * @return 日报表
         */
        @Override
        public ProtocolVo day(List<ProtocolVo> list) {
            ProtocolVo data = list.get(0);
            // 删除小时
            data.setHour(null);
            // 计算
            data.setEvent(getDayMonthEvent(list));
            return data;
        }

        /**
         * 获取日月事件
         *
         * @param list 小时/月报表
         * @return 日/月事件
         */
        private static ReportHourDayMonth getDayMonthEvent(List<ProtocolVo> list) {
            ReportHourDayMonth event = new ReportHourDayMonth();
            event.setTemperatureAvg((int) Math.round(list.stream().mapToInt(m -> ((ReportHourDayMonth) m.getEvent()).getTemperatureAvg()).average().getAsDouble()));
            event.setTemperatureMax(list.stream().mapToInt(m -> ((ReportHourDayMonth) m.getEvent()).temperatureMax).max().getAsInt());
            event.setTemperatureMin(list.stream().mapToInt(m -> ((ReportHourDayMonth) m.getEvent()).temperatureMin).min().getAsInt());
            return event;
        }

        /**
         * 月报表处理
         *
         * @param list 日报表
         * @return 月报表
         */
        @Override
        public ProtocolVo month(List<ProtocolVo> list) {
            ProtocolVo data = list.get(0);
            // 删除日
            data.setDay(null);
            // 计算
            data.setEvent(getDayMonthEvent(list));
            return data;
        }

    }

}
