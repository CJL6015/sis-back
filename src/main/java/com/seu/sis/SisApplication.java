package com.seu.sis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.seu.sis.dao.mapper")
public class SisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SisApplication.class, args);
    }

}
