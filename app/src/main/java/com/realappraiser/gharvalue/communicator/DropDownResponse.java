package com.realappraiser.gharvalue.communicator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.realappraiser.gharvalue.model.TypeOfSteel;

import java.util.List;

public class DropDownResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<TypeOfSteel.Datum> data = null;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TypeOfSteel.Datum> getData() {
        return data;
    }

    public void setData(List<TypeOfSteel.Datum> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public static class Datum {

        public Datum() {

        }

        @SerializedName("PurposeofloanId")
        @Expose
        private Integer PurposeofloanId;

        @SerializedName("TypeOfPropertyId")
        @Expose
        private Integer TypeOfPropertyId;

        @SerializedName("LoanTypeId")
        @Expose
        private Integer LoanTypeId;
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

        public Integer getTypeOfPropertyId() {
            return TypeOfPropertyId;
        }

        @Override
        public String toString() {
            return name ;
        }

        public void setPurposeofloanId(Integer purposeofloanId) {
            PurposeofloanId = purposeofloanId;
        }

        public Integer getPurposeofloanId() {
            return PurposeofloanId;
        }

        public void setTypeOfPropertyId(Integer typeOfPropertyId) {
            TypeOfPropertyId = typeOfPropertyId;
        }

        public void setLoanTypeId(Integer loanTypeId) {
            LoanTypeId = loanTypeId;
        }

        public Integer getLoanTypeId() {
            return LoanTypeId;
        }
    }
}
