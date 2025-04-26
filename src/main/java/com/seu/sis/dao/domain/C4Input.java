package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName C4Input
 */
@TableName(value ="C4Input")
@Data
public class C4Input {
    /**
     * 
     */
    private Double id;

    /**
     * 
     */
    private String timetag;

    /**
     * 
     */
    private Double p1Mw;

    /**
     * 
     */
    private Double p2Mw;
}