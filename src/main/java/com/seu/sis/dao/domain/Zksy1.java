package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName ZKSY1
 */
@TableName(value ="ZKSY1")
@Data
public class Zksy1 {
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
}