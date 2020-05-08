package com.example.lightcontrolapp.dto;

import java.io.Serializable;

/**
 * 灯的状态值
 */
public class StatusDto implements Serializable {
    //灯具号  从1开始数起
    private Integer id;
    //亮度流明值
    private Integer lmVal;
    //色温值
    private Integer tpVal;

    private Boolean isSelect;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLmVal() {
        return lmVal;
    }

    public void setLmVal(Integer lmVal) {
        this.lmVal = lmVal;
    }

    public Integer getTpVal() {
        return tpVal;
    }

    public void setTpVal(Integer tpVal) {
        this.tpVal = tpVal;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }


}
