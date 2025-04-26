package com.seu.sis.controller;

import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.ParamVO;
import com.seu.sis.service.ParamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-12 10:12
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/param")
public class ParamController {
    private final ParamService paramService;

    @GetMapping()
    public Result<List<ParamVO>> getParam(String search) {
        List<ParamVO> params = paramService.getParams(search);
        return Result.success(params);
    }

    @GetMapping("/list")
    public Result<List<String>> getParamList() {
        List<String> paramList = paramService.getParamList();
        return Result.success(paramList);
    }
}
