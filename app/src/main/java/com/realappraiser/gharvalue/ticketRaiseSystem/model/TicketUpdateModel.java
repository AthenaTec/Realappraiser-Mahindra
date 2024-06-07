package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketUpdateModel {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private String data;

    @SerializedName("msg")
    @Expose
    private String msg;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public int getStatus() {
        return status;
    }

    public String getData() {
        return data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
