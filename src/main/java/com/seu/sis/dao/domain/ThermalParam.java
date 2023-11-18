package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName thermal_param
 */
@TableName(value ="thermal_param")
@Data
public class ThermalParam implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 单位
     */
    private String unit;

    /**
     * #1机组
     */
    private String p1;

    /**
     * #2机组
     */
    private String p2;

    /**
     * #3机组
     */
    private String p3;

    /**
     * #4机组
     */
    private String p4;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}