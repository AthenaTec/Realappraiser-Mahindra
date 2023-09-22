package com.realappraiser.gharvalue.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EnvExposureCondition {

    @SerializedName("EnvExposureCondition")
    @Expose
    private List<EnvExposureConditionData> envExposureCondition;


    public List<EnvExposureConditionData> getEnvExposureCondition() {
        return envExposureCondition;
    }

    public void setEnvExposureCondition(List<EnvExposureConditionData> envExposureCondition) {
        this.envExposureCondition = envExposureCondition;
    }

    public static class EnvExposureConditionData{

        @Override
        public String toString() {
            return name ;
        }

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Id")
        @Expose
        private int id;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
    }
}
