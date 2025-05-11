package com.seu.sis.controller;

import com.seu.sis.model.entity.Result;
import com.seu.sis.model.param.DiagnosisScatterParam;
import com.seu.sis.model.vo.CalculateDataVO;
import com.seu.sis.model.vo.CalculateParam;
import com.seu.sis.model.vo.DiagnosisVO;
import com.seu.sis.model.vo.GateDiagnosisVO;
import com.seu.sis.service.DiagnosisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2025-05-07 19:46
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diagnosis")
public class DiagnosisController {
    private final DiagnosisService diagnosisService;

    @GetMapping("/table")
    public Result<List<CalculateDataVO.TableRow>> getDiagnosisTable() {
        return Result.success(diagnosisService.getDiagnosisTable());
    }

    @GetMapping("/scatter")
    public Result<DiagnosisVO> getDiagnosisScatter(CalculateParam calculateDataVO) {
        return Result.success(diagnosisService.getDiagnosisData(calculateDataVO));
    }

    @GetMapping("/getCleanData")
    public Result<Map<String, List<Object[]>>> getCleanData(CalculateParam calculateDataVO) {
        return Result.success(diagnosisService.getCleanData(calculateDataVO));
    }

    @GetMapping("/getCleanData1")
    public Result<Map<String, List<Object[]>>> getCleanData1(CalculateParam calculateDataVO) {
        return Result.success(diagnosisService.getCleanData1(calculateDataVO));
    }

    @GetMapping("/getCleanData2")
    public Result<Map<String, List<Object[]>>> getCleanData2(CalculateParam calculateDataVO) {
        return Result.success(diagnosisService.getCleanData2(calculateDataVO));
    }

    @GetMapping("/getGateDiagnosisData")
    public Result<GateDiagnosisVO> getGateDiagnosisData() {
        return Result.success(diagnosisService.getGateDiagnosisData());
    }

    @GetMapping("/getGateDiagnosisData2")
    public Result<GateDiagnosisVO> getGateDiagnosisData2() {
        return Result.success(diagnosisService.getGateDiagnosisData2());
    }
}
