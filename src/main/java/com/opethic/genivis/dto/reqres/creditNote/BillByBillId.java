package com.opethic.genivis.dto.reqres.creditNote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillByBillId implements Serializable {
    @SerializedName("invoice_id")
    @Expose
    private Long invoiceId;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("invoice_unique_id")
    @Expose
    private String invoiceUniqueId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("ledger_id")
    @Expose
    private Long ledgerId;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("due_days")
    @Expose
    private Integer dueDays;
    @SerializedName("balancing_type")
    @Expose
    private String balancingType;
    @SerializedName("paid_amt")
    @Expose
    private Double paidAmt;
    @SerializedName("bill_details_id")
    @Expose
    private Long billDetailsId = 0L;
    @SerializedName("remaining_amt")
    @Expose
    private Double remainingAmt;
    @SerializedName("tranx_tracking_code")
    @Expose
    private String tranxTrackingCode;

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Long getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Long billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public String getTranxTrackingCode() {
        return tranxTrackingCode;
    }

    public void setTranxTrackingCode(String tranxTrackingCode) {
        this.tranxTrackingCode = tranxTrackingCode;
    }

    @Override
    public String toString() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("invoice_id", invoiceId);
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("invoice_unique_id", invoiceUniqueId);
        jsonObject.addProperty("amount", amount);
        jsonObject.addProperty("total_amt", totalAmt);
        jsonObject.addProperty("invoice_date", invoiceDate);
        jsonObject.addProperty("invoice_no", invoiceNo);
        jsonObject.addProperty("ledger_id", ledgerId);
        jsonObject.addProperty("source", source);
        jsonObject.addProperty("due_days", dueDays);
        jsonObject.addProperty("balancing_type", balancingType);
        jsonObject.addProperty("paid_amt", paidAmt);
        jsonObject.addProperty("bill_details_id", billDetailsId);
        jsonObject.addProperty("remaining_amt", remainingAmt);
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
