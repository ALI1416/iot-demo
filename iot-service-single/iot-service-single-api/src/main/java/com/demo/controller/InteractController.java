package com.demo.controller;

import com.demo.base.ControllerBase;
import com.demo.constant.InteractType;
import com.demo.entity.pojo.Result;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import com.demo.service.InteractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <h1>交互</h1>
 *
 * <p>
 * createDate 2023/11/14 11:41:53
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@RequestMapping("interact")
@AllArgsConstructor
@Tag(name = "交互")
@Slf4j
public class InteractController extends ControllerBase {

    private final InteractService interactService;

    /**
     * 发送
     */
    @PostMapping("send")
    @Operation(summary = "发送", description = "需要gatewaySn,deviceSn,commandCode<br>响应：成功id,失败0")
    public Result<ProtocolVo> send(@RequestBody ProtocolVo protocol) {
        Integer gatewaySn = protocol.getGatewaySn();
        Integer deviceSn = protocol.getDeviceSn();
        Integer commandCode = protocol.getCommandCode();
        if (existNull(gatewaySn, deviceSn, commandCode)) {
            return paramError();
        }
        // 请求转换
        Map.Entry<Integer, Protocol.Data> requestMap = Protocol.requestConvert(commandCode, protocol.getRequestJson());
        // 协议格式错误或转换失败或数据检查未通过
        if (requestMap == null || (requestMap.getValue() != null && requestMap.getValue().dataCheckNotPass())) {
            return paramError();
        }
        // 网关序号、设备序号、设备类型不匹配
        // if (GatewayService.notValid(gatewaySn, deviceSn, requestMap.getKey())) {
        //     return paramIsError();
        // }
        protocol.setType(InteractType.DEFAULT.getCode());
        protocol.setRequest(requestMap.getValue());
        protocol.setRequestJson(null);
        // 插入并发送
        ProtocolVo result = interactService.insertAndSend(protocol);
        if (result != null) {
            log.info("交互请求发送成功:[{}]", result);
        } else {
            log.warn("交互请求发送失败:[{}]", protocol);
        }
        return Result.o(result);
    }

    /**
     * 获取最后一条成功消息
     */
    @GetMapping("findLast")
    @Operation(summary = "获取最后一条成功消息")
    @Parameter(name = "gatewaySn", description = "网关序号")
    @Parameter(name = "deviceSn", description = "设备序号")
    @Parameter(name = "commandCode", description = "命令代码")
    public Result<ProtocolVo> findLast(int gatewaySn, int deviceSn, int commandCode) {
        return Result.o(interactService.findLast(gatewaySn, deviceSn, commandCode));
    }

}
