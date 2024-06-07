package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class TicketQueryDataModel {


    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private ArrayList<TicketQueryDataModel.Data> data = null;

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class Data {
        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("Name")
        @Expose
        private String Name;

        public int getID() {
            return ID;
        }

        public String getName() {
            return Name;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public void setName(String name) {
            Name = name;
        }

        public Data(String name) {
            this.Name = name;
        }

        @Override
        public String toString() {
            return  Name ;
        }
    }
}
