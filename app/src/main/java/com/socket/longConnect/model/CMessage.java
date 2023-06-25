package com.socket.longConnect.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CMessage implements Serializable {
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    @SerializedName("code")
    private int code;
    @SerializedName("type")
    private int type;
    @SerializedName("msg")
    private String msg;


    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
