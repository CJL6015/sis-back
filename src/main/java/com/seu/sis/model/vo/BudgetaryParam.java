package com.seu.sis.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-11-18 9:42
 */
@Data
@Builder
@AllArgsConstructor
public class BudgetaryParam {
    private Double electricity;
    private Double coal;
    private Double count;
    private Double temperature;
}
