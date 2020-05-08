package com.example.lightcontrolapp.dto;

public class EventBusMessage {

    /*//tyoe =
        1：为场景长按改变分控的界面
        2：效果长按编辑完成，更新效果右上角标识
        3: 设置亮度和色温更新 设置界面说明文字
     */
    private int type ;
    private String message;

    public EventBusMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "EventBusMessage{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
