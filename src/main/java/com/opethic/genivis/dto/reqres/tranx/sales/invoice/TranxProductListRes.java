package com.opethic.genivis.dto.reqres.tranx.sales.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxProductListRes {
    @SerializedName("batch_expiry")
    @Expose
    private String batchExpiry;
    @SerializedName("mrp")
    @Expose
    private Double mrp;
    @SerializedName("sales_rate")
    @Expose
    private Double salesRate;
    @SerializedName("unit_id")
    @Expose
    private Integer unit_id;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("is_negative")
    @Expose
    private Boolean isNegative;
    @SerializedName("hsn")
    @Expose
    private String hsn;
    @SerializedName("tax_type")
    @Expose
    private String taxType;
    @SerializedName("tax_per")
    @Expose
    private Double taxPer;
    @SerializedName("igst")
    @Expose
    private Double igst;
    @SerializedName("cgst")
    @Expose
    private Double cgst;
    @SerializedName("sgst")
    @Expose
    private Double sgst;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("packing")
    @Expose
    private String packing;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("is_inventory")
    @Expose
    private Boolean isInventory;
    @SerializedName("is_serial")
    @Expose
    private Boolean isSerial;
    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("productType")
    @Expose
    private String productType;
    @SerializedName("purchaserate")
    @Expose
    private Double purchaseRate;
    @SerializedName("current_stock")
    @Expose
    private Double currentStock;

    public String getBatchExpiry() {
        return batchExpiry;
    }

    public void setBatchExpiry(String batchExpiry) {
        this.batchExpiry = batchExpiry;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Boolean getNegative() {
        return isNegative;
    }

    public void setNegative(Boolean negative) {
        isNegative = negative;
    }

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Double getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(Double taxPer) {
        this.taxPer = taxPer;
    }

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Boolean getBatch() {
        return isBatch;
    }

    public void setBatch(Boolean batch) {
        isBatch = batch;
    }

    public Boolean getInventory() {
        return isInventory;
    }

    public void setInventory(Boolean inventory) {
        isInventory = inventory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getSerial() {
        return isSerial;
    }

    public void setSerial(Boolean serial) {
        isSerial = serial;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }
}
