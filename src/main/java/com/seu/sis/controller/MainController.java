package com.seu.sis.controller;

import cn.hutool.core.collection.ListUtil;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.entity.Result;
import com.seu.sis.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-05-12 13:39
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
public class MainController {
    private final InfoService infoService;

    private final InfluxService influxService;

    @GetMapping("/info")
    public Result<List<Map<String, String>>> getInfo() {
        return Result.success(infoService.getInfo());
    }

    @GetMapping("/load/trend")
    public Result<Map<String, List<Object[]>>> getLoadTrend(String st, String et) {
        Map<String, List<Object[]>> history = influxService.getHistory("HJB_XBSS",
                ListUtil.of("U1P_QC", "U2P_QC", "XHSBACZT_1", "XHSBBDZT_1", "XHSBACZT_2", "XHSBBDZT_2"),
                st, et, "5m");
        return Result.success(history);
    }

    @GetMapping("/pump/trend")
    public Result<Map<String, List<Object[]>>> getPumpTrend(String st, String et) {
        Map<String, List<Object[]>> history = influxService.getHistory("HJB_SSYH", ListUtil.of("LXZDSY","SJSY","JZSY","SJXBPWXH","LXXBPWXH"),
                st, et, "5m");
        return Result.success(history);
    }
}
