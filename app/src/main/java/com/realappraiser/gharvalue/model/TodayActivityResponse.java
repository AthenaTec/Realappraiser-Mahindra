package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class TodayActivityResponse {
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private Datum data = null;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    public static class Datum {
        @SerializedName("DateofTravel")
        @Expose
        private String DateofTravel;

        @SerializedName("TotalDistance")
        @Expose
        private double TotalDistance;

        @SerializedName("StateName")
        @Expose
        private String StateName;

        @SerializedName("BranchName")
        @Expose
        private String BranchName;

        @SerializedName("SAPcode")
        @Expose
        private String SAPcode;

        @SerializedName("EmployeeName")
        @Expose
        private String EmployeeName;




        @SerializedName("NoOfActivities")
        @Expose
        private int NoOfActivities;

        @SerializedName("Activities")
        @Expose
        private ArrayList<DailyActivityData> Activities;

        public String getDateofTravel() {
            return DateofTravel;
        }

        public void setDateofTravel(String dateofTravel) {
            DateofTravel = dateofTravel;
        }

        public double getTotalDistance() {
            return TotalDistance;
        }

        public void setTotalDistance(double totalDistance) {
            TotalDistance = totalDistance;
        }

        public int getNoOfActivities() {
            return NoOfActivities;
        }

        public void setNoOfActivities(int noOfActivities) {
            NoOfActivities = noOfActivities;
        }

        public ArrayList<DailyActivityData> getActivities() {
            return Activities;
        }

        public void setActivities(ArrayList<DailyActivityData> activities) {
            Activities = activities;
        }

        public String getEmployeeName() {
            return EmployeeName;
        }

        public void setEmployeeName(String employeeName) {
            EmployeeName = employeeName;
        }

        public String getSAPcode() {
            return SAPcode;
        }

        public void setSAPcode(String SAPcode) {
            this.SAPcode = SAPcode;
        }

        public String getBranchName() {
            return BranchName;
        }

        public void setBranchName(String branchName) {
            BranchName = branchName;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String stateName) {
            StateName = stateName;
        }
    }
}
