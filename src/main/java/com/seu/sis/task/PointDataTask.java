package com.seu.sis.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.seu.sis.influx.InfluxConfig;
import com.seu.sis.sis.SisClient;
import com.seu.sis.sis.SisClientFactory;
import com.seu.sis.sis.SisConfig;
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
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-21 21:11
 */
@Slf4j
//@Component
@RequiredArgsConstructor
public class PointDataTask {
    private static final String VALUE_KEY = "value";
    private static final String TXT_PATH = "E:\\point\\point.txt";
    private final InfluxDBClient influxDBClient;
    private final InfluxConfig config;
    private final SisConfig sisConfig;
    private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(4, 4,
            1, TimeUnit.MINUTES, new LinkedBlockingDeque<>(20),
            new ThreadFactoryBuilder().setNamePrefix("sis-").build());
    private SisClient sisClient = null;



    /**
     * 从sis中获取点号的值,并写入实时数据库中
     * 每10秒执行一次
     */
//    @Scheduled(fixedRate = 10000)
    public void doTask() {
        if (sisClient == null) {
            sisClient = SisClientFactory.getSisClient(sisConfig);
        }
        Future<?> future = executorService.submit(this::fetchAndStorePointData);
        try {
            Object o = future.get(5, TimeUnit.SECONDS);
            boolean result = Boolean.getBoolean(o.toString());
            if (!result) {
                log.error("同步sis结果为异常");
                closeSisClient();
            }
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            log.error("sis同步测点异常", e);
            future.cancel(true);
            closeSisClient();
        }
    }

    public void closeSisClient() {
        try {
            sisClient.releaseConnection();
        } catch (Exception ex) {
            log.error("关闭sis客户端异常", ex);
        }
        sisClient = null;
    }


    public boolean fetchAndStorePointData() {
        try {
            String s = FileUtil.readUtf8String(TXT_PATH);
            String[] before = s.split(",");
            List<String> names = Arrays.asList(before);
            log.info("本次同步点号:{}", names);
            if (CollectionUtil.isEmpty(names)) {
                log.info("数据库测点为空");
                return true;
            }
            log.info("从SIS同步到InfluxDB开始,共{}个测点", names.size());
            HashMap<String, PointValue> realDataByNames = sisClient.getRealDataByNames(names);
            if (CollectionUtil.isEmpty(realDataByNames)) {
                log.error("sis读取时实值异常");
                return false;
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
            return true;
        } catch (Exception e) {
            log.error("从sis同步数据到influx异常", e);
            return false;
        }
    }
}
