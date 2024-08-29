package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RowListDTO {

    @SerializedName("selectedProduct")
    @Expose
    private SelectedProductResDTO selectedProduct;
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
    @SerializedName("packing")
    @Expose
    private String packing;
    @SerializedName("unit_name")
    @Expose
    private String unitName;
    @SerializedName("unitId")
    @Expose
    private Integer unitId;
    @SerializedName("unit_conv")
    @Expose
    private Float unitConv;
    @SerializedName("qty")
    @Expose
    private Float qty;
    @SerializedName("returnable_qty")
    @Expose
    private Float returnableQty;
    @SerializedName("rate")
    @Expose
    private Float rate;
    @SerializedName("base_amt")
    @Expose
    private Object baseAmt;
    @SerializedName("dis_amt")
    @Expose
    private Float disAmt;
    @SerializedName("dis_per")
    @Expose
    private Float disPer;
    @SerializedName("dis_per_cal")
    @Expose
    private Float disPerCal;
    @SerializedName("dis_amt_cal")
    @Expose
    private Float disAmtCal;
    @SerializedName("total_amt")
    @Expose
    private Float totalAmt;
    @SerializedName("gst")
    @Expose
    private Float gst;
    @SerializedName("igst")
    @Expose
    private Float igst;
    @SerializedName("cgst")
    @Expose
    private Float cgst;
    @SerializedName("sgst")
    @Expose
    private Float sgst;
    @SerializedName("total_igst")
    @Expose
    private Float totalIgst;
    @SerializedName("total_cgst")
    @Expose
    private Float totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private Float totalSgst;
    @SerializedName("final_amt")
    @Expose
    private Float finalAmt;
    @SerializedName("free_qty")
    @Expose
    private String freeQty;
    @SerializedName("dis_per2")
    @Expose
    private Float disPer2;
    @SerializedName("row_dis_amt")
    @Expose
    private Float rowDisAmt;
    @SerializedName("gross_amt")
    @Expose
    private Float grossAmt;
    @SerializedName("add_chg_amt")
    @Expose
    private Float addChgAmt;
    @SerializedName("grossAmt1")
    @Expose
    private Float grossAmt1;
    @SerializedName("invoice_dis_amt")
    @Expose
    private Float invoiceDisAmt;
    @SerializedName("transaction_status")
    @Expose
    private String transactionStatus;
    @SerializedName("inventoryId")
    @Expose
    private Integer inventoryId;
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
    private Float purchaseRate;
    @SerializedName("is_batch")
    @Expose
    private Boolean isBatch;
    @SerializedName("min_rate_a")
    @Expose
    private Object minRateA;
    @SerializedName("min_rate_b")
    @Expose
    private Object minRateB;
    @SerializedName("min_rate_c")
    @Expose
    private Object minRateC;
    @SerializedName("min_discount")
    @Expose
    private String minDiscount;
    @SerializedName("max_discount")
    @Expose
    private String maxDiscount;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturingDate;
    @SerializedName("margin_per")
    @Expose
    private String marginPer;
    @SerializedName("b_rate")
    @Expose
    private Float bRate;
    @SerializedName("sales_rate")
    @Expose
    private Float salesRate;
    @SerializedName("costing")
    @Expose
    private Float costing;
    @SerializedName("costingWithTax")
    @Expose
    private Float costingWithTax;
    @SerializedName("serialNo")
    @Expose
    private List<Object> serialNo;

    public SelectedProductResDTO getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(SelectedProductResDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

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

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
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

    public Float getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(Float unitConv) {
        this.unitConv = unitConv;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public Float getReturnableQty() {
        return returnableQty;
    }

    public void setReturnableQty(Float returnableQty) {
        this.returnableQty = returnableQty;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Object getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(Object baseAmt) {
        this.baseAmt = baseAmt;
    }

    public Float getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Float disAmt) {
        this.disAmt = disAmt;
    }

    public Float getDisPer() {
        return disPer;
    }

    public void setDisPer(Float disPer) {
        this.disPer = disPer;
    }

    public Float getDisPerCal() {
        return disPerCal;
    }

    public void setDisPerCal(Float disPerCal) {
        this.disPerCal = disPerCal;
    }

    public Float getDisAmtCal() {
        return disAmtCal;
    }

    public void setDisAmtCal(Float disAmtCal) {
        this.disAmtCal = disAmtCal;
    }

    public Float getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Float totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Float getGst() {
        return gst;
    }

    public void setGst(Float gst) {
        this.gst = gst;
    }

    public Float getIgst() {
        return igst;
    }

    public void setIgst(Float igst) {
        this.igst = igst;
    }

    public Float getCgst() {
        return cgst;
    }

    public void setCgst(Float cgst) {
        this.cgst = cgst;
    }

    public Float getSgst() {
        return sgst;
    }

    public void setSgst(Float sgst) {
        this.sgst = sgst;
    }

    public Float getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Float totalIgst) {
        this.totalIgst = totalIgst;
    }

    public Float getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Float totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Float getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Float totalSgst) {
        this.totalSgst = totalSgst;
    }

    public Float getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(Float finalAmt) {
        this.finalAmt = finalAmt;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public Float getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(Float disPer2) {
        this.disPer2 = disPer2;
    }

    public Float getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(Float rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public Float getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Float grossAmt) {
        this.grossAmt = grossAmt;
    }

    public Float getAddChgAmt() {
        return addChgAmt;
    }

    public void setAddChgAmt(Float addChgAmt) {
        this.addChgAmt = addChgAmt;
    }

    public Float getGrossAmt1() {
        return grossAmt1;
    }

    public void setGrossAmt1(Float grossAmt1) {
        this.grossAmt1 = grossAmt1;
    }

    public Float getInvoiceDisAmt() {
        return invoiceDisAmt;
    }

    public void setInvoiceDisAmt(Float invoiceDisAmt) {
        this.invoiceDisAmt = invoiceDisAmt;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
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

    public Float getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Float purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Boolean getIsBatch() {
        return isBatch;
    }

    public void setIsBatch(Boolean isBatch) {
        this.isBatch = isBatch;
    }

    public Object getMinRateA() {
        return minRateA;
    }

    public void setMinRateA(Object minRateA) {
        this.minRateA = minRateA;
    }

    public Object getMinRateB() {
        return minRateB;
    }

    public void setMinRateB(Object minRateB) {
        this.minRateB = minRateB;
    }

    public Object getMinRateC() {
        return minRateC;
    }

    public void setMinRateC(Object minRateC) {
        this.minRateC = minRateC;
    }

    public String getMinDiscount() {
        return minDiscount;
    }

    public void setMinDiscount(String minDiscount) {
        this.minDiscount = minDiscount;
    }

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getMarginPer() {
        return marginPer;
    }

    public void setMarginPer(String marginPer) {
        this.marginPer = marginPer;
    }

    public Float getbRate() {
        return bRate;
    }

    public void setbRate(Float bRate) {
        this.bRate = bRate;
    }

    public Float getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Float salesRate) {
        this.salesRate = salesRate;
    }

    public Float getCosting() {
        return costing;
    }

    public void setCosting(Float costing) {
        this.costing = costing;
    }

    public Float getCostingWithTax() {
        return costingWithTax;
    }

    public void setCostingWithTax(Float costingWithTax) {
        this.costingWithTax = costingWithTax;
    }

    public List<Object> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<Object> serialNo) {
        this.serialNo = serialNo;
    }


    @Override
    public String toString() {
        return "RowListDTO{" +
                "selectedProduct=" + selectedProduct +
                ", detailsId=" + detailsId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", levelAId='" + levelAId + '\'' +
                ", levelBId='" + levelBId + '\'' +
                ", levelCId='" + levelCId + '\'' +
                ", packing='" + packing + '\'' +
                ", unitName='" + unitName + '\'' +
                ", unitId=" + unitId +
                ", unitConv=" + unitConv +
                ", qty=" + qty +
                ", returnableQty=" + returnableQty +
                ", rate=" + rate +
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
                ", freeQty='" + freeQty + '\'' +
                ", disPer2=" + disPer2 +
                ", rowDisAmt=" + rowDisAmt +
                ", grossAmt=" + grossAmt +
                ", addChgAmt=" + addChgAmt +
                ", grossAmt1=" + grossAmt1 +
                ", invoiceDisAmt=" + invoiceDisAmt +
                ", transactionStatus='" + transactionStatus + '\'' +
                ", inventoryId=" + inventoryId +
                ", isExpired=" + isExpired +
                ", bDetailsId=" + bDetailsId +
                ", batchNo='" + batchNo + '\'' +
                ", bExpiry='" + bExpiry + '\'' +
                ", purchaseRate=" + purchaseRate +
                ", isBatch=" + isBatch +
                ", minRateA=" + minRateA +
                ", minRateB=" + minRateB +
                ", minRateC=" + minRateC +
                ", minDiscount='" + minDiscount + '\'' +
                ", maxDiscount='" + maxDiscount + '\'' +
                ", manufacturingDate='" + manufacturingDate + '\'' +
                ", marginPer='" + marginPer + '\'' +
                ", bRate=" + bRate +
                ", salesRate=" + salesRate +
                ", costing=" + costing +
                ", costingWithTax=" + costingWithTax +
                ", serialNo=" + serialNo +
                '}';
    }
}
