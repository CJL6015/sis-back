package com.seu.sis.controller;

import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.CalculateDataVO;
import com.seu.sis.model.vo.CalculateParam;
import com.seu.sis.service.CalculateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/calculate")
@RequiredArgsConstructor
public class CalculateController {
    private final CalculateService calculateService;

    @GetMapping("/data")
    public Result<CalculateDataVO> getCalculateData(CalculateParam calculateDataVO) {
        return Result.success(calculateService.getCalculateDataVO(calculateDataVO));
    }

    @GetMapping("/realtime")
    public Result<List<CalculateService.DataRow>> getRealtimeData() {
        return Result.success(calculateService.getRealtimeData());
    }
}
