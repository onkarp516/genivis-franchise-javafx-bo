package com.opethic.genivis.dto.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillByBillRes {
    @SerializedName("invoice_id")
    @Expose
    private Integer invoiceId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_unique_id")
    @Expose
    private String invoiceUniqueId;

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("total_amt")
    @Expose
    private Double total_amt;

    @SerializedName("invoice_date")
    @Expose
    private String invoice_date;

    @SerializedName("invoice_no")
    @Expose
    private String invoice_no;
    @SerializedName("ledger_id")
    @Expose
    private Integer ledger_id;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("due_days")
    @Expose
    private Integer due_days;
    @SerializedName("balancing_type")
    @Expose
    private String balancing_type;
    @SerializedName("paid_amt")
    @Expose
    private Double paid_amt;

    @SerializedName("bill_details_id")
    @Expose
    private Integer bill_details_id;
    @SerializedName("remaining_amt")
    @Expose
    private Double remaining_amt;

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(Double total_amt) {
        this.total_amt = total_amt;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public Integer getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(Integer ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDue_days() {
        return due_days;
    }

    public void setDue_days(Integer due_days) {
        this.due_days = due_days;
    }

    public String getBalancing_type() {
        return balancing_type;
    }

    public void setBalancing_type(String balancing_type) {
        this.balancing_type = balancing_type;
    }

    public Double getPaid_amt() {
        return paid_amt;
    }

    public void setPaid_amt(Double paid_amt) {
        this.paid_amt = paid_amt;
    }

    public Integer getBill_details_id() {
        return bill_details_id;
    }

    public void setBill_details_id(Integer bill_details_id) {
        this.bill_details_id = bill_details_id;
    }

    public Double getRemaining_amt() {
        return remaining_amt;
    }

    public void setRemaining_amt(Double remaining_amt) {
        this.remaining_amt = remaining_amt;
    }
}
