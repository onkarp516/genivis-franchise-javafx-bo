package com.opethic.genivis.dto.reqres.debitNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DebitNoteByIdBillsDTO implements Serializable {

    @SerializedName("bill_details_id")
    @Expose
    private Long billDetailsId;
    @SerializedName("paid_amt")
    @Expose
    private Double paidAmt;
    @SerializedName("remaining_amt")
    @Expose
    private Double remainingAmt;
    @SerializedName("invoice_id")
    @Expose
    private Long invoiceId;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("balancing_type")
    @Expose
    private String balancingType;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("invoice_unique_id")
    @Expose
    private String invoiceUniqueId;
    @SerializedName("amount")
    @Expose
    private Double amount;

    public Long getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Long billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInvoiceUniqueId() {
        return invoiceUniqueId;
    }

    public void setInvoiceUniqueId(String invoiceUniqueId) {
        this.invoiceUniqueId = invoiceUniqueId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
