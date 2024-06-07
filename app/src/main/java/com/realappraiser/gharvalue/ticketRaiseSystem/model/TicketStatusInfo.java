package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketStatusInfo {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    public static class Status {
        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("Name")
        @Expose
        private String Name;

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getID() {
            return ID;
        }

        public String getName() {
            return Name;
        }

        public Status(String name) {
            this.Name = name;
        }

        @NonNull
        @Override
        public String toString() {
            return Name;
        }
    }


    @SerializedName("data")
    @Expose
    private List<Status> dataList;

    public List<Status> getDataList() {
        return dataList;
    }

    public void setDataList(List<Status> dataList) {
        this.dataList = dataList;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }
}
