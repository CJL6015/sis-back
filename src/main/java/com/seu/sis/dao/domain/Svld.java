package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName SVLD
 */
@TableName(value ="SVLD")
@Data
public class Svld {
    /**
     * 
     */
    private String tagname;

    /**
     * 
     */
    private String value;
}