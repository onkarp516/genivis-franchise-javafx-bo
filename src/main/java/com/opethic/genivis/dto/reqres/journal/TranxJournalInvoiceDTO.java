package com.opethic.genivis.dto.reqres.journal;

import javafx.scene.control.CheckBox;

public class TranxJournalInvoiceDTO {
    private CheckBox isSelect;
    private String id;
    private String invoiceNo;
    private String invoiceAmt;
    private String type;
    private String BillDate;
    private String balance;
    private String dueDate;
    private String days;
    private String paidAmt;
    private String balanceAmt;

    public TranxJournalInvoiceDTO() {
    }

    public CheckBox getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(CheckBox isSelect) {
        this.isSelect = isSelect;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceAmt() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(String invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBillDate() {
        return BillDate;
    }

    public void setBillDate(String billDate) {
        BillDate = billDate;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getBalanceAmt() {
        return balanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        this.balanceAmt = balanceAmt;
    }
}
