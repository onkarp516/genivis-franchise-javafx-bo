package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PurOrdToChallGetByIdInvDataDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("purchase_order")
    @Expose
    private String purchaseOrder;
    @SerializedName("purchase_id")
    @Expose
    private Integer purchaseId;
    @SerializedName("purchase_name")
    @Expose
    private String purchaseName;
    @SerializedName("po_sr_no")
    @Expose
    private Integer poSrNo;
    @SerializedName("pi_transaction_dt")
    @Expose
    private String piTransactionDt;
    @SerializedName("transport_name")
    @Expose
    private String transportName;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("supplier_id")
    @Expose
    private Integer supplierId;
    @SerializedName("supplier_name")
    @Expose
    private String supplierName;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("gstNo")
    @Expose
    private String gstNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceDt() {
        return invoiceDt;
    }

    public void setInvoiceDt(String invoiceDt) {
        this.invoiceDt = invoiceDt;
    }

    public String getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(String purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Integer getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Integer purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public Integer getPoSrNo() {
        return poSrNo;
    }

    public void setPoSrNo(Integer poSrNo) {
        this.poSrNo = poSrNo;
    }

    public String getPiTransactionDt() {
        return piTransactionDt;
    }

    public void setPiTransactionDt(String piTransactionDt) {
        this.piTransactionDt = piTransactionDt;
    }

    public String getTransportName() {
        return transportName;
    }

    public void setTransportName(String transportName) {
        this.transportName = transportName;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }
}
