package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName XBPW
 */
@TableName(value ="XBPW")
@Data
public class Xbpw {
    /**
     * 
     */
    private Double plantn;

    /**
     * 
     */
    private Double pw;

    /**
     * 
     */
    private String des;
}