package com.demo.announce;

import com.demo.entity.protocol.ProtocolInfo;
import lombok.Getter;

/**
 * <h1>字段状态枚举</h1>
 *
 * <p>
 * createDate 2024/01/18 09:42:56
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
public enum FieldStatusEnum {

    /**
     * 空
     */
    NULL(new ProtocolInfo.FieldStatus(null, null));

    /**
     * 工作模式
     */
    @Getter
    public enum WorkMode {

        /**
         * 调试
         */
        DEBUG(0),
        /**
         * 峰谷
         */
        PEAK_VALLEY(1),
        /**
         * 增容
         */
        INCREASE_CAPACITY(2),
        /**
         * 远程
         */
        REMOTE(3),
        ;

        /**
         * 编码
         */
        private final int code;

        WorkMode(int code) {
            this.code = code;
        }

    }

    /**
     * 字段状态
     */
    private final ProtocolInfo.FieldStatus[] status;

    FieldStatusEnum(ProtocolInfo.FieldStatus... status) {
        this.status = status;
    }

}
