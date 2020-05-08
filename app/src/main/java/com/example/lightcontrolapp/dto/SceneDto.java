package com.example.lightcontrolapp.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 场景信息
 */
public class SceneDto implements Serializable {
    //ID号 从1开始数起
    private Integer id;
    //场景选中灯的状态值
    private StatusDto statusDtoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StatusDto getStatusDtoList() {
        return statusDtoList;
    }

    public void setStatusDtoList(StatusDto statusDtoList) {
        this.statusDtoList = statusDtoList;
    }
}
