package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultiBranchModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Datum {

        @SerializedName("BranchName")
        @Expose
        private String branchName;
        @SerializedName("AgencyBranchId")
        @Expose
        private Integer agencyBranchId;

        public Datum(String branchName, Integer agencyBranchId) {
            this.branchName = branchName;
            this.agencyBranchId = agencyBranchId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public Integer getAgencyBranchId() {
            return agencyBranchId;
        }

        public void setAgencyBranchId(Integer agencyBranchId) {
            this.agencyBranchId = agencyBranchId;
        }

        @Override
        public String toString() {
            return branchName;
        }
    }
}
