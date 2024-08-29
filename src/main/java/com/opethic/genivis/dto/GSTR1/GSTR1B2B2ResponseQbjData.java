package com.opethic.genivis.dto.GSTR1;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GSTR1B2B2ResponseQbjData implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("invoice_date")
    @Expose
    private String invoiceDate;
    @SerializedName("sale_serial_number")
    @Expose
    private Integer saleSerialNumber;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("sundry_debtor_name")
    @Expose
    private String sundryDebtorName;
    @SerializedName("sundry_debtor_id")
    @Expose
    private Integer sundryDebtorId;
    @SerializedName("sale_account_name")
    @Expose
    private String saleAccountName;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("tax_amt")
    @Expose
    private Double taxAmt;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("referenceNo")
    @Expose
    private String referenceNo;
    @SerializedName("referenceType")
    @Expose
    private Object referenceType;
    @SerializedName("totalcgst")
    @Expose
    private Double totalcgst;
    @SerializedName("totalsgst")
    @Expose
    private Double totalsgst;
    @SerializedName("totaligst")
    @Expose
    private Double totaligst;
    @SerializedName("tranxCode")
    @Expose
    private String tranxCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public Integer getSaleSerialNumber() {
        return saleSerialNumber;
    }

    public void setSaleSerialNumber(Integer saleSerialNumber) {
        this.saleSerialNumber = saleSerialNumber;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSundryDebtorName() {
        return sundryDebtorName;
    }

    public void setSundryDebtorName(String sundryDebtorName) {
        this.sundryDebtorName = sundryDebtorName;
    }

    public Integer getSundryDebtorId() {
        return sundryDebtorId;
    }

    public void setSundryDebtorId(Integer sundryDebtorId) {
        this.sundryDebtorId = sundryDebtorId;
    }

    public String getSaleAccountName() {
        return saleAccountName;
    }

    public void setSaleAccountName(String saleAccountName) {
        this.saleAccountName = saleAccountName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Object getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(Object referenceType) {
        this.referenceType = referenceType;
    }

    public Double getTotalcgst() {
        return totalcgst;
    }

    public void setTotalcgst(Double totalcgst) {
        this.totalcgst = totalcgst;
    }

    public Double getTotalsgst() {
        return totalsgst;
    }

    public void setTotalsgst(Double totalsgst) {
        this.totalsgst = totalsgst;
    }

    public Double getTotaligst() {
        return totaligst;
    }

    public void setTotaligst(Double totaligst) {
        this.totaligst = totaligst;
    }

    public String getTranxCode() {
        return tranxCode;
    }

    public void setTranxCode(String tranxCode) {
        this.tranxCode = tranxCode;
    }
}
