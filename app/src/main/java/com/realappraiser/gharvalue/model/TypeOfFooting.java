package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TypeOfFooting {

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

        public Datum(String name) {
            this.name = name;
        }

        @SerializedName("typeoffootingfoundationId")
        @Expose
        private Integer typeoffootingfoundationId;
        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("CreatedOn")
        @Expose
        private Object createdOn;
        @SerializedName("CreatedBy")
        @Expose
        private Object createdBy;
        @SerializedName("ModifiedOn")
        @Expose
        private Object modifiedOn;
        @SerializedName("ModifiedBy")
        @Expose
        private Object modifiedBy;

        public Integer getTypeoffootingfoundationId() {
            return typeoffootingfoundationId;
        }

        public void setTypeoffootingfoundationId(Integer typeoffootingfoundationId) {
            this.typeoffootingfoundationId = typeoffootingfoundationId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Object createdOn) {
            this.createdOn = createdOn;
        }

        public Object getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(Object createdBy) {
            this.createdBy = createdBy;
        }

        public Object getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(Object modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public Object getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Object modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        @Override
        public String toString() {
            return name ;
        }

    }
}