/*
 * @Author: mengzesam@126.com
 * @Date: 2022-11-12 19:16:50
 * @LastEditors: mengzesam@126.com
 * @LastEditTime: 2022-11-13 16:09:04
 * @FilePath: \turbinevalvedaqsis\src\main\java\com\mzs\turbinevalvesis\model\PointValue.java
 * @Description: 
 * 
 * Copyright (c) 2022 by mengzesam@126.com, All Rights Reserved. 
 */
package com.seu.sis.task.model;

import com.gdtianren.rtdb.TagData;

import java.util.Date;


public class PointValue {
    // private int state; 
    private Date time;
    private double value;

    public PointValue(){}

    public PointValue(long time, double value){
        this.value=value;
        this.time=new Date(time);
    }

    public PointValue(Date time, double value){
        this.value=value;
        this.time=time;
    }



    public PointValue(TagData tagData){
        // this.state=tagData.getStatus();
        this.value=tagData.getValue();
        this.time=tagData.getTime();
    }


    public Date getTime(){
        return time;
    }
    public void setTime(Date time){
        this.time=time;
    }

    public double getValue(){
        return value;
    }
    public void setValue(double value){
        this.value=value;
    }

    public String toString(){
        String ans;
        // ans="state:"+state+";time:"+time+";value:"+value;
        ans="time:"+time+";value:"+value;
        return ans;
    }

};
