package com.seu.sis.controller;

import com.seu.sis.model.entity.Result;
import com.seu.sis.model.param.TestReportParam;
import com.seu.sis.model.param.TestResultParam;
import com.seu.sis.model.vo.TestResultVO;
import com.seu.sis.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;

    @GetMapping("/getTestReport")
    public Result<List<String>> getTestReport(TestReportParam param) {
        return Result.success(testService.getTestReport(param));
    }

    @GetMapping("/getTestResult")
    public Result<TestResultVO> getTestReport(TestResultParam param) {
        return Result.success(testService.getTestResult(param.getTime(), param.getUnitId()));
    }

    @PostMapping("/submit/{unitId}")
    public Result<Boolean> submit(@PathVariable Integer unitId) {
        return Result.success(testService.submit(unitId));
    }
}
