package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName C3Output
 */
@TableName(value ="C3Output")
@Data
public class C3output implements Serializable {
    /**
     * 
     */
    private Double id;

    /**
     * 
     */
    private String tagname;

    /**
     * 
     */
    private String des;

    /**
     * 
     */
    private Double value;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}