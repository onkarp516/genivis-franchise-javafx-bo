package com.opethic.genivis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UnitRateList implements Serializable {
    @SerializedName("unitid")
    @Expose
    private Long unitId;
    @SerializedName("unitName")
    @Expose
    private String unitName;
    @SerializedName("unitConv")
    @Expose
    private Double unitConv;
    @SerializedName("fsrmh")
    @Expose
    private Double fsrmh;
    @SerializedName("fsrai")
    @Expose
    private Double fsrai;
    @SerializedName("csrmh")
    @Expose
    private Double csrmh;
    @SerializedName("csrai")
    @Expose
    private Double csrai;
    @SerializedName("mrp")
    @Expose
    private Double mrp;
    @SerializedName("purchaserate")
    @Expose
    private Double purRate;

    public UnitRateList() {
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(Double unitConv) {
        this.unitConv = unitConv;
    }

    public Double getFsrmh() {
        return fsrmh;
    }

    public void setFsrmh(Double fsrmh) {
        this.fsrmh = fsrmh;
    }

    public Double getFsrai() {
        return fsrai;
    }

    public void setFsrai(Double fsrai) {
        this.fsrai = fsrai;
    }

    public Double getCsrmh() {
        return csrmh;
    }

    public void setCsrmh(Double csrmh) {
        this.csrmh = csrmh;
    }

    public Double getCsrai() {
        return csrai;
    }

    public void setCsrai(Double csrai) {
        this.csrai = csrai;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getPurRate() {
        return purRate;
    }

    public void setPurRate(Double purRate) {
        this.purRate = purRate;
    }

    @Override
    public String toString() {
        return "UnitRateList{" +
                "unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", unitConv=" + unitConv +
                ", fsrmh=" + fsrmh +
                ", fsrai=" + fsrai +
                ", csrmh=" + csrmh +
                ", csrai=" + csrai +
                '}';
    }
}
