package com.demo.util;

import java.util.Calendar;

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
     * 获取时间戳yyyy
     *
     * @param timestamp 时间戳
     * @return yyyy
     */
    public static int getYyyy(long timestamp) {
        return new Calendar.Builder().setInstant(timestamp).build().get(Calendar.YEAR);
    }

    /**
     * 获取时间戳yyyyMM
     *
     * @param timestamp 时间戳
     * @return yyyyMM
     */
    public static int getYyyyMm(long timestamp) {
        Calendar calendar = new Calendar.Builder().setInstant(timestamp).build();
        return calendar.get(Calendar.YEAR) * 100 + calendar.get(Calendar.MONTH) + 1;
    }

}
