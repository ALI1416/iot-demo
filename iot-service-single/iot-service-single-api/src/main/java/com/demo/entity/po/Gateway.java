package com.demo.entity.po;

import com.demo.base.EntityBase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2023/11/30 14:35:11
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@Getter
@Setter
@Schema(description = "网关")
public class Gateway extends EntityBase {

    /**
     * 网关序号
     */
    @Schema(description = "网关序号")
    private Integer sn;
    /**
     * 网关名称
     */
    @Schema(description = "网关名称")
    private String name;
    /**
     * 上一次在线时间
     */
    @Schema(description = "上一次在线时间")
    private Timestamp lastOnlineTime;
    /**
     * 备注
     */
    @Schema(description = "备注")
    private String comment;

}
