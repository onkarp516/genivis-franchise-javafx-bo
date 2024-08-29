package com.opethic.genivis.dto.reqres.tranx.sales.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranxRowRes {
    private boolean isSelected;
    @SerializedName("add_chg_amt")
    @Expose
    private Double addChgAmt;
    @SerializedName("b_detailsId")
    @Expose
    private Integer bDetailsId;
    @SerializedName("b_expiry")
    @Expose
    private String bExpiry;
    @SerializedName("b_rate")
    @Expose
    private Double bRate;
    @SerializedName("base_amt")
    @Expose
    private Double baseAmt;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;
    @SerializedName("cgst")
    @Expose
    private Double cgst;
    @SerializedName("costing")
    @Expose
    private Double costing;
    @SerializedName("costingWithTax")
    @Expose
    private Double costingWithTax;
    @SerializedName("details_id")
    @Expose
    private Integer detailsId;
    @SerializedName("dis_amt")
    @Expose
    private Double disAmt;
    @SerializedName("dis_amt_cal")
    @Expose
    private Double disAmtCal;
    @SerializedName("dis_per")
    @Expose
    private Double disPer;
    @SerializedName("dis_per2")
    @Expose
    private Double disPer2;
    @SerializedName("dis_per_cal")
    @Expose
    private Double disPerCal;
    @SerializedName("final_amt")
    @Expose
    private Double finalAmt;
    @SerializedName("free_qty")
    @Expose
    private String freeQty;
    @SerializedName("grossAmt1")
    @Expose
    private Double grossAmt1;
    @SerializedName("gross_amt")
    @Expose
    private Double grossAmt;
    @SerializedName("gst")
    @Expose
    private Double gst;
    @SerializedName("igst")
    @Expose
    private Double igst;
    @SerializedName("invoice_dis_amt")
    @Expose
    private Double invoice_dis_amt;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;
    @SerializedName("level_a_id")
    @Expose
    private String levelAId;
    @SerializedName("level_b_id")
    @Expose
    private String levelBId;
    @SerializedName("level_c_id")
    @Expose
    private String levelCId;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturingDate;
    @SerializedName("max_discount")
    @Expose
    private Double maxDiscount;
    @SerializedName("min_discount")
    @Expose
    private Double minDiscount;
    @SerializedName("min_rate_a")
    @Expose
    private Double minRateA;
    @SerializedName("min_rate_b")
    @Expose
    private Double minRateB;
    @SerializedName("min_rate_c")
    @Expose
    private Double minRateC;
    @SerializedName("pack_name")
    @Expose
    private String packName;
    @SerializedName("product_id")
    @Expose
    private Integer productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("purchase_rate")
    @Expose
    private Double purchaseRate;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("rate")
    @Expose
    private Double Rate;
    @SerializedName("returnable_qty")
    @Expose
    private Integer returnableQty;
    @SerializedName("row_dis_amt")
    @Expose
    private Double rowDisAmt;
    @SerializedName("sales_rate")
    @Expose
    private Double salesRate;
    @SerializedName("serialNo")
    @Expose
    private List<String> serialNo;
    @SerializedName("sgst")
    @Expose
    private Double sgst;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("total_cgst")
    @Expose
    private Double totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private Double totalSgst;
    @SerializedName("total_igst")
    @Expose
    private Double totalIgst;
    @SerializedName("unitId")
    @Expose
    private Integer unitId;
    @SerializedName("unit_conv")
    @Expose
    private Double unitConv;
    @SerializedName("unit_name")
    @Expose
    private String unitName;

    public Double getAddChgAmt() {
        return addChgAmt;
    }

    public void setAddChgAmt(Double addChgAmt) {
        this.addChgAmt = addChgAmt;
    }

    public Integer getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(Integer bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public String getbExpiry() {
        return bExpiry;
    }

    public void setbExpiry(String bExpiry) {
        this.bExpiry = bExpiry;
    }

    public Double getbRate() {
        return bRate;
    }

    public void setbRate(Double bRate) {
        this.bRate = bRate;
    }

    public Double getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(Double baseAmt) {
        this.baseAmt = baseAmt;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
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

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }

    public Double getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Double disAmt) {
        this.disAmt = disAmt;
    }

    public Double getDisAmtCal() {
        return disAmtCal;
    }

    public void setDisAmtCal(Double disAmtCal) {
        this.disAmtCal = disAmtCal;
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

    public Double getDisPerCal() {
        return disPerCal;
    }

    public void setDisPerCal(Double disPerCal) {
        this.disPerCal = disPerCal;
    }

    public Double getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Double finalAmt) {
        this.finalAmt = finalAmt;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public Double getGrossAmt1() {
        return grossAmt1;
    }

    public void setGrossAmt1(Double grossAmt1) {
        this.grossAmt1 = grossAmt1;
    }

    public Double getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Double grossAmt) {
        this.grossAmt = grossAmt;
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

    public Double getInvoice_dis_amt() {
        return invoice_dis_amt;
    }

    public void setInvoice_dis_amt(Double invoice_dis_amt) {
        this.invoice_dis_amt = invoice_dis_amt;
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

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Double getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(Double minDiscount) {
        this.minDiscount = minDiscount;
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

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
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

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public Integer getReturnableQty() {
        return returnableQty;
    }

    public void setReturnableQty(Integer returnableQty) {
        this.returnableQty = returnableQty;
    }

    public Double getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(Double rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public List<String> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<String> serialNo) {
        this.serialNo = serialNo;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
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

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "TranxRowRes{" +
                "addChgAmt=" + addChgAmt +
                ", bDetailsId=" + bDetailsId +
                ", bExpiry='" + bExpiry + '\'' +
                ", bRate=" + bRate +
                ", baseAmt=" + baseAmt +
                ", batchNo='" + batchNo + '\'' +
                ", cgst=" + cgst +
                ", costing=" + costing +
                ", costingWithTax=" + costingWithTax +
                ", detailsId=" + detailsId +
                ", disAmt=" + disAmt +
                ", disAmtCal=" + disAmtCal +
                ", disPer=" + disPer +
                ", disPer2=" + disPer2 +
                ", disPerCal=" + disPerCal +
                ", finalAmt=" + finalAmt +
                ", freeQty=" + freeQty +
                ", grossAmt1=" + grossAmt1 +
                ", grossAmt=" + grossAmt +
                ", gst=" + gst +
                ", igst=" + igst +
                ", invoice_dis_amt=" + invoice_dis_amt +
                ", isBatch=" + isBatch +
                ", isExpired=" + isExpired +
                ", levelAId=" + levelAId +
                ", levelBId=" + levelBId +
                ", levelCId=" + levelCId +
                ", manufacturingDate='" + manufacturingDate + '\'' +
                ", maxDiscount=" + maxDiscount +
                ", minDiscount=" + minDiscount +
                ", minRateA=" + minRateA +
                ", minRateB=" + minRateB +
                ", minRateC=" + minRateC +
                ", packName='" + packName + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", purchaseRate=" + purchaseRate +
                ", qty=" + qty +
                ", Rate=" + Rate +
                ", returnableQty=" + returnableQty +
                ", rowDisAmt=" + rowDisAmt +
                ", salesRate=" + salesRate +
                ", serialNo=" + serialNo +
                ", sgst=" + sgst +
                ", totalAmt=" + totalAmt +
                ", totalCgst=" + totalCgst +
                ", totalSgst=" + totalSgst +
                ", totalIgst=" + totalIgst +
                ", unitId=" + unitId +
                ", unitConv=" + unitConv +
                ", unitName='" + unitName + '\'' +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
