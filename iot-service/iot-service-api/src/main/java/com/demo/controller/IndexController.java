package com.demo.controller;

import com.demo.entity.pojo.Result;
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

}
