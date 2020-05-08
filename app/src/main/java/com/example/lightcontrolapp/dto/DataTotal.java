package com.example.lightcontrolapp.dto;

import android.app.Application;

import java.io.Serializable;
import java.util.List;

/**
 * 总数据
 */
public class DataTotal implements Serializable {

    //网关IP群组
    private List<String> ipGroup;
    //初始化流明最大值
    private Integer lmMax;
    //初始化流明最小值
    private Integer lmMin;
    //初始化色温最大值
    private Integer tpMax;
    //初始化色温最小值
    private Integer tpMin;
    //场景信息
    private List<SceneDto>  sceneDtoList;
    //效果信息
    private List<EffecDto> effecDtoList;


    public Integer getLmMax() {
        return lmMax;
    }

    public void setLmMax(Integer lmMax) {
        this.lmMax = lmMax;
    }

    public Integer getLmMin() {
        return lmMin;
    }

    public void setLmMin(Integer lmMin) {
        this.lmMin = lmMin;
    }

    public Integer getTpMax() {
        return tpMax;
    }

    public void setTpMax(Integer tpMax) {
        this.tpMax = tpMax;
    }

    public Integer getTpMin() {
        return tpMin;
    }

    public void setTpMin(Integer tpMin) {
        this.tpMin = tpMin;
    }

    public List<String> getIpGroup() {
        return ipGroup;
    }

    public void setIpGroup(List<String> ipGroup) {
        this.ipGroup = ipGroup;
    }

    public List<SceneDto> getSceneDtoList() {
        return sceneDtoList;
    }

    public void setSceneDtoList(List<SceneDto> sceneDtoList) {
        this.sceneDtoList = sceneDtoList;
    }

    public List<EffecDto> getEffecDtoList() {
        return effecDtoList;
    }

    public void setEffecDtoList(List<EffecDto> effecDtoList) {
        this.effecDtoList = effecDtoList;
    }
}
