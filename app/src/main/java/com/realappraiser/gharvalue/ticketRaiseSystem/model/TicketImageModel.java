package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class TicketImageModel {

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private List<TicketImageModel.Data> data;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public List<TicketImageModel.Data> getData() {
        return data;
    }

    public static class Data{
        @SerializedName("ID")
        @Expose
        private int ID;

        @SerializedName("TicketID")
        @Expose
        private int TicketID;

        @SerializedName("FilePath")
        @Expose
        private String FilePath;

        @SerializedName("Image")
        @Expose
        private String Image;

        @SerializedName("FileName")
        @Expose
        private String FileName;

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String fileName) {
            FileName = fileName;
        }

        public void setImage(String image) {
            Image = image;
        }

        public String getImage() {
            return Image;
        }

        public String getFilePath() {
            return FilePath;
        }

        public void setFilePath(String filePath) {
            FilePath = filePath;
        }

        public void setTicketID(int ticketID) {
            TicketID = ticketID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }
    }
}
