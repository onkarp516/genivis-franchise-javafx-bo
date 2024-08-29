package com.opethic.genivis.dto.reqres.tranx.sales.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxRowConvRes {
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
    private Double unitConv;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("rate")
    @Expose
    private Double rate;

    @SerializedName("pack_name")
    @Expose
    private String packName;
    @SerializedName("base_amt")
    @Expose
    private Double baseAmt;
    @SerializedName("dis_amt")
    @Expose
    private Double disAmt;
    @SerializedName("dis_per")
    @Expose
    private Double disPer;
    @SerializedName("dis_per_cal")
    @Expose
    private Double disPerCal;
    @SerializedName("dis_amt_cal")
    @Expose
    private Double disAmtCal;
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
    private String free_qty;
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
    private Integer bDetailsId;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;
    @SerializedName("b_expiry")
    @Expose
    private String bExpiry;
    @SerializedName("purchase_rate")
    @Expose
    private Double purchaseRate;
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
    private Double minDiscount;
    @SerializedName("max_discount")
    @Expose
    private Double maxDiscount;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturing_date;
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

    public Double getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(Double unitConv) {
        this.unitConv = unitConv;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
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

    public String getFree_qty() {
        return free_qty;
    }

    public void setFree_qty(String free_qty) {
        this.free_qty = free_qty;
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

    public Integer getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(Integer bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public Boolean getBatch() {
        return isBatch;
    }

    public void setBatch(Boolean batch) {
        isBatch = batch;
    }

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
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

    public Double getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Double minDiscount) {
        this.minDiscount = minDiscount;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getManufacturing_date() {
        return manufacturing_date;
    }

    public void setManufacturing_date(String manufacturing_date) {
        this.manufacturing_date = manufacturing_date;
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

    @Override
    public String toString() {
        return "TranxRowConvRes{" +
                "detailsId=" + detailsId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", levelAId='" + levelAId + '\'' +
                ", levelBId='" + levelBId + '\'' +
                ", levelCId='" + levelCId + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitId=" + unitId +
                ", unitConv=" + unitConv +
                ", qty=" + qty +
                ", rate=" + rate +
                ", packName='" + packName + '\'' +
                ", baseAmt=" + baseAmt +
                ", disAmt=" + disAmt +
                ", disPer=" + disPer +
                ", disPerCal=" + disPerCal +
                ", disAmtCal=" + disAmtCal +
                ", totalAmt=" + totalAmt +
                ", gst=" + gst +
                ", igst=" + igst +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", totalIgst=" + totalIgst +
                ", totalCgst=" + totalCgst +
                ", totalSgst=" + totalSgst +
                ", finalAmt=" + finalAmt +
                ", free_qty='" + free_qty + '\'' +
                ", disPer2='" + disPer2 + '\'' +
                ", rowDisAmt=" + rowDisAmt +
                ", grossAmt=" + grossAmt +
                ", grossAmt1=" + grossAmt1 +
                ", invoiceDisAmt=" + invoiceDisAmt +
                ", referenceId=" + referenceId +
                ", referenceType='" + referenceType + '\'' +
                ", bDetailsId=" + bDetailsId +
                ", isBatch=" + isBatch +
                ", isExpired=" + isExpired +
                ", batchNo='" + batchNo + '\'' +
                ", bExpiry='" + bExpiry + '\'' +
                ", purchaseRate=" + purchaseRate +
                ", minRateA=" + minRateA +
                ", minRateB=" + minRateB +
                ", minRateC=" + minRateC +
                ", minDiscount=" + minDiscount +
                ", maxDiscount=" + maxDiscount +
                ", manufacturing_date='" + manufacturing_date + '\'' +
                ", minMargin=" + minMargin +
                ", bRate=" + bRate +
                ", salesRate=" + salesRate +
                ", costing=" + costing +
                ", costingWithTax=" + costingWithTax +
                '}';
    }
}
