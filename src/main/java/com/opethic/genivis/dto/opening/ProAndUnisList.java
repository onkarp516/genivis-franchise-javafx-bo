package com.opethic.genivis.dto.opening;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProAndUnisList {

    @SerializedName("product_id")
    @Expose
    private Integer productId;

    @SerializedName("product_name")
    @Expose
    private String productName;

    @SerializedName("unit_id")
    @Expose
    private Integer unitId;

    @SerializedName("unit_name")
    @Expose
    private String unitName;

    @SerializedName("batch_id")
    @Expose
    private Integer batchId;

    @SerializedName("batch_no")
    @Expose
    private String batchNo;


    @SerializedName("mrp")
    @Expose
    private Float mrp;

    @SerializedName("purchase_rate")
    @Expose
    private Float purchaseRate;

    @SerializedName("sale_rate")
    @Expose
    private Float saleRate;

    @SerializedName("free_qty")
    @Expose
    private Float freeQty;


    @SerializedName("mfg_date")
    @Expose
    private String mfgDate;

    @SerializedName("expire_date")
    @Expose
    private String expireDate;

    @SerializedName("opening_qty")
    @Expose
    private Float openingQty;

    @SerializedName("costing")
    @Expose
    private Float costing;


    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Float getMrp() {
        return mrp;
    }

    public void setMrp(Float mrp) {
        this.mrp = mrp;
    }

    public Float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Float getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Float saleRate) {
        this.saleRate = saleRate;
    }

    public Float getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(Float freeQty) {
        this.freeQty = freeQty;
    }

    public String getMfgDate() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate = mfgDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public Float getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(Float openingQty) {
        this.openingQty = openingQty;
    }

    public Float getCosting() {
        return costing;
    }

    public void setCosting(Float costing) {
        this.costing = costing;
    }

    @Override
    public String toString() {
        return "ProAndUnisList{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", batchId=" + batchId +
                ", batchNo='" + batchNo + '\'' +
                ", mrp=" + mrp +
                ", purchaseRate=" + purchaseRate +
                ", saleRate=" + saleRate +
                ", freeQty=" + freeQty +
                ", mfgDate='" + mfgDate + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", openingQty=" + openingQty +
                ", costing=" + costing +
                '}';
    }
}
