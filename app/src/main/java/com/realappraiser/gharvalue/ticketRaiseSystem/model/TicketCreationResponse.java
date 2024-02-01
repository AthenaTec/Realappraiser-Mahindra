package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketCreationResponse {

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private TicketCreationResponse.Data data = null;

    public TicketCreationResponse.Data getData() {
        return data;
    }

    public void setData(TicketCreationResponse.Data data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static class Data {
        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("TicketIDVal")
        @Expose
        private int TicketIDVal;

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

        public Data(String name) {
            this.Name = name;
        }

        @Override
        public String toString() {
            return  Name ;
        }

        public void setTicketIDVal(int ticketIDVal) {
            TicketIDVal = ticketIDVal;
        }

        public int getTicketIDVal() {
            return TicketIDVal;
        }
    }
}
