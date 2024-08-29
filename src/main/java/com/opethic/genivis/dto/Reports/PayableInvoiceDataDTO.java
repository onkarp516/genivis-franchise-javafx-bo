package com.opethic.genivis.dto.Reports;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PayableInvoiceDataDTO implements Serializable {
    @SerializedName("ledger_name")
    @Expose
    private String ledgerName;
    @SerializedName("CreditorsId")
    @Expose
    private Integer creditorsId;
    @SerializedName("Invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("invoiceDate")
    @Expose
    private String invoiceDate;
    @SerializedName("credit_days")
    @Expose
    private Integer creditDays;
    @SerializedName("Due_date")
    @Expose
    private String dueDate;
    @SerializedName("overDueDays")
    @Expose
    private Integer overDueDays;
    @SerializedName("balancing_method")
    @Expose
    private String balancingMethod;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("paid_amt")
    @Expose
    private Double paidAmt;
    @SerializedName("remaining_amt")
    @Expose
    private Double remainingAmt;
    @SerializedName("balancing_type")
    @Expose
    private String balancingType;

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Integer getCreditorsId() {
        return creditorsId;
    }

    public void setCreditorsId(Integer creditorsId) {
        this.creditorsId = creditorsId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getOverDueDays() {
        return overDueDays;
    }

    public void setOverDueDays(Integer overDueDays) {
        this.overDueDays = overDueDays;
    }

    public String getBalancingMethod() {
        return balancingMethod;
    }

    public void setBalancingMethod(String  balancingMethod) {
        this.balancingMethod = balancingMethod;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

}
