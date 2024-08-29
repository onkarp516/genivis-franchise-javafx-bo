package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InvoiceData {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("tranx_unique_code")
    @Expose
    private String tranxUniqueCode;
    @SerializedName("purchase_sr_no")
    @Expose
    private Long purchaseSrNo;
    @SerializedName("purchase_account_ledger_id")
    @Expose
    private Long purchaseAccountLedgerId;
    @SerializedName("supplierId")
    @Expose
    private Long supplierId;
    @SerializedName("supplierName")
    @Expose
    private String supplierName;
    @SerializedName("transaction_dt")
    @Expose
    private String transactionDt;
    @SerializedName("additional_charges_total")
    @Expose
    private Double additionalChargesTotal;
    @SerializedName("gstNo")
    @Expose
    private String gstNo;
    @SerializedName("isRoundOffCheck")
    @Expose
    private Boolean isRoundOffCheck;
    @SerializedName("roundoff")
    @Expose
    private Double roundoff;
    @SerializedName("source")
    @Expose
    private String source;

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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTranxUniqueCode() {
        return tranxUniqueCode;
    }

    public void setTranxUniqueCode(String tranxUniqueCode) {
        this.tranxUniqueCode = tranxUniqueCode;
    }

    public Long getPurchaseSrNo() {
        return purchaseSrNo;
    }

    public void setPurchaseSrNo(Long purchaseSrNo) {
        this.purchaseSrNo = purchaseSrNo;
    }

    public Long getPurchaseAccountLedgerId() {
        return purchaseAccountLedgerId;
    }

    public void setPurchaseAccountLedgerId(Long purchaseAccountLedgerId) {
        this.purchaseAccountLedgerId = purchaseAccountLedgerId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public Double getAdditionalChargesTotal() {
        return additionalChargesTotal;
    }

    public void setAdditionalChargesTotal(Double additionalChargesTotal) {
        this.additionalChargesTotal = additionalChargesTotal;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Boolean getRoundOffCheck() {
        return isRoundOffCheck;
    }

    public void setRoundOffCheck(Boolean roundOffCheck) {
        isRoundOffCheck = roundOffCheck;
    }

    public Double getRoundoff() {
        return roundoff;
    }

    public void setRoundoff(Double roundoff) {
        this.roundoff = roundoff;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
