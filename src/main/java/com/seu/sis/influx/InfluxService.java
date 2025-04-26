package com.seu.sis.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.seu.sis.common.constant.StringConstant.VALUE_KEY;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-11 20:41
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class InfluxService {
    private static final String QUERY_FORMAT = "from(bucket:\"%s\") " +
            "|> range(start:-10m) " +
            "|> filter(fn: (r) => %s) " +
            "|> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
            "|> aggregateWindow(every: 1h, fn: last, createEmpty: false)";
    private static final String QUERY_HISTORY_FORMAT = "from(bucket:\"%s\") " +
            "|> range(start:%s , stop:%s) " +
            "|> filter(fn: (r) => %s) " +
            "|> filter(fn: (r) => r[\"_field\"] == \"value\")\n" +
            "|> aggregateWindow(every: %s, fn: last, createEmpty: false)";
    private static final String MEASUREMENT_QUERY = "r[\"_measurement\"] == \"%s\"";
    private final InfluxConfig config;
    private final InfluxDBClient influxDBClient;

    public double read(String bucket, String point) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query("from(bucket:\"" + bucket + "\") |> range(start: -10m)|> filter(fn: (r) " +
                "=> r[\"_measurement\"] == \"" + point + "\")", config.getOrg());
        List<FluxRecord> records = query.get(0).getRecords();
        return (double) records.get(records.size() - 1).getValues().get("_value");
    }

    public void readBucket(String bucket) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        long t1 = System.currentTimeMillis();
        List<FluxTable> query = queryApi.query("from(bucket:\"" + bucket + "\") |> range(start: -1m)", config.getOrg());
        long t2 = System.currentTimeMillis();
        System.out.println(t2 - t1);
        System.out.println(1);
    }

    public Map<String, List<Object[]>> getHistory(String bucket, List<String> points,
                                                  String startTime, String endTime, String period) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        String measurements = points.stream()
                .map(p -> String.format(MEASUREMENT_QUERY, p))
                .collect(Collectors.joining(" or "));
        List<FluxTable> query = queryApi.query(String.format(QUERY_HISTORY_FORMAT, bucket, startTime,
                endTime, measurements, period), config.getOrg());
        Map<String, List<Object[]>> result = new HashMap<>(32);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        query.forEach(fluxTable -> {
            List<FluxRecord> records = fluxTable.getRecords();
            List<Object[]> trendList = new ArrayList<>();
            for (FluxRecord record : records) {
                Map<String, Object> values = record.getValues();
                Object[] trend = new Object[2];
                String time = values.get("_time").toString();
                try {
                    trend[0] = outputFormat.format(inputFormat.parse(time));
                } catch (ParseException e) {
                    log.error("解析时间异常:{}", time, e);
                    trend[0] = time;

                }
                trend[1] = (Double) values.get("_value");
                trendList.add(trend);
            }
            FluxRecord fluxRecord = records.get(0);
            Map<String, Object> values = fluxRecord.getValues();
            result.put((String) values.get("_measurement"), trendList);
        });
        return result;
    }

    public Map<String, Double> readGroupNow(String bucket, List<String> points) {
        String measurements = points.stream()
                .map(p -> String.format(MEASUREMENT_QUERY, p))
                .collect(Collectors.joining(" or "));
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query(String.format(QUERY_FORMAT, bucket, measurements), config.getOrg());
        Map<String, Double> res = new HashMap<>(64);
        query.forEach(fluxTable -> {
            List<FluxRecord> records = fluxTable.getRecords();
            FluxRecord fluxRecord = records.get(records.size() - 1);
            Map<String, Object> values = fluxRecord.getValues();
            res.put((String) values.get("_measurement"), (Double) values.get("_value"));
        });
        return res;
    }

    public void write(String key, Double value, String bucket) {
        Point point = Point.measurement(key)
                .addField(VALUE_KEY, value)
                .time(Instant.now(), WritePrecision.NS);
        WriteApi writeApi = influxDBClient.getWriteApi();
        writeApi.writePoint(bucket, config.getOrg(), point);
    }
}
