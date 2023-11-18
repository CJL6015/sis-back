package com.seu.sis.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.seu.sis.dao.service.PointCfgService;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.sis.SisClient;
import com.seu.sis.task.model.PointValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-21 21:11
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PointDataTask {
    private static final String VALUE_KEY = "value";
    private static final String TXT_PATH = "E:\\point\\point.txt";


    private final SisClient sisClient;

    private final InfluxDBClient influxDBClient;

    private final PointCfgService pointCfgService;

    private final InfluxConfig config;

//    @Scheduled(fixedRate = 10000)
    public void test() {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query("from(bucket:\"FC_XBSS\") |> range(start: -10m)|> filter(fn: (r) " +
                "=> r[\"_measurement\"] == \"P_1\")", config.getOrg());
        List<FluxRecord> records = query.get(0).getRecords();
        double value =(double) records.get(records.size() - 1).getValues().get("_value");
        System.out.println(1);
    }


    /**
     * 从sis中获取点号的值,并写入实时数据库中
     * 每10秒执行一次
     */
//    @Scheduled(fixedRate = 10000)
    public void fetchAndStorePointData() {
        try {
//            List<PointCfg> list = pointCfgService.list();
//            List<String> names = list.stream().map(PointCfg::getPointId).collect(Collectors.toList());
            String s = FileUtil.readUtf8String(TXT_PATH);
            String[] before = s.split(",");
            List<String> names = Arrays.asList(before);
            log.info("本次同步点号:{}", names);
            if (CollectionUtil.isEmpty(names)) {
                log.info("数据库测点为空");
                return;
            }
            log.info("从SIS同步到InfluxDB开始,共{}个测点", names.size());
            HashMap<String, PointValue> realDataByNames = sisClient.getRealDataByNames(names);
            if (CollectionUtil.isEmpty(realDataByNames)) {
                log.error("sis读取时实值异常");
                return;
            }
            log.info("sis读取结果:{}", realDataByNames);
            List<Point> points = new ArrayList<>();
            List<String> exits = new ArrayList<>();
            realDataByNames.forEach((key, value) -> {
                Point point = Point.measurement(key)
                        .addField(VALUE_KEY, value.getValue())
                        .time(Instant.now(), WritePrecision.NS);
                points.add(point);
                exits.add(key);
            });

            if (names.size() != exits.size()) {
                List<String> remain = Arrays.stream(before)
                        .filter(e -> !exits.contains(e))
                        .collect(Collectors.toList());
                log.info("{}测点未获取到值", remain);
            }
            WriteApi writeApi = influxDBClient.getWriteApi();
            writeApi.writePoints(config.getBucket(), config.getOrg(), points);
            log.info("从SIS同步到InfluxDB结束,共{}测点", points.size());
        } catch (Exception e) {
            log.error("从sis同步数据到influx异常", e);
        }
    }
}
