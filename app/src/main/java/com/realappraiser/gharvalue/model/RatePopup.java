package com.realappraiser.gharvalue.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RatePopup {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("msg")
    @Expose
    private String msg;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public class Data {

        @SerializedName("ParentCaseId")
        @Expose
        private Integer parentCaseId;
        @SerializedName("CurrentLat")
        @Expose
        private Float currentLat;
        @SerializedName("CurrentLong")
        @Expose
        private Float currentLong;
        @SerializedName("Type")
        @Expose
        private Integer type;
        @SerializedName("PropertyCategoryId")
        @Expose
        private Integer propertyCategoryId;
        @SerializedName("MinAmount")
        @Expose
        private Float minAmount;
        @SerializedName("MaxAmount")
        @Expose
        private Float maxAmount;
        @SerializedName("lstProprtylatlong")
        @Expose
        private List<LstProprtylatlong> lstProprtylatlong = null;

        public Integer getParentCaseId() {
            return parentCaseId;
        }

        public void setParentCaseId(Integer parentCaseId) {
            this.parentCaseId = parentCaseId;
        }

        public Float getCurrentLat() {
            return currentLat;
        }

        public void setCurrentLat(Float currentLat) {
            this.currentLat = currentLat;
        }

        public Float getCurrentLong() {
            return currentLong;
        }

        public void setCurrentLong(Float currentLong) {
            this.currentLong = currentLong;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getPropertyCategoryId() {
            return propertyCategoryId;
        }

        public void setPropertyCategoryId(Integer propertyCategoryId) {
            this.propertyCategoryId = propertyCategoryId;
        }

        public Float getMinAmount() {
            return minAmount;
        }

        public void setMinAmount(Float minAmount) {
            this.minAmount = minAmount;
        }

        public Float getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(Float maxAmount) {
            this.maxAmount = maxAmount;
        }

        public List<LstProprtylatlong> getLstProprtylatlong() {
            return lstProprtylatlong;
        }

        public void setLstProprtylatlong(List<LstProprtylatlong> lstProprtylatlong) {
            this.lstProprtylatlong = lstProprtylatlong;
        }
        public class LstProprtylatlong {

            @SerializedName("CaseId")
            @Expose
            private Integer caseId;
            @SerializedName("PropertyId")
            @Expose
            private Integer propertyId;
            @SerializedName("PropertyCategoryId")
            @Expose
            private Integer propertyCategoryId;
            @SerializedName("Lat")
            @Expose
            private Float lat;
            @SerializedName("Long")
            @Expose
            private Float _long;
            @SerializedName("Distance")
            @Expose
            private Float distance;
            @SerializedName("LandRate")
            @Expose
            private Float landRate;
            @SerializedName("YardLandRate")
            @Expose
            private Float yardLandRate;
            @SerializedName("SquareFeetLandRate")
            @Expose
            private Float squareFeetLandRate;
            @SerializedName("FlatRate")
            @Expose
            private Float flatRate;
            @SerializedName("YardFlatRate")
            @Expose
            private Float yardFlatRate;
            @SerializedName("SquareFeetFlatRate")
            @Expose
            private Float squareFeetFlatRate;
            @SerializedName("MeasurementUnit")
            @Expose
            private Integer measurementUnit;
            @SerializedName("CreatedOn")
            @Expose
            private String createdOn;

            public Integer getCaseId() {
                return caseId;
            }

            public void setCaseId(Integer caseId) {
                this.caseId = caseId;
            }

            public Integer getPropertyId() {
                return propertyId;
            }

            public void setPropertyId(Integer propertyId) {
                this.propertyId = propertyId;
            }

            public Integer getPropertyCategoryId() {
                return propertyCategoryId;
            }

            public void setPropertyCategoryId(Integer propertyCategoryId) {
                this.propertyCategoryId = propertyCategoryId;
            }

            public Float getLat() {
                return lat;
            }

            public void setLat(Float lat) {
                this.lat = lat;
            }

            public Float getLong() {
                return _long;
            }

            public void setLong(Float _long) {
                this._long = _long;
            }

            public Float getDistance() {
                return distance;
            }

            public void setDistance(Float distance) {
                this.distance = distance;
            }

            public Float getLandRate() {
                return landRate;
            }

            public void setLandRate(Float landRate) {
                this.landRate = landRate;
            }

            public Float getYardLandRate() {
                return yardLandRate;
            }

            public void setYardLandRate(Float yardLandRate) {
                this.yardLandRate = yardLandRate;
            }

            public Float getSquareFeetLandRate() {
                return squareFeetLandRate;
            }

            public void setSquareFeetLandRate(Float squareFeetLandRate) {
                this.squareFeetLandRate = squareFeetLandRate;
            }

            public Float getFlatRate() {
                return flatRate;
            }

            public void setFlatRate(Float flatRate) {
                this.flatRate = flatRate;
            }

            public Float getYardFlatRate() {
                return yardFlatRate;
            }

            public void setYardFlatRate(Float yardFlatRate) {
                this.yardFlatRate = yardFlatRate;
            }

            public Float getSquareFeetFlatRate() {
                return squareFeetFlatRate;
            }

            public void setSquareFeetFlatRate(Float squareFeetFlatRate) {
                this.squareFeetFlatRate = squareFeetFlatRate;
            }

            public Integer getMeasurementUnit() {
                return measurementUnit;
            }

            public void setMeasurementUnit(Integer measurementUnit) {
                this.measurementUnit = measurementUnit;
            }

            public String getCreatedOn() {
                return createdOn;
            }

            public void setCreatedOn(String createdOn) {
                this.createdOn = createdOn;
            }

        }
    }
}