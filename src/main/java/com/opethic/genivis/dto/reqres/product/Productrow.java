package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**** uses in GET BY PRODUCT ID  ****/
public class Productrow {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("selectedLevelA")
    @Expose
    private String selectedLevelA;
    @SerializedName("selectedLevelB")
    @Expose
    private String selectedLevelB;
    @SerializedName("selectedLevelC")
    @Expose
    private String selectedLevelC;
    @SerializedName("selectedUnit")
    @Expose
    private String selectedUnit;
    @SerializedName("conv")
    @Expose
    private Double conv;
    @SerializedName("unit_marg")
    @Expose
    private Double unitMarg;
    @SerializedName("mrp")
    @Expose
    private Double mrp;
    @SerializedName("pur_rate")
    @Expose
    private Double purRate;
    @SerializedName("rate_1")
    @Expose
    private Double rate1;
    @SerializedName("rate_2")
    @Expose
    private Double rate2;
    @SerializedName("rate_3")
    @Expose
    private Double rate3;

    @SerializedName("rate_4")
    @Expose
    private Double rate4 = 0.0;
    @SerializedName("min_qty")
    @Expose
    private Double minQty;
    @SerializedName("max_qty")
    @Expose
    private Double maxQty;
    @SerializedName("is_negetive")
    @Expose
    private Boolean isNegetive;

    @SerializedName("is_rate")
    @Expose
    private Boolean isRate;
    @SerializedName("batchList")
    @Expose
    private List<ProductBatchList> batchList;


    public Boolean getIsRate() {
        return isRate;
    }

    public void setIsRate(Boolean rate) {
        isRate = rate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelectedLevelA() {
        return selectedLevelA;
    }

    public void setSelectedLevelA(String selectedLevelA) {
        this.selectedLevelA = selectedLevelA;
    }

    public String getSelectedLevelB() {
        return selectedLevelB;
    }

    public void setSelectedLevelB(String selectedLevelB) {
        this.selectedLevelB = selectedLevelB;
    }

    public String getSelectedLevelC() {
        return selectedLevelC;
    }

    public void setSelectedLevelC(String selectedLevelC) {
        this.selectedLevelC = selectedLevelC;
    }

    public String getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public Double getConv() {
        return conv;
    }

    public void setConv(Double conv) {
        this.conv = conv;
    }

    public Double getUnitMarg() {
        return unitMarg;
    }

    public void setUnitMarg(Double unitMarg) {
        this.unitMarg = unitMarg;
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

    public Double getRate1() {
        return rate1;
    }

    public void setRate1(Double rate1) {
        this.rate1 = rate1;
    }

    public Double getRate2() {
        return rate2;
    }

    public void setRate2(Double rate2) {
        this.rate2 = rate2;
    }

    public Double getRate3() {
        return rate3;
    }

    public void setRate3(Double rate3) {
        this.rate3 = rate3;
    }


    public Double getRate4() {
        return rate4;
    }

    public void setRate4(Double rate4) {
        this.rate4 = rate4;
    }

    public Double getMinQty() {
        return minQty;
    }

    public void setMinQty(Double minQty) {
        this.minQty = minQty;
    }

    public Double getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Double maxQty) {
        this.maxQty = maxQty;
    }

    public Boolean getIsNegetive() {
        return isNegetive;
    }

    public void setIsNegetive(Boolean isNegetive) {
        this.isNegetive = isNegetive;
    }

    public List<ProductBatchList> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<ProductBatchList> batchList) {
        this.batchList = batchList;
    }

    @Override
    public String toString() {
        return "Productrow{" +
                "id=" + id +
                ", selectedLevelA='" + selectedLevelA + '\'' +
                ", selectedLevelB='" + selectedLevelB + '\'' +
                ", selectedLevelC='" + selectedLevelC + '\'' +
                ", selectedUnit='" + selectedUnit + '\'' +
                ", conv=" + conv +
                ", unitMarg=" + unitMarg +
                ", mrp=" + mrp +
                ", purRate=" + purRate +
                ", rate1=" + rate1 +
                ", rate2=" + rate2 +
                ", rate3=" + rate3 +
                ", rate4=" + rate4 +
                ", minQty=" + minQty +
                ", maxQty=" + maxQty +
                ", isNegetive=" + isNegetive +
                ", isRate=" + isRate +
                '}';
    }
}
