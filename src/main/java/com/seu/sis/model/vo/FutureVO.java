package com.seu.sis.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author chenjiale
 * @version 1.0
 * @date 2024-05-12 12:30
 */
@Data
@Builder
@AllArgsConstructor
public class FutureVO {
    @JSONField(name="ID")
    @JsonAlias("ID")
    private Integer id;

    /**
     *
     */
    @JSONField(name="TimeTag")
    @JsonAlias("TimeTag")
    private String timetag;

    /**
     *
     */
    @JSONField(name="P1")
    @JsonAlias("P1")
    private Double p1Mw;

    /**
     *
     */
    @JSONField(name="P2")
    @JsonAlias("P2")
    private Double p2Mw;
}
