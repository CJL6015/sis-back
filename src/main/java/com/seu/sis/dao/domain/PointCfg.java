package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 点号配置
 * @author 陈小黑
 * @TableName point_cfg
 */
@TableName(value ="point_cfg")
@Data
public class PointCfg implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Integer id;

    /**
     * 点号名称
     */
    private String pointId;

    /**
     * 点号描述
     */
    private String description;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}