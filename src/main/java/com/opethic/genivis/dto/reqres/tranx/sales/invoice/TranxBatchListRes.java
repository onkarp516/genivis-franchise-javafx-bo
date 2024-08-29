package com.opethic.genivis.dto.reqres.tranx.sales.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxBatchListRes {
    @SerializedName("is_expired")
    @Expose
    private Boolean isExpired;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("b_details_id")
    @Expose
    private Integer bDetailsId;
    @SerializedName("org_b_details_id")
    @Expose
    private Integer orgBDetailsId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("batch_no")
    @Expose
    private String batchNo;
    @SerializedName("qty")
    @Expose
    private Integer qty;
    @SerializedName("free_qty")
    @Expose
    private Double free_qty;
    @SerializedName("expiry_date")
    @Expose
    private String expiryDate;

    @SerializedName("purchase_rate")
    @Expose
    private Double purchaseRate;
    @SerializedName("sales_rate")
    @Expose
    private Double salesRate;
    @SerializedName("mrp")
    @Expose
    private Double mrp;

    @SerializedName("min_rate_a")
    @Expose
    private Double minRateA;
    @SerializedName("min_rate_b")
    @Expose
    private Double minRateB;
    @SerializedName("min_rate_c")
    @Expose
    private Double minRateC;
    @SerializedName("net_rate")
    @Expose
    private Double netRate;
    @SerializedName("sales_rate_with_tax")
    @Expose
    private Double salesRateWithTax;
    @SerializedName("manufacturing_date")
    @Expose
    private String manufacturingDate;
    @SerializedName("min_margin")
    @Expose
    private Double minMargin;
    @SerializedName("b_rate")
    @Expose
    private Double bRate;
    @SerializedName("costing")
    @Expose
    private Double costing;
    @SerializedName("costingWithTax")
    @Expose
    private Double costingWithTax;
    @SerializedName("closing_stock")
    @Expose
    private Double closingStock;
    @SerializedName("opening_stock")
    @Expose
    private Double openingStock;
    @SerializedName("max_discount")
    @Expose
    private Double maxDiscount;
    @SerializedName("min_discount")
    @Expose
    private Double minDiscount;
    @SerializedName("dis_per")
    @Expose
    private Double disPer;
    @SerializedName("dis_amt")
    @Expose
    private Double disAmt;
    @SerializedName("cess_per")
    @Expose
    private Double cessPer;
    @SerializedName("cess_amt")
    @Expose
    private Double cessAmt;
    @SerializedName("barcode")
    @Expose
    private Double barcode;
    @SerializedName("pur_date")
    @Expose
    private String purDate;
    @SerializedName("tax_per")
    @Expose
    private String taxPer;
    @SerializedName("is_edit")
    @Expose
    private Boolean isEdit;

    public Boolean getExpired() {
        return isExpired;
    }

    public void setExpired(Boolean expired) {
        isExpired = expired;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getbDetailsId() {
        return bDetailsId;
    }

    public void setbDetailsId(Integer bDetailsId) {
        this.bDetailsId = bDetailsId;
    }

    public Integer getOrgBDetailsId() {
        return orgBDetailsId;
    }

    public void setOrgBDetailsId(Integer orgBDetailsId) {
        this.orgBDetailsId = orgBDetailsId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Double getFree_qty() {
        return free_qty;
    }

    public void setFree_qty(Double free_qty) {
        this.free_qty = free_qty;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
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

    public Double getNetRate() {
        return netRate;
    }

    public void setNetRate(Double netRate) {
        this.netRate = netRate;
    }

    public Double getSalesRateWithTax() {
        return salesRateWithTax;
    }

    public void setSalesRateWithTax(Double salesRateWithTax) {
        this.salesRateWithTax = salesRateWithTax;
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

    public Double getClosingStock() {
        return closingStock;
    }

    public void setClosingStock(Double closingStock) {
        this.closingStock = closingStock;
    }

    public Double getOpeningStock() {
        return openingStock;
    }

    public void setOpeningStock(Double openingStock) {
        this.openingStock = openingStock;
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

    public Double getDisPer() {
        return disPer;
    }

    public void setDisPer(Double disPer) {
        this.disPer = disPer;
    }

    public Double getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(Double disAmt) {
        this.disAmt = disAmt;
    }

    public Double getCessPer() {
        return cessPer;
    }

    public void setCessPer(Double cessPer) {
        this.cessPer = cessPer;
    }

    public Double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(Double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public Double getBarcode() {
        return barcode;
    }

    public void setBarcode(Double barcode) {
        this.barcode = barcode;
    }

    public String getPurDate() {
        return purDate;
    }

    public void setPurDate(String purDate) {
        this.purDate = purDate;
    }

    public String getTaxPer() {
        return taxPer;
    }

    public void setTaxPer(String taxPer) {
        this.taxPer = taxPer;
    }

    public Boolean getEdit() {
        return isEdit;
    }

    public void setEdit(Boolean edit) {
        isEdit = edit;
    }

    @Override
    public String toString() {
        return "TranxBatchListRes{" +
                "isExpired=" + isExpired +
                ", id=" + id +
                ", bDetailsId=" + bDetailsId +
                ", orgBDetailsId=" + orgBDetailsId +
                ", productName='" + productName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", qty=" + qty +
                ", free_qty=" + free_qty +
                ", expiryDate='" + expiryDate + '\'' +
                ", purchaseRate=" + purchaseRate +
                ", salesRate=" + salesRate +
                ", mrp=" + mrp +
                ", minRateA=" + minRateA +
                ", minRateB=" + minRateB +
                ", minRateC=" + minRateC +
                ", netRate=" + netRate +
                ", salesRateWithTax=" + salesRateWithTax +
                ", manufacturingDate='" + manufacturingDate + '\'' +
                ", minMargin=" + minMargin +
                ", bRate=" + bRate +
                ", costing=" + costing +
                ", costingWithTax=" + costingWithTax +
                ", closingStock=" + closingStock +
                ", openingStock=" + openingStock +
                ", maxDiscount=" + maxDiscount +
                ", minDiscount=" + minDiscount +
                ", disPer=" + disPer +
                ", disAmt=" + disAmt +
                ", cessPer=" + cessPer +
                ", cessAmt=" + cessAmt +
                ", barcode=" + barcode +
                ", purDate='" + purDate + '\'' +
                ", taxPer='" + taxPer + '\'' +
                ", isEdit=" + isEdit +
                '}';
    }
}

