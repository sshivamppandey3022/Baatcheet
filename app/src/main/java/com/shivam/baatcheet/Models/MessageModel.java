package com.shivam.baatcheet.Models;

public class MessageModel {
    String uId, msg;
    long timeStamp;

    public MessageModel(String uId, String msg, long timeStamp) {
        this.uId = uId;
        this.msg = msg;
        this.timeStamp = timeStamp;
    }

    public MessageModel(String uId, String msg) {
        this.uId = uId;
        this.msg = msg;
    }
    public MessageModel(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
