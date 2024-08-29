package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SaleOrderToChallanRow implements Serializable {
    //class for get the row data of Sale Order to challan conversion
    @SerializedName("details_id")
    @Expose
    private Integer detailsId;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("level_a_id")
    @Expose
    private String levelAId;
    @SerializedName("level_b_id")
    @Expose
    private String levelBId;
    @SerializedName("level_c_id")
    @Expose
    private String levelCId;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("unitId")
    @Expose
    private Integer unitId;
    @SerializedName("unit_conv")
    @Expose
    private Integer unitConv;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("rate")
    @Expose
    private Integer rate;
    @SerializedName("pack_name")
    @Expose
    private String packName;
    @SerializedName("base_amt")
    @Expose
    private Integer baseAmt;
    @SerializedName("dis_amt")
    @Expose
    private Integer disAmt;
    @SerializedName("dis_per")
    @Expose
    private String disPer;
    @SerializedName("dis_per_cal")
    @Expose
    private String disPerCal;
    @SerializedName("dis_amt_cal")
    @Expose
    private String disAmtCal;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("gst")
    @Expose
    private Double gst;
    @SerializedName("igst")
    @Expose
    private Double igst;
    @SerializedName("cgst")
    @Expose
    private Double cgst;
    @SerializedName("sgst")
    @Expose
    private Double sgst;
    @SerializedName("total_igst")
    @Expose
    private Double totalIgst;
    @SerializedName("total_cgst")
    @Expose
    private Double totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private Double totalSgst;
    @SerializedName("final_amt")
    @Expose
    private Double finalAmt;
    @SerializedName("free_qty")
    @Expose
    private Integer freeQty;
    @SerializedName("dis_per2")
    @Expose
    private String disPer2;
    @SerializedName("row_dis_amt")
    @Expose
    private Double rowDisAmt;
    @SerializedName("gross_amt")
    @Expose
    private Double grossAmt;
    @SerializedName("grossAmt1")
    @Expose
    private Double grossAmt1;
    @SerializedName("invoice_dis_amt")
    @Expose
    private Double invoiceDisAmt;
    @SerializedName("reference_id")
    @Expose
    private Integer referenceId;
    @SerializedName("reference_type")
    @Expose
    private String referenceType;
    @SerializedName("b_detailsId")
    @Expose
    private String bDetailsId;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

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

    public String getLevelAId() {
        return levelAId;
    }

    public void setLevelAId(String levelAId) {
        this.levelAId = levelAId;
    }

    public String getLevelBId() {
        return levelBId;
    }

    public void setLevelBId(String levelBId) {
        this.levelBId = levelBId;
    }

    public String getLevelCId() {
        return levelCId;
    }

    public void setLevelCId(String levelCId) {
        this.levelCId = levelCId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public Integer getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(Integer unitConv) {
        this.unitConv = unitConv;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Integer getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(Integer baseAmt) {
        this.baseAmt = baseAmt;
    }

    public Integer getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Integer disAmt) {
        this.disAmt = disAmt;
    }

    public String getDisPer() {
        return disPer;
    }

    public void setDisPer(String disPer) {
        this.disPer = disPer;
    }

    public String getDisPerCal() {
        return disPerCal;
    }

    public void setDisPerCal(String disPerCal) {
        this.disPerCal = disPerCal;
    }

    public String getDisAmtCal() {
        return disAmtCal;
    }

    public void setDisAmtCal(String disAmtCal) {
        this.disAmtCal = disAmtCal;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
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

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public Double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public Double getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Double finalAmt) {
        this.finalAmt = finalAmt;
    }

    public Integer getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(Integer freeQty) {
        this.freeQty = freeQty;
    }

    public String getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(String disPer2) {
        this.disPer2 = disPer2;
    }

    public Double getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(Double rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public Double getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Double grossAmt) {
        this.grossAmt = grossAmt;
    }

    public Double getGrossAmt1() {
        return grossAmt1;
    }

    public void setGrossAmt1(Double grossAmt1) {
        this.grossAmt1 = grossAmt1;
    }

    public Double getInvoiceDisAmt() {
        return invoiceDisAmt;
    }

    public void setInvoiceDisAmt(Double invoiceDisAmt) {
        this.invoiceDisAmt = invoiceDisAmt;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(String bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

}
