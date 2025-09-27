package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName ZKSY2
 */
@TableName(value ="ZKSY2")
@Data
public class Zksy2 {
    /**
     * 
     */
    private String sy;

    /**
     * 
     */
    private String jl;

    /**
     * 
     */
    private String tjks;

    /**
     * 
     */
    private String tjjs;

    /**
     * 
     */
    private String dbyjz;

    /**
     * 
     */
    private String dbywdjz;

    /**
     * 
     */
    private String dbyyljz;

    /**
     * 
     */
    private String gbyjz;

    /**
     * 
     */
    private String gbywdjz;

    /**
     * 
     */
    private String gbyyljz;

    /**
     * 
     */
    private String szd;

    private String dbyxjsd;

    private String gbyxjsd;
}