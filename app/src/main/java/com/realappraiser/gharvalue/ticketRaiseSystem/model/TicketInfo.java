package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TicketInfo {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private Data data;

    public void setStatus(int status) {
        this.status = status;
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

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public static class Data{
        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("QueryType")
        @Expose
        private int QueryType;

        @SerializedName("Description")
        @Expose
        private String Description;

        @SerializedName("EmailId")
        @Expose
        private String EmailId;

        @SerializedName("SAPID")
        @Expose
        private String SAPID;

        @SerializedName("ContactNumber")
        @Expose
        private String ContactNumber;


        @SerializedName("TicketStatus")
        @Expose
        private int TicketStatus;


        @SerializedName("AssignedTo")
        @Expose
        private int AssignedTo;

        public int getID() {
            return ID;
        }

        public int getQueryType() {
            return QueryType;
        }

        public int getAssignedTo() {
            return AssignedTo;
        }

        public int getTicketStatus() {
            return TicketStatus;
        }

        public String getContactNumber() {
            return ContactNumber;
        }

        public String getDescription() {
            return Description;
        }

        public String getEmailId() {
            return EmailId;
        }

        public String getSAPID() {
            return SAPID;
        }
    }
}
