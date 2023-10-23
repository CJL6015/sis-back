package com.seu.sis.influx;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-21 21:53
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "influx")
public class InfluxConfig {
    /**
     * token
     */
    private String token;

    /**
     * 地址
     */
    private String url;

    /**
     * 数据桶
     */
    private String bucket;

    /**
     * 测点所属机构
     */
    private String org;

    @Bean
    public InfluxDBClient getInfluxDbClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

}
