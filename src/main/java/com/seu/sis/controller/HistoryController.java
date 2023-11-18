package com.seu.sis.controller;

import com.seu.sis.dao.service.HistoryService;
import com.seu.sis.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-18 12:41
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {
    private final HistoryService historyService;

    @GetMapping("")
    public Result<Map<String, List<Object[]>>> getHistory(Integer unitId, String st, String et) {
        Map<String, List<Object[]>> history = historyService.getHistory(unitId, st, et);
        return Result.success(history);
    }


    @GetMapping("/scatter")
    public Result<List<Double[]>> getHistoryScatter(Integer unitId,
                                                    String x,
                                                    String y,
                                                    String st,
                                                    String et) {
        List<Double[]> scatter = historyService.getScatter(unitId, x, y, st, et);
        return Result.success(scatter);
    }
}
