package com.realappraiser.gharvalue.communicator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ShowFSUIResponse {
    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("data")
    @Expose
    private ArrayList<ShowFSUIResponse.Datum> data = null;

    public void setData(ArrayList<Datum> data) {
        this.data = data;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Datum> getData() {
        return data;
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public static class Datum {


         @SerializedName("FieldName")
        @Expose String fieldName;

        @SerializedName("FieldinUI")
        @Expose String fieldinUI;

        @SerializedName("TemplateID")
        @Expose int templateID;

        @SerializedName("SectionID")
        @Expose int SectionID;

        @SerializedName("sequenceNumber")
        @Expose String sequenceNumber;

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public void setFieldinUI(String fieldinUI) {
            this.fieldinUI = fieldinUI;
        }

        public void setSectionID(int sectionID) {
            SectionID = sectionID;
        }

        public void setTemplateID(int templateID) {
            this.templateID = templateID;
        }

        public void setSequenceNumber(String sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getFieldinUI() {
            return fieldinUI;
        }

        public int getTemplateID() {
            return templateID;
        }

        public int getSectionID() {
            return SectionID;
        }

        public String getSequenceNumber() {
            return sequenceNumber;
        }
    }
}
