package com.demo.announce;

import com.demo.entity.protocol.fault.Fault20100;
import lombok.Getter;

import java.util.Map;

/**
 * <h1>故障Map</h1>
 *
 * <p>
 * createDate 2024/01/18 11:43:24
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FaultMap {

    /**
     * 温度计故障
     */
    FAULT_20100(Fault20100.MAP),

    /**
     * 空
     */
    NULL(null);

    /**
     * 故障Map
     */
    private final Map<String, String[]> map;

    FaultMap(Map<String, String[]> map) {
        this.map = map;
    }

}
