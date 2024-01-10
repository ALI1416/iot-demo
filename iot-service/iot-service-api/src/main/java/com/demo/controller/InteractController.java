package com.demo.controller;

import com.demo.base.ControllerBase;
import com.demo.entity.pojo.Result;
import com.demo.entity.protocol.Protocol;
import com.demo.entity.vo.ProtocolVo;
import com.demo.service.GatewayService;
import com.demo.service.InteractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <h1>互动</h1>
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
@Tag(name = "互动")
@Slf4j
public class InteractController extends ControllerBase {

    private final InteractService interactService;
    private final GatewayService gatewayService;

    /**
     * 发送
     */
    @PostMapping("send")
    @Operation(summary = "发送", description = "需要gatewaySn/deviceSn/commandCode<br>响应：成功ProtocolVo/失败null")
    public Result<ProtocolVo> send(@RequestBody ProtocolVo base) {
        Integer gatewaySn = base.getGatewaySn();
        Integer deviceSn = base.getDeviceSn();
        Integer commandCode = base.getCommandCode();
        if (existNull(gatewaySn, deviceSn, commandCode)) {
            return paramIsError();
        }
        // 请求转换
        Map.Entry<Integer, Protocol.Data> requestMap = Protocol.requestConvert(commandCode, base.getRequestJson());
        // 协议格式错误或转换失败或数据检查未通过
        if (requestMap == null || (requestMap.getValue() != null && requestMap.getValue().dataCheckNotPass())) {
            return paramIsError();
        }
        // 网关序号、设备序号、设备类型不匹配
        if (gatewayService.notValid(gatewaySn, deviceSn, requestMap.getKey())) {
            return paramIsError();
        }
        base.setRequest(requestMap.getValue());
        base.setRequestJson(null);
        // 插入并发送
        ProtocolVo result = interactService.insertAndSend(base);
        if (result != null) {
            log.info("互动请求发送成功:[{}]", result);
        } else {
            log.warn("互动请求发送失败:[{}]", base);
        }
        return Result.o(result);
    }

}
