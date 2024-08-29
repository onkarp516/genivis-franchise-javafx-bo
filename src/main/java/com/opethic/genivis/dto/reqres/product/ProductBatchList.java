package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**** uses in GET BY PRODUCT ID  ****/
public class ProductBatchList {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("b_no")
    @Expose
    private String bNo;
    @SerializedName("batch_id")
    @Expose
    private String batchId;
    @SerializedName("opening_qty")
    @Expose
    private Double openingQty;
    @SerializedName("b_free_qty")
    @Expose
    private String bFreeQty;
    @SerializedName("b_mrp")
    @Expose
    private String bMrp;
    @SerializedName("b_sale_rate")
    @Expose
    private String bSaleRate;
    @SerializedName("b_purchase_rate")
    @Expose
    private String bPurchaseRate;
    @SerializedName("b_costing")
    @Expose
    private Double bCosting;
    @SerializedName("b_expiry")
    @Expose
    private String bExpiry;
    @SerializedName("b_manufacturing_date")
    @Expose
    private String bManufacturingDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getbNo() {
        return bNo;
    }

    public void setbNo(String bNo) {
        this.bNo = bNo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public Double getOpeningQty() {
        return openingQty;
    }

    public void setOpeningQty(Double openingQty) {
        this.openingQty = openingQty;
    }

    public String getbFreeQty() {
        return bFreeQty;
    }

    public void setbFreeQty(String bFreeQty) {
        this.bFreeQty = bFreeQty;
    }

    public String getbMrp() {
        return bMrp;
    }

    public void setbMrp(String bMrp) {
        this.bMrp = bMrp;
    }

    public String getbSaleRate() {
        return bSaleRate;
    }

    public void setbSaleRate(String bSaleRate) {
        this.bSaleRate = bSaleRate;
    }

    public String getbPurchaseRate() {
        return bPurchaseRate;
    }

    public void setbPurchaseRate(String bPurchaseRate) {
        this.bPurchaseRate = bPurchaseRate;
    }

    public Double getbCosting() {
        return bCosting;
    }

    public void setbCosting(Double bCosting) {
        this.bCosting = bCosting;
    }

    public String getbExpiry() {
        return bExpiry;
    }

    public void setbExpiry(String bExpiry) {
        this.bExpiry = bExpiry;
    }

    public String getbManufacturingDate() {
        return bManufacturingDate;
    }

    public void setbManufacturingDate(String bManufacturingDate) {
        this.bManufacturingDate = bManufacturingDate;
    }

    @Override
    public String toString() {
        return "ProductBatchList{" +
                "id=" + id +
                ", bNo='" + bNo + '\'' +
                ", batchId='" + batchId + '\'' +
                ", openingQty=" + openingQty +
                ", bFreeQty='" + bFreeQty + '\'' +
                ", bMrp='" + bMrp + '\'' +
                ", bSaleRate='" + bSaleRate + '\'' +
                ", bPurchaseRate='" + bPurchaseRate + '\'' +
                ", bCosting=" + bCosting +
                ", bExpiry='" + bExpiry + '\'' +
                ", bManufacturingDate='" + bManufacturingDate + '\'' +
                '}';
    }
}
