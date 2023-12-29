package com.demo.util;

/**
 * <h1>比特工具</h1>
 *
 * <p>
 * createDate 2023/12/07 17:03:59
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
public class BitUtils {

    private BitUtils() {
    }

    /**
     * 获取bit数组(低位在前)
     *
     * @param value 值
     * @return bit数组
     */
    public static boolean[] getBits(int value) {
        boolean[] bits = new boolean[32];
        for (int i = 0; i < 32; i++) {
            bits[i] = ((value >> i) & 1) == 1;
        }
        return bits;
    }

}
