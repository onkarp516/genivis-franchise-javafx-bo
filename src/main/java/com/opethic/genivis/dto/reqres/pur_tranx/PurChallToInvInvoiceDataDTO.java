package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurChallToInvInvoiceDataDTO {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("purchase_order")
    @Expose
    private String purchaseOrder;
    @SerializedName("purchase_id")
    @Expose
    private Long purchaseId;
    @SerializedName("purchase_name")
    @Expose
    private String purchaseName;
    @SerializedName("po_sr_no")
    @Expose
    private Long poSrNo;
    @SerializedName("po_date")
    @Expose
    private String poDate;
    @SerializedName("transport_name")
    @Expose
    private String transportName;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("supplier_id")
    @Expose
    private Long supplierId;
    @SerializedName("supplier_name")
    @Expose
    private String supplierName;
    @SerializedName("gstNo")
    @Expose
    private String  gstNo;
    @SerializedName("narration")
    @Expose
    private String narration;

    public PurChallToInvInvoiceDataDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public Long getPoSrNo() {
        return poSrNo;
    }

    public void setPoSrNo(Long poSrNo) {
        this.poSrNo = poSrNo;
    }

    public String getPoDate() {
        return poDate;
    }

    public void setPoDate(String poDate) {
        this.poDate = poDate;
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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }
}
