package com.seu.sis.model.vo;

import lombok.*;

import java.util.List;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2023-10-04 9:37
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrendVO<K, V> {
    /**
     * 时间
     */
    private List<K> times;

    /**
     * 值
     */
    private List<V> value;
}
