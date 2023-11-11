package com.seu.sis.dao.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName XBSS_Input
 */
@TableName(value ="XBSS_Input")
@Data
public class XbssInput implements Serializable {
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
    private String varname;

    /**
     * 
     */
    private String des;

    /**
     * 
     */
    private String unit;

    /**
     * 
     */
    private Double plant;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}