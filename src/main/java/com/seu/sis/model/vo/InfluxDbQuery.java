package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-18 10:57
 */
@Data
@Builder
@AllArgsConstructor
public class InfluxDbQuery {
    private String bucket;
    private String points;
    private String st;
    private String et;
}
