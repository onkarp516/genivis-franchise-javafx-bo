package com.opethic.genivis.dto.reqres.sales_tranx;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerRow {

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
    @SerializedName("pack_name")
    @Expose
    private String packName;
    @SerializedName("unit_conv")
    @Expose
    private Double unitConv;
    @SerializedName("qty")
    @Expose
    private Double qty;
    @SerializedName("rate")
    @Expose
    private Double rate;
    @SerializedName("base_amt")
    @Expose
    private Double baseAmt;
    @SerializedName("dis_amt")
    @Expose
    private Double disAmt;
    @SerializedName("dis_per")
    @Expose
    private Double disPer;
    @SerializedName("dis_per2")
    @Expose
    private Double disPer2;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("final_amt")
    @Expose
    private Double finalAmt;
    @SerializedName("free_qty")
    @Expose
    private Double freeQty;
    @SerializedName("row_dis_amt")
    @Expose
    private Double rowDisAmt;
    @SerializedName("igst")
    @Expose
    private Double igst;
    @SerializedName("sgst")
    @Expose
    private Double sgst;
    @SerializedName("cgst")
    @Expose
    private Double cgst;
    @SerializedName("total_igst")
    @Expose
    private Double totalIgst;
    @SerializedName("total_sgst")
    @Expose
    private Double totalSgst;
    @SerializedName("total_cgst")
    @Expose
    private Double totalCgst;
    @SerializedName("dis_per_cal")
    @Expose
    private Double disPerCal;
    @SerializedName("dis_amt_cal")
    @Expose
    private Double disAmtCal;
    @SerializedName("gst")
    @Expose
    private Double gst;
    @SerializedName("gross_amt")
    @Expose
    private Double grossAmt;
    @SerializedName("add_chg_amt")
    @Expose
    private Double addChgAmt;
    @SerializedName("grossAmt1")
    @Expose
    private Double grossAmt1;
    @SerializedName("invoice_dis_amt")
    @Expose
    private Double invoiceDisAmt;
    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;
    @SerializedName("b_detailsId")
    @Expose
    private Integer bDetailsId;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;
    @SerializedName("b_expiry")
    @Expose
    private String bExpiry;
    @SerializedName("purchase_rate")
    @Expose
    private Double purchaseRate;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("min_rate_a")
    @Expose
    private Double minRateA;
    @SerializedName("min_rate_b")
    @Expose
    private Double minRateB;
    @SerializedName("min_rate_c")
    @Expose
    private Double minRateC;
    @SerializedName("min_discount")
    @Expose
    private Object minDiscount;
    @SerializedName("max_discount")
    @Expose
    private Object maxDiscount;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturingDate;
    @SerializedName("min_margin")
    @Expose
    private Double minMargin;
    @SerializedName("b_rate")
    @Expose
    private Double bRate;
    @SerializedName("sales_rate")
    @Expose
    private Double salesRate;
    @SerializedName("costing")
    @Expose
    private Double costing;
    @SerializedName("costingWithTax")
    @Expose
    private Double costingWithTax;

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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Double getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(Double unitConv) {
        this.unitConv = unitConv;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(Double baseAmt) {
        this.baseAmt = baseAmt;
    }

    public Double getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Double disAmt) {
        this.disAmt = disAmt;
    }

    public Double getDisPer() {
        return disPer;
    }

    public void setDisPer(Double disPer) {
        this.disPer = disPer;
    }

    public Double getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(Double disPer2) {
        this.disPer2 = disPer2;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Double finalAmt) {
        this.finalAmt = finalAmt;
    }

    public Double getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(Double freeQty) {
        this.freeQty = freeQty;
    }

    public Double getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(Double rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
    }

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public Double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public Double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Double getDisPerCal() {
        return disPerCal;
    }

    public void setDisPerCal(Double disPerCal) {
        this.disPerCal = disPerCal;
    }

    public Double getDisAmtCal() {
        return disAmtCal;
    }

    public void setDisAmtCal(Double disAmtCal) {
        this.disAmtCal = disAmtCal;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
    }

    public Double getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Double grossAmt) {
        this.grossAmt = grossAmt;
    }

    public Double getAddChgAmt() {
        return addChgAmt;
    }

    public void setAddChgAmt(Double addChgAmt) {
        this.addChgAmt = addChgAmt;
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

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }

    public Integer getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(Integer bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getbExpiry() {
        return bExpiry;
    }

    public void setbExpiry(String bExpiry) {
        this.bExpiry = bExpiry;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public Double getMinRateA() {
        return minRateA;
    }

    public void setMinRateA(Double minRateA) {
        this.minRateA = minRateA;
    }

    public Double getMinRateB() {
        return minRateB;
    }

    public void setMinRateB(Double minRateB) {
        this.minRateB = minRateB;
    }

    public Double getMinRateC() {
        return minRateC;
    }

    public void setMinRateC(Double minRateC) {
        this.minRateC = minRateC;
    }

    public Object getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Object minDiscount) {
        this.minDiscount = minDiscount;
    }

    public Object getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Object maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Double getMinMargin() {
        return minMargin;
    }

    public void setMinMargin(Double minMargin) {
        this.minMargin = minMargin;
    }

    public Double getbRate() {
        return bRate;
    }

    public void setbRate(Double bRate) {
        this.bRate = bRate;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Double getCosting() {
        return costing;
    }

    public void setCosting(Double costing) {
        this.costing = costing;
    }

    public Double getCostingWithTax() {
        return costingWithTax;
    }

    public void setCostingWithTax(Double costingWithTax) {
        this.costingWithTax = costingWithTax;
    }

}