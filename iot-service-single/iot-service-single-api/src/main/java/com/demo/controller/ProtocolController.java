package com.demo.controller;

import com.demo.base.ControllerBase;
import com.demo.entity.pojo.Result;
import com.demo.entity.protocol.ProtocolInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <h1>协议</h1>
 *
 * <p>
 * createDate 2024/05/11 15:29:28
 * </p>
 *
 * @author ALI[ali-k@foxmail.com]
 * @since 1.0.0
 **/
@RestController
@RequestMapping("protocol")
@AllArgsConstructor
@Tag(name = "协议")
@Slf4j
public class ProtocolController extends ControllerBase {

    /**
     * 获取
     */
    @GetMapping("get")
    @Operation(summary = "获取")
    public static Result<List<ProtocolInfo>> get() {
        return Result.o(ProtocolInfo.getTable());
    }

}
