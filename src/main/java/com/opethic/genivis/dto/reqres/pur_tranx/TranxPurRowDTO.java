package com.opethic.genivis.dto.reqres.pur_tranx;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxPurRowDTO {
    @SerializedName("details_id")
    @Expose
    private String details_id;
    @SerializedName("counterNo")
    @Expose
    private String counterNo;
    @SerializedName("inventoryId")
    @Expose
    private String inventoryId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("levelaId")
    @Expose
    private String levelaId;
    @SerializedName("levelbId")
    @Expose
    private String levelbId;
    @SerializedName("levelcId")
    @Expose
    private String levelcId;
    @SerializedName("unitId")
    @Expose
    private String unitId;
    @SerializedName("unit_conv")
    @Expose
    private String unitConv;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("free_qty")
    @Expose
    private String freeQty;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("base_amt")
    @Expose
    private String baseAmt;
    @SerializedName("dis_amt")
    @Expose
    private String disAmt;
    @SerializedName("dis_per")
    @Expose
    private String disPer;
    @SerializedName("dis_per2")
    @Expose
    private String disPer2;
    @SerializedName("dis_per_cal")
    @Expose
    private String disPerCal;
    @SerializedName("dis_amt_cal")
    @Expose
    private String disAmtCal;
    @SerializedName("row_dis_amt")
    @Expose
    private String rowDisAmt;
    @SerializedName("gross_amt")
    @Expose
    private String grossAmt;
    @SerializedName("add_chg_amt")
    @Expose
    private String addChgAmt;
    @SerializedName("gross_amt1")
    @Expose
    private String grossAmt1;
    @SerializedName("invoice_dis_amt")
    @Expose
    private String invoiceDisAmt;
    @SerializedName("total_amt")
    @Expose
    private String totalAmt;
    @SerializedName("gst")
    @Expose
    private String gst;
    @SerializedName("igst")
    @Expose
    private String igst;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;
    @SerializedName("total_igst")
    @Expose
    private String totalIgst;
    @SerializedName("total_cgst")
    @Expose
    private String totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private String totalSgst;
    @SerializedName("final_amt")
    @Expose
    private String finalAmt;

    @SerializedName("b_no")
    @Expose
    private String bNo;
    @SerializedName("b_rate")
    @Expose
    private String bRate;


    @SerializedName("sales_rate")
    @Expose
    private String salesRate;
    @SerializedName("rate_a")
    @Expose
    private String rateA;
    @SerializedName("rate_b")
    @Expose
    private String rateB;
    @SerializedName("rate_c")
    @Expose
    private String rateC;
    @SerializedName("costing")
    @Expose
    private String costing;
    @SerializedName("costing_with_tax")
    @Expose
    private String costingWithTax;
    @SerializedName("min_margin")
    @Expose
    private String minMargin;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturingDate;
    @SerializedName("b_purchase_rate")
    @Expose
    private String bPurchaseRate;
    @SerializedName("b_expiry")
    @Expose
    private String bExpiry;
    @SerializedName("b_details_id")
    @Expose
    private String bDetailsId;
    @SerializedName("is_batch")
    @Expose
    private String  isBatch;
    @SerializedName("serialNo")
    @Expose
    private List<Object> serialNo;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("reference_type")
    @Expose
    private String referenceType;
    @SerializedName("productCode")
    @Expose
    private String productCode;
    @SerializedName("productUnit")
    @Expose
    private String productUnit;

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }
    public String getDetailsId() {
        return details_id;
    }

    public void setDetailsId(String details_id) {
        this.details_id = details_id;
    }

    public String getCounterNo() {
        return counterNo;
    }

    public void setCounterNo(String counterNo) {
        this.counterNo = counterNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLevelaId() {
        return levelaId;
    }

    public void setLevelaId(String levelaId) {
        this.levelaId = levelaId;
    }

    public String getLevelbId() {
        return levelbId;
    }

    public void setLevelbId(String levelbId) {
        this.levelbId = levelbId;
    }

    public String getLevelcId() {
        return levelcId;
    }

    public void setLevelcId(String levelcId) {
        this.levelcId = levelcId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(String unitConv) {
        this.unitConv = unitConv;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(String baseAmt) {
        this.baseAmt = baseAmt;
    }

    public String getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(String disAmt) {
        this.disAmt = disAmt;
    }

    public String getDisPer() {
        return disPer;
    }

    public void setDisPer(String disPer) {
        this.disPer = disPer;
    }

    public String getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(String disPer2) {
        this.disPer2 = disPer2;
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

    public String getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(String rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public String getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(String grossAmt) {
        this.grossAmt = grossAmt;
    }

    public String getAddChgAmt() {
        return addChgAmt;
    }

    public void setAddChgAmt(String addChgAmt) {
        this.addChgAmt = addChgAmt;
    }

    public String getGrossAmt1() {
        return grossAmt1;
    }

    public void setGrossAmt1(String grossAmt1) {
        this.grossAmt1 = grossAmt1;
    }

    public String getInvoiceDisAmt() {
        return invoiceDisAmt;
    }

    public void setInvoiceDisAmt(String invoiceDisAmt) {
        this.invoiceDisAmt = invoiceDisAmt;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(String totalIgst) {
        this.totalIgst = totalIgst;
    }

    public String getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(String totalCgst) {
        this.totalCgst = totalCgst;
    }

    public String getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(String totalSgst) {
        this.totalSgst = totalSgst;
    }

    public String getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(String finalAmt) {
        this.finalAmt = finalAmt;
    }

    public String getbNo() {
        return bNo;
    }

    public void setbNo(String bNo) {
        this.bNo = bNo;
    }

    public String getbRate() {
        return bRate;
    }

    public void setbRate(String bRate) {
        this.bRate = bRate;
    }

    public String getRateA() {
        return rateA;
    }

    public void setRateA(String rateA) {
        this.rateA = rateA;
    }

    public String getRateB() {
        return rateB;
    }

    public void setRateB(String rateB) {
        this.rateB = rateB;
    }

    public String getRateC() {
        return rateC;
    }

    public void setRateC(String rateC) {
        this.rateC = rateC;
    }

    public String getCosting() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing = costing;
    }

    public String getCostingWithTax() {
        return costingWithTax;
    }

    public void setCostingWithTax(String costingWithTax) {
        this.costingWithTax = costingWithTax;
    }

    public String getMinMargin() {
        return minMargin;
    }

    public void setMinMargin(String minMargin) {
        this.minMargin = minMargin;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getbPurchaseRate() {
        return bPurchaseRate;
    }

    public void setbPurchaseRate(String bPurchaseRate) {
        this.bPurchaseRate = bPurchaseRate;
    }

    public String getbExpiry() {
        return bExpiry;
    }

    public void setbExpiry(String bExpiry) {
        this.bExpiry = bExpiry;
    }

    public String getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(String bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public String getBatch() {
        return isBatch;
    }

    public void setBatch(String batch) {
        isBatch = batch;
    }

    public List<Object> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<Object> serialNo) {
        this.serialNo = serialNo;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }
    public String getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(String salesRate) {
        this.salesRate = salesRate;
    }

    public String getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(String isBatch) {
        this.isBatch = isBatch;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }
//
//    public String getDetails_id() {
//        return details_id;
//    }
//    public void setDetails_id(String details_id) {
//        {
//            this.details_id = details_id;
//        }
//    }
}