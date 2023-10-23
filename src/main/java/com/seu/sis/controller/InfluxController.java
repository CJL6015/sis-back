package com.seu.sis.controller;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.HealthCheck;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.model.entity.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

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
}
