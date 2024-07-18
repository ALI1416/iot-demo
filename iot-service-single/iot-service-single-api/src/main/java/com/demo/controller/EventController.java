package com.demo.controller;

import cn.z.mongo.entity.Page;
import com.demo.base.ControllerBase;
import com.demo.entity.pojo.Result;
import com.demo.entity.vo.ProtocolVo;
import com.demo.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
