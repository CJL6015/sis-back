package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 
 * @TableName ZKSY_Signal
 */
@TableName(value ="ZKSY_Signal")
@Data
public class ZksySignal {
    /**
     * 
     */
    private String tagname;

    /**
     * 
     */
    private Double value;
}