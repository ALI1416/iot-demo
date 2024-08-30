package com.demo.controller;

import cn.z.mongo.entity.Page;
import com.demo.base.ControllerBase;
import com.demo.entity.pojo.Result;
import com.demo.entity.vo.ProtocolVo;
import com.demo.service.EventReportService;
import com.demo.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>事件</h1>
 *
 * <p>
 * createDate 2024/02/27 15:30:57
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@RequestMapping("event")
@AllArgsConstructor
@Tag(name = "事件")
@Slf4j
public class EventController extends ControllerBase {

    private final EventService eventService;
    private final EventReportService eventReportService;

    /**
     * 分页查询报表
     */
    @PostMapping("findReportPage")
    @Operation(summary = "分页查询报表", description = "需要gatewaySn,year,reportType")
    public Result<Page<ProtocolVo>> findReportPage(@RequestBody ProtocolVo protocol) {
        if (existNull(protocol.getGatewaySn(), protocol.getYear(), protocol.getReportType())) {
            return paramIsError();
        }
        return Result.o(eventService.findReportPage(protocol));
    }

    /**
     * 获取最后一条消息
     */
    @GetMapping("findLast")
    @Operation(summary = "获取最后一条消息")
    @Parameter(name = "gatewaySn", description = "网关序号")
    @Parameter(name = "deviceSn", description = "设备序号")
    @Parameter(name = "commandCode", description = "命令代码")
    public static Result<ProtocolVo> findLast(int gatewaySn, int deviceSn, int commandCode) {
        return Result.o(EventService.getMsg(gatewaySn, deviceSn, commandCode));
    }

    /**
     * 重新计算分钟报表
     */
    @GetMapping("recalcReportMinute")
    @Operation(summary = "重新计算分钟报表", description = "响应：删除条数")
    @Parameter(name = "gatewaySn", description = "网关序号 null 所有网关")
    @Parameter(name = "deviceSn", description = "设备序号 null 所有设备")
    @Parameter(name = "commandCode", description = "命令代码 null 所有命令")
    @Parameter(name = "year", description = "年")
    @Parameter(name = "month", description = "月 null 1-12月")
    @Parameter(name = "day", description = "日 null 1-31日")
    @Parameter(name = "hour", description = "小时 null 0-23小时")
    @Parameter(name = "minute", description = "分钟 null 0-59分钟")
    public Result<Long> recalcReportMinute(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour, Integer minute) {
        long count = eventReportService.reportMinuteDelete(gatewaySn, deviceSn, commandCode, year, month, day, hour, minute);
        eventReportService.reportMinute(gatewaySn, deviceSn, commandCode, year, month, day, hour, minute);
        return Result.o(count);
    }

    /**
     * 重新计算小时报表
     */
    @GetMapping("recalcReportHour")
    @Operation(summary = "重新计算小时报表", description = "响应：删除条数")
    @Parameter(name = "gatewaySn", description = "网关序号 null 所有网关")
    @Parameter(name = "deviceSn", description = "设备序号 null 所有设备")
    @Parameter(name = "commandCode", description = "命令代码 null 所有命令")
    @Parameter(name = "year", description = "年")
    @Parameter(name = "month", description = "月 null 1-12月")
    @Parameter(name = "day", description = "日 null 1-31日")
    @Parameter(name = "hour", description = "小时 null 0-23小时")
    public Result<Long> recalcReportHour(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day, Integer hour) {
        long count = eventReportService.reportHourDelete(gatewaySn, deviceSn, commandCode, year, month, day, hour);
        eventReportService.reportHour(gatewaySn, deviceSn, commandCode, year, month, day, hour);
        return Result.o(count);
    }

    /**
     * 重新计算日报表
     */
    @GetMapping("recalcReportDay")
    @Operation(summary = "重新计算日报表", description = "响应：删除条数")
    @Parameter(name = "gatewaySn", description = "网关序号 null 所有网关")
    @Parameter(name = "deviceSn", description = "设备序号 null 所有设备")
    @Parameter(name = "commandCode", description = "命令代码 null 所有命令")
    @Parameter(name = "year", description = "年")
    @Parameter(name = "month", description = "月 null 1-12月")
    @Parameter(name = "day", description = "日 null 1-31日")
    public Result<Long> recalcReportDay(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month, Integer day) {
        long count = eventReportService.reportDayDelete(gatewaySn, deviceSn, commandCode, year, month, day);
        eventReportService.reportDay(gatewaySn, deviceSn, commandCode, year, month, day);
        return Result.o(count);
    }

    /**
     * 重新计算月报表
     */
    @GetMapping("recalcReportMonth")
    @Operation(summary = "重新计算月报表", description = "响应：删除条数")
    @Parameter(name = "gatewaySn", description = "网关序号 null 所有网关")
    @Parameter(name = "deviceSn", description = "设备序号 null 所有设备")
    @Parameter(name = "commandCode", description = "命令代码 null 所有命令")
    @Parameter(name = "year", description = "年")
    @Parameter(name = "month", description = "月 null 1-12月")
    @Parameter(name = "day", description = "日 null 1-31日")
    public Result<Long> recalcReportMonth(Integer gatewaySn, Integer deviceSn, Integer commandCode, int year, Integer month) {
        long count = eventReportService.reportMonthDelete(gatewaySn, deviceSn, commandCode, year, month);
        eventReportService.reportMonth(gatewaySn, deviceSn, commandCode, year, month);
        return Result.o(count);
    }

}
