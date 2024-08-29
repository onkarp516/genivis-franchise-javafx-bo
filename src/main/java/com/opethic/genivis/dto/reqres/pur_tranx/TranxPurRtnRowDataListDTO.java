package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TranxPurRtnRowDataListDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("pur_return_no")
    @Expose
    private String purReturnNo;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("purchase_return_date")
    @Expose
    private String purchaseReturnDate;
    @SerializedName("purchase_return_serial_number")
    @Expose
    private Long purchaseReturnSerialNumber;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("sundry_creditor_name")
    @Expose
    private String sundryCreditorName;
    @SerializedName("sundry_creditor_id")
    @Expose
    private Long sundryCreditorId;
    @SerializedName("tax_amt")
    @Expose
    private Double taxAmt;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;
    @SerializedName("purchase_account_name")
    @Expose
    private String purchaseAccountName;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("transactionTrackingNo")
    @Expose
    private String transactionTrackingNo;
    @SerializedName("tranxCode")
    @Expose
    private String tranxCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPurReturnNo() {
        return purReturnNo;
    }

    public void setPurReturnNo(String purReturnNo) {
        this.purReturnNo = purReturnNo;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPurchaseReturnDate() {
        return purchaseReturnDate;
    }

    public void setPurchaseReturnDate(String purchaseReturnDate) {
        this.purchaseReturnDate = purchaseReturnDate;
    }

    public Long getPurchaseReturnSerialNumber() {
        return purchaseReturnSerialNumber;
    }

    public void setPurchaseReturnSerialNumber(Long purchaseReturnSerialNumber) {
        this.purchaseReturnSerialNumber = purchaseReturnSerialNumber;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSundryCreditorName() {
        return sundryCreditorName;
    }

    public void setSundryCreditorName(String sundryCreditorName) {
        this.sundryCreditorName = sundryCreditorName;
    }

    public Long getSundryCreditorId() {
        return sundryCreditorId;
    }

    public void setSundryCreditorId(Long sundryCreditorId) {
        this.sundryCreditorId = sundryCreditorId;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public Double getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(Double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getPurchaseAccountName() {
        return purchaseAccountName;
    }

    public void setPurchaseAccountName(String purchaseAccountName) {
        this.purchaseAccountName = purchaseAccountName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTransactionTrackingNo() {
        return transactionTrackingNo;
    }

    public void setTransactionTrackingNo(String transactionTrackingNo) {
        this.transactionTrackingNo = transactionTrackingNo;
    }

    public String getTranxCode() {
        return tranxCode;
    }

    public void setTranxCode(String tranxCode) {
        this.tranxCode = tranxCode;
    }
}
