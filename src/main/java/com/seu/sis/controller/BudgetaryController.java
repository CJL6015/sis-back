package com.seu.sis.controller;

import com.seu.sis.dao.service.BudgetaryService;
import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.BudgetaryParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-15 21:50
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/budgetary")
public class BudgetaryController {

    private final BudgetaryService budgetaryService;


    @GetMapping()
    public Result<List<List<Double>>> getBudgetaryData() {
        List<List<Double>> budgetaryData = budgetaryService.getBudgetaryData();
        return Result.success(budgetaryData);
    }

    @GetMapping("/param")
    public Result<Map<String, Double>> getParam() {
        Map<String, Double> param = budgetaryService.getParam();
        return Result.success(param);
    }

    @PostMapping("/submit")
    public Result<Boolean> submit(@RequestBody BudgetaryParam param) {
        boolean submit = budgetaryService.submit(param);
        return Result.success(submit);
    }

    @GetMapping("/data")
    public Result<List<List<Object>>> getData() {
        List<List<Object>> data = budgetaryService.getData();
        return Result.success(data);
    }
}
