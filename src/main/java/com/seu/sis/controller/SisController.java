package com.seu.sis.controller;

import com.seu.sis.model.entity.Result;
import com.seu.sis.sis.SisClient;
import com.seu.sis.task.model.PointValue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-23 21:45
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sis")
public class SisController {
    private final SisClient sisClient;

    @GetMapping("/{points}")
    public Result<Map<String, PointValue>> getValue(@PathVariable String points) {
        try {
            String[] pointArr = points.split(",");
            HashMap<String, PointValue> values = sisClient.getRealDataByNames(Arrays.asList(pointArr));
            return Result.success(values);
        } catch (Exception e) {
            return Result.fail("sis获取失败" + e);
        }
    }

}
