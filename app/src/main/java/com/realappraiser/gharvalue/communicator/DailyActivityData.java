package com.realappraiser.gharvalue.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyActivityData {
    @SerializedName("Activity")
    @Expose
    private String activity;

    @SerializedName("Latitude")
    @Expose
    private double latitude;

    @SerializedName("Longitude")
    @Expose
    private double longitude;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("Time")
    @Expose
    private String time;

    @SerializedName("CaseId")
    @Expose
    private String CaseId;


    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaseId() {
        return CaseId;
    }

    public void setCaseId(String caseId) {
        CaseId = caseId;
    }
}
