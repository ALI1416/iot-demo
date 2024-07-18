package com.demo.controller;

import com.demo.base.ControllerBase;
import com.demo.entity.pojo.PageInfo;
import com.demo.entity.pojo.Result;
import com.demo.entity.vo.GatewayVo;
import com.demo.service.GatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <h1>网关</h1>
 *
 * <p>
 * createDate 2024/02/26 10:58:52
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@RequestMapping("gateway")
@AllArgsConstructor
@Tag(name = "网关")
@Slf4j
public class GatewayController extends ControllerBase {

    private final GatewayService gatewayService;

    /**
     * 插入
     */
    @PostMapping("insert")
    @Operation(summary = "插入", description = "需要sn,name<br>响应：成功id,失败0")
    public Result<Long> send(@RequestBody GatewayVo gateway) {
        if (existNull(gateway.getSn(), gateway.getName())) {
            return paramIsError();
        }
        return Result.o(gatewayService.insert(gateway));
    }

    /**
     * 查询通过id
     */
    @GetMapping("findById")
    @Operation(summary = "查询通过id")
    public Result<GatewayVo> findById(long id) {
        return Result.o(gatewayService.findById(id));
    }

    /**
     * 查询通过sn
     */
    @GetMapping("findBySn")
    @Operation(summary = "查询通过sn")
    public Result<GatewayVo> findBySn(int sn) {
        return Result.o(gatewayService.findBySn(sn));
    }

    /**
     * 查询
     */
    @PostMapping("find")
    @Operation(summary = "查询")
    public Result<PageInfo<GatewayVo>> find(@RequestBody GatewayVo gateway) {
        return Result.o(gatewayService.find(gateway));
    }

    /**
     * 获取网关表缓存
     */
    @GetMapping("getCache")
    @Operation(summary = "获取网关表缓存")
    public static Result<List<GatewayVo>> getCache() {
        return Result.o(GatewayService.getTable());
    }

    /**
     * 更新网关表缓存
     */
    @GetMapping("updateCache")
    @Operation(summary = "更新网关表缓存")
    public Result updateCache() {
        gatewayService.updateTable();
        return Result.o();
    }

}
