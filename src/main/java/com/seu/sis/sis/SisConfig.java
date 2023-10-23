package com.seu.sis.sis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-21 21:15
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sis")
public class SisConfig {

    /**
     * sis ip
     */
    private String ip;

    /**
     * sis 端口
     */
    private int port;

    /**
     * path
     */
    private String path;

    /**
     * 用户
     */
    private String user;

    /**
     * 密码
     */
    private String password;

}
