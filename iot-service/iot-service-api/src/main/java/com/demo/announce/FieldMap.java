package com.demo.announce;

import lombok.Getter;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>字段Map</h1>
 *
 * <p>
 * createDate 2024/01/18 09:42:56
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldMap {

    /**
     * 光伏遥控
     */
    PV_REMOTE(
            new AbstractMap.SimpleEntry<>("0", "加载优先"),
            new AbstractMap.SimpleEntry<>("1", "电池优先"),
            new AbstractMap.SimpleEntry<>("2", "电网优先")
    ),

    /**
     * 空
     */
    NULL(new AbstractMap.SimpleEntry<>(null, null));

    /**
     * 字段Map
     */
    private final Map<String, String> map;

    FieldMap(Map.Entry<String, String>... array) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : array) {
            map.put(entry.getKey(), entry.getValue());
        }
        this.map = map;
    }

}
