package com.demo.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

/**
 * <h1>时间工具</h1>
 *
 * <p>
 * createDate 2023/11/30 10:56:39
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class DateUtils {

    private DateUtils() {
    }

    /**
     * 获取年
     *
     * @return 年
     */
    public static int getYear() {
        return Calendar.getInstance().get(YEAR);
    }

    /**
     * 获取年
     *
     * @param timestamp 时间戳
     * @return 年
     */
    public static int getYear(long timestamp) {
        return new Builder().setInstant(timestamp).build().get(YEAR);
    }

    /**
     * 获取年
     *
     * @param date Date
     * @return 年
     */
    public static int getYear(Date date) {
        return getYear(date.getTime());
    }

    /**
     * 获取年月
     *
     * @param timestamp 时间戳
     * @return 年月
     */
    public static int getYearMonth(long timestamp) {
        Calendar calendar = new Builder().setInstant(timestamp).build();
        return calendar.get(YEAR) * 100 + calendar.get(MONTH) + 1;
    }

    /**
     * 获取年月
     *
     * @param date Date
     * @return 年月
     */
    public static int getYearMonth(Date date) {
        return getYearMonth(date.getTime());
    }

    /**
     * 获取年月
     *
     * @param year  年
     * @param month 月
     * @return 年月
     */
    public static int getYearMonth(int year, int month) {
        return year * 100 + month;
    }

    /**
     * 获取年月日
     *
     * @param timestamp 时间戳
     * @return 年月日
     */
    public static int getYearMonthDay(long timestamp) {
        Calendar calendar = new Builder().setInstant(timestamp).build();
        return calendar.get(YEAR) * 10000 + (calendar.get(MONTH) + 1) * 100 + calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取年月日
     *
     * @param date Date
     * @return 年月日
     */
    public static int getYearMonthDay(Date date) {
        return getYearMonthDay(date.getTime());
    }

    /**
     * 获取年月日
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 年月日
     */
    public static int getYearMonthDay(int year, int month, int day) {
        return year * 10000 + month * 100 + day;
    }

    /**
     * 获取当前月份最大日期
     *
     * @param timestamp 时间戳
     * @return 当前月份最大日期
     */
    public static int getMaxDay(long timestamp) {
        return new Builder().setInstant(timestamp).build().getActualMaximum(DAY_OF_MONTH);
    }

    /**
     * 获取当前月份最大日期
     *
     * @param date Date
     * @return 当前月份最大日期
     */
    public static int getMaxDay(Date date) {
        return getMaxDay(date.getTime());
    }

    /**
     * 设置为本周第一天(周一)
     *
     * @param calendar Calendar
     */
    public static void setWeekFirst(Calendar calendar) {
        calendar.setFirstDayOfWeek(MONDAY);
        calendar.set(DAY_OF_WEEK, calendar.getFirstDayOfWeek());
    }

    /**
     * 获取本周第一天(周一)
     *
     * @param timestamp 时间戳
     * @return Calendar
     */
    public static Calendar getWeekFirst(long timestamp) {
        Calendar calendar = new Builder().setInstant(timestamp).build();
        setWeekFirst(calendar);
        return calendar;
    }

    /**
     * 获取本周第一天(周一)
     *
     * @param date Date
     * @return Calendar
     */
    public static Calendar getWeekFirst(Date date) {
        return getWeekFirst(date.getTime());
    }

    /**
     * 增加天数
     *
     * @param calendar Calendar
     * @param day      增加天数
     */
    public static void addDay(Calendar calendar, int day) {
        calendar.add(DAY_OF_MONTH, day);
    }

    /**
     * 增加月数
     *
     * @param calendar Calendar
     * @param month    增加月数
     */
    public static void addMonth(Calendar calendar, int month) {
        calendar.add(MONTH, month);
    }

    /**
     * 获取时间戳
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @param second      秒
     * @param millisecond 毫秒
     * @return 时间戳
     */
    public static long getTimestamp(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return new Builder().setFields(
                YEAR, year,
                MONTH, month - 1,
                DAY_OF_MONTH, day,
                HOUR_OF_DAY, hour,
                MINUTE, minute,
                SECOND, second,
                MILLISECOND, millisecond
        ).build().getTimeInMillis();
    }

    /**
     * 获取时间戳
     *
     * @param year        年
     * @param month       月
     * @param day         日
     * @param hour        小时
     * @param minute      分钟
     * @param second      秒
     * @param millisecond 毫秒
     * @return Timestamp
     */
    public static Timestamp getNewTimestamp(int year, int month, int day, int hour, int minute, int second, int millisecond) {
        return new Timestamp(getTimestamp(year, month, day, hour, minute, second, millisecond));
    }

}
