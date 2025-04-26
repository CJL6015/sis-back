package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName C4_Output
 */
@TableName(value ="C4Output")
@Data
public class C4Output implements Serializable {
    /**
     * 
     */
    private Integer id;

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