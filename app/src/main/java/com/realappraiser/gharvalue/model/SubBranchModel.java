package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class SubBranchModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private ArrayList<Datum> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class Datum {
        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("AgencyBranchId")
        @Expose
        private Integer agencyBranchId;
        @SerializedName("SubBranch")
        @Expose
        private String subBranch;
        @SerializedName("ModifiedOn")
        @Expose
        private String modifiedOn;
        @SerializedName("CreatedBy")
        @Expose
        private Integer createdBy;
        @SerializedName("Status")
        @Expose
        private Object status;

        public Datum(Integer id, Integer agencyBranchId, String subBranch) {
            this.id = id;
            this.agencyBranchId = agencyBranchId;
            this.subBranch = subBranch;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getAgencyBranchId() {
            return agencyBranchId;
        }

        public void setAgencyBranchId(Integer agencyBranchId) {
            this.agencyBranchId = agencyBranchId;
        }

        public String getSubBranch() {
            return subBranch;
        }

        public void setSubBranch(String subBranch) {
            this.subBranch = subBranch;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public Integer getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Integer createdBy) {
            this.createdBy = createdBy;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }


        @Override
        public String toString() {
            return subBranch ;
        }
    }
}
