package com.realappraiser.gharvalue.ticketRaiseSystem.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

public class TicketRaisePhoto {

    @SerializedName("QueryType")
    @Expose
    private int queryType;

    @SerializedName("Description")
    @Expose
    private String description;

    @SerializedName("EmailId")
    @Expose
    private String emailId;

    @SerializedName("OtherQueries")
    @Expose
    private String otherQueries;

    @SerializedName("SAPID")
    @Expose
    private String SAPID;

    @SerializedName("ContactNumber")
    @Expose
    private String contactNumber;

    @SerializedName("TicketStatus")
    @Expose
    private String ticketStatus;

    @SerializedName("TicketImages")
    @Expose
    private ArrayList<TicketRaisePhoto.Datum> ticketImages;

    public ArrayList<Datum> getTicketImages() {
        return ticketImages;
    }

    public void setTicketImages(ArrayList<Datum> ticketImages) {
        this.ticketImages = ticketImages;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getSAPID() {
        return SAPID;
    }

    public void setSAPID(String SAPID) {
        this.SAPID = SAPID;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public void setOtherQueries(String otherQueries) {
        this.otherQueries = otherQueries;
    }

    public String getOtherQueries() {
        return otherQueries;
    }

    public static class Datum {

        @SerializedName("Id")
        @Expose
        private int id;

        @SerializedName("Image")
        @Expose
        private String image;

        @SerializedName("Title")
        @Expose
        private String title;

        @SerializedName("FileName")
        @Expose
        private String fileName;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
