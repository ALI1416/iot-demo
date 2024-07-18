package com.demo.controller;

import com.demo.base.ControllerBase;
import com.demo.entity.pojo.PageInfo;
import com.demo.entity.pojo.Result;
import com.demo.entity.vo.DeviceVo;
import com.demo.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>设备</h1>
 *
 * <p>
 * createDate 2024/02/26 17:34:16
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@RequestMapping("device")
@AllArgsConstructor
@Tag(name = "设备")
@Slf4j
public class DeviceController extends ControllerBase {

    private final DeviceService deviceService;

    /**
     * 查询通过id
     */
    @GetMapping("findById")
    @Operation(summary = "查询通过id")
    public Result<DeviceVo> findById(long id) {
        return Result.o(deviceService.findById(id));
    }

    /**
     * 查询通过序号
     */
    @GetMapping("findBySn")
    @Operation(summary = "查询通过序号")
    @Parameter(name = "sn", description = "序号")
    public Result<DeviceVo> findBySn(int sn) {
        return Result.o(deviceService.findBySn(sn));
    }

    /**
     * 查询
     */
    @PostMapping("find")
    @Operation(summary = "查询")
    public Result<PageInfo<DeviceVo>> find(@RequestBody DeviceVo device) {
        return Result.o(deviceService.find(device));
    }

    /**
     * 查询通过网关序号
     */
    @GetMapping("findByGatewaySn")
    @Operation(summary = "查询通过网关序号")
    @Parameter(name = "gatewaySn", description = "网关序号")
    public Result<List<DeviceVo>> findByGatewaySn(long gatewaySn) {
        return Result.o(deviceService.findByGatewaySn(gatewaySn));
    }

}
