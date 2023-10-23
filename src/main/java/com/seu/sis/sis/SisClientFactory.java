package com.seu.sis.sis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈小黑
 */
@Configuration
@RequiredArgsConstructor
public class SisClientFactory {
    private final SisConfig config;

    @Bean
    public SisClient getSisClient() {
        SisClient sisClient = null;
        try {
            sisClient = new SisClientGDTianrenImpl();
            sisClient.createConnection(config.getIp(), config.getPort(),
                    config.getPath(), config.getUser(), config.getPassword());
        } catch (Exception e) {
            sisClient = null;
        }
        return sisClient;
    }
}
