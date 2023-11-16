package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TypeOfMasonry {

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

        @SerializedName("typeofmasonryId")
        @Expose
        private Integer typeofmasonryId = 0;
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
        private String modifiedOn;
        @SerializedName("ModifiedBy")
        @Expose
        private Object modifiedBy;

        @SerializedName("Id")
        @Expose
        private Integer id;

        public Integer getTypeofmasonryId() {
            return typeofmasonryId;
        }

        public void setTypeofmasonryId(Integer typeofmasonryId) {
            this.typeofmasonryId = typeofmasonryId;
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

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

}