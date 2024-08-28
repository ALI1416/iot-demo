package com.demo.controller;

import com.demo.entity.pojo.Result;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.protocol.ProtocolInfo;
import com.demo.entity.protocol.event.Event10100;
import com.demo.entity.protocol.interact.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h1>首页</h1>
 *
 * <p>
 * createDate 2023/10/12 14:17:18
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@Tag(name = "首页")
public class IndexController {

    /**
     * 首页
     */
    @GetMapping
    @Operation(summary = "首页")
    public Result index() {
        return Result.o();
    }

    /**
     * 协议实体
     */
    @GetMapping("protocolEntity")
    @Operation(summary = "协议实体")
    public Result protocol(
            Frame frame, Protocol protocol, ProtocolInfo protocolInfo,
            Event10100.Event event10100Event, Event10100.ReportMinute event10100ReportMinute, Event10100.ReportHourDayMonth event10100EventHourDayMonth,
            Interact30000.Request interact30000Request,
            Interact30100.Request interact30100Request,
            Interact40000.Response interact40000Response,
            Interact40001.Response interact40001Response,
            Interact40100.Response interact40100Response
    ) {
        return Result.o();
    }

}
