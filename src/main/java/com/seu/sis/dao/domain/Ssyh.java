package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName SSYH
 */
@TableName(value ="SSYH")
@Data
public class Ssyh implements Serializable {
    /**
     * 
     */
    private String tagname;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private Double value;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}