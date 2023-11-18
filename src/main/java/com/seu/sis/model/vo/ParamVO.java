package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-12 10:14
 */
@Data
@Builder
@AllArgsConstructor
public class ParamVO {
    /**
     * 名称
     */
    private String name;

    /**
     * 单位
     */
    private String unit;

    /**
     * #1
     */
    private Double p1;
    /**
     * #2
     */
    private Double p2;
    /**
     * #3
     */
    private Double p3;
    /**
     * #4
     */
    private Double p4;
}
