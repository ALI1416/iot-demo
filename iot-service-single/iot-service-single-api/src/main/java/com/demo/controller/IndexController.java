package com.demo.controller;

import com.demo.entity.pojo.Result;
import com.demo.entity.protocol.Frame;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.protocol.ProtocolInfo;
import com.demo.entity.protocol.event.Event1000100;
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
            Event1000100.Event event1000100Event, Event1000100.ReportMinute event1000100ReportMinute, Event1000100.ReportHourDayMonth event1000100EventHourDayMonth,
            Interact3000000.Request interact3000000Request,
            Interact3000100.Request interact3000100Request,
            Interact4000000.Response interact4000000Response,
            Interact4000001.Response interact4000001Response,
            Interact4000100.Response interact4000100Response
    ) {
        return Result.o();
    }

}
