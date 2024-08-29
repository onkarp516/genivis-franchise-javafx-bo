package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SelectedProductResDTO {


    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cgst")
    @Expose
    private Float cgst;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("current_stock")
    @Expose
    private Integer currentStock;
    @SerializedName("hsn")
    @Expose
    private Float hsn;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("is_inventory")
    @Expose
    private Boolean isInventory;
    @SerializedName("is_serial")
    @Expose
    private Boolean isSerial;
    @SerializedName("packing")
    @Expose
    private String packing;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("sgst")
    @Expose
    private Float sgst;
    @SerializedName("tax_type")
    @Expose
    private String taxType;
    @SerializedName("unit")
    @Expose
    private String unit;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Float getCgst() {
        return cgst;
    }

    public void setCgst(Float cgst) {
        this.cgst = cgst;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Float getHsn() {
        return hsn;
    }

    public void setHsn(Float hsn) {
        this.hsn = hsn;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public Boolean getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(Boolean isInventory) {
        this.isInventory = isInventory;
    }

    public Boolean getIsSerial() {
        return isSerial;
    }

    public void setIsSerial(Boolean isSerial) {
        this.isSerial = isSerial;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Float getSgst() {
        return sgst;
    }

    public void setSgst(Float sgst) {
        this.sgst = sgst;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Override
    public String toString() {
        return "SelectedProductResDTO{" +
                "barcode='" + barcode + '\'' +
                ", id=" + id +
                ", cgst=" + cgst +
                ", code='" + code + '\'' +
                ", currentStock=" + currentStock +
                ", hsn=" + hsn +
                ", isBatch=" + isBatch +
                ", isInventory=" + isInventory +
                ", isSerial=" + isSerial +
                ", packing='" + packing + '\'' +
                ", productName='" + productName + '\'' +
                ", sgst=" + sgst +
                ", taxType='" + taxType + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
