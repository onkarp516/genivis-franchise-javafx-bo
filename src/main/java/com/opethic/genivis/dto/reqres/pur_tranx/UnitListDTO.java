package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class UnitListDTO{

    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("unitId")
    @Expose
    private Integer unitId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("unitName")
    @Expose
    private String unitName;
    @SerializedName("unitCode")
    @Expose
    private String unitCode;
    @SerializedName("unitConversion")
    @Expose
    private String unitConversion;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(String unitConversion) {
        this.unitConversion = unitConversion;
    }
}