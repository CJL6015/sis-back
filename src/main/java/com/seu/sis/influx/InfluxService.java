package com.seu.sis.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxTable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-11 20:41
 */
@Service
@RequiredArgsConstructor
public class InfluxService {
    private final InfluxDBClient influxDBClient;

    public void read(String bucket, String point) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> query = queryApi.query("from(bucket:\""
                + bucket + "\") |> range(start: -1m)|> filter(fn: (r) " +
                "=> r[\"_measurement\"] == \"" + point + "\")");
    }
}
