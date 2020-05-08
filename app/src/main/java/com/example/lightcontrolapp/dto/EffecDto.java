package com.example.lightcontrolapp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 效果内容
 */
public class EffecDto implements Serializable {
    //ID号  从1开始数起
    private Integer id;
    //延时
    private Date delay;
    //定时时间
    private Date time;
    //起始流明度
    private Integer lmBegin;
    //结束流明度
    private Integer lmEnd;
    //起始色温值
    private Integer tpBegin;
    //结束色温值
    private Integer tpEnd;
    //选中灯具号（通道号）
    private List<Integer> selectLightNum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDelay() {
        return delay;
    }

    public void setDelay(Date delay) {
        this.delay = delay;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getLmBegin() {
        return lmBegin;
    }

    public void setLmBegin(Integer lmBegin) {
        this.lmBegin = lmBegin;
    }

    public Integer getLmEnd() {
        return lmEnd;
    }

    public void setLmEnd(Integer lmEnd) {
        this.lmEnd = lmEnd;
    }

    public Integer getTpBegin() {
        return tpBegin;
    }

    public void setTpBegin(Integer tpBegin) {
        this.tpBegin = tpBegin;
    }

    public Integer getTpEnd() {
        return tpEnd;
    }

    public void setTpEnd(Integer tpEnd) {
        this.tpEnd = tpEnd;
    }

    public List<Integer> getSelectLightNum() {
        return selectLightNum;
    }

    public void setSelectLightNum(List<Integer> selectLightNum) {
        this.selectLightNum = selectLightNum;
    }



}
