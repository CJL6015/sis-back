package com.seu.sis.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.HealthCheck;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.influx.InfluxService;
import com.seu.sis.model.entity.Result;
import com.seu.sis.model.vo.InfluxDbQuery;
import com.seu.sis.model.vo.TrendVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.seu.sis.common.constant.StringConstant.VALUE_KEY;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-23 22:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/influx")
public class InfluxController {

    private final InfluxDBClient influxDBClient;

    private final InfluxConfig config;

    private final InfluxService influxService;


    @GetMapping("/{point}")
    public Result<String> getValue(@PathVariable String point) {
        return Result.success();
    }


    @GetMapping("/write/double")
    public Result<String> setValue(String key, Double value) {
        Point point = Point.measurement(key)
                .addField(VALUE_KEY, value)
                .time(Instant.now(), WritePrecision.NS);
        WriteApi writeApi = influxDBClient.getWriteApi();
        writeApi.writePoint(config.getBucket(), config.getOrg(), point);
        return Result.success("写入成功");
    }


    @GetMapping("/status")
    public Result<HealthCheck.StatusEnum> isConnect() {
        HealthCheck health = influxDBClient.health();
        return Result.success(health.getStatus());
    }

    @GetMapping("/now/list")
    public Result<Map<String, Double>> listNow(String bucket, String measurements) {
        Map<String, Double> groupNow = influxService.readGroupNow(bucket,
                Arrays.asList(measurements.split(",")));
        return Result.success(groupNow);
    }

    @GetMapping("/history")
    public Result<Map<String, List<Object[]>>> getHistoryGroup(InfluxDbQuery query) {
        String[] points = query.getPoints().split(",");
        Map<String, List<Object[]>> history = influxService.getHistory(query.getBucket(),
                Arrays.asList(points), query.getSt(), query.getEt());
        return Result.success(history);
    }
}
