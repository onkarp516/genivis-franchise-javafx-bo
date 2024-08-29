package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxSalesInvoiceRtnListResDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("payment_mode")
    @Expose
    private String paymentMode;
    @SerializedName("transactionTrackingNo")
    @Expose
    private String transactionTrackingNo;
    @SerializedName("referenceNo")
    @Expose
    private String referenceNo;
    @SerializedName("sales_return_no")
    @Expose
    private String invoiceNo;
    @SerializedName("referenceType")
    @Expose
    private String referenceType;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;
    @SerializedName("tranxCode")
    @Expose
    private String tranxCode;
    @SerializedName("transaction_date")
    @Expose
    private String invoiceDate;
    @SerializedName("sale_serial_number")
    @Expose
    private String saleSerialNumber;
    @SerializedName("sundry_debtor_id")
    @Expose
    private Integer sundryDebtorId;
    @SerializedName("sundry_debtor_name")
    @Expose
    private String sundryDebtorName;
    @SerializedName("tax_amt")
    @Expose
    private Double taxAmt;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("totaligst")
    @Expose
    private Double totalIgst;
    @SerializedName("totalcgst")
    @Expose
    private Double totalCgst;
    @SerializedName("totalsgst")
    @Expose
    private Double totalSgst;
    @SerializedName("sale_account_name")
    @Expose
    private String saleAccountName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getTransactionTrackingNo() {
        return transactionTrackingNo;
    }

    public void setTransactionTrackingNo(String transactionTrackingNo) {
        this.transactionTrackingNo = transactionTrackingNo;
    }

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Double getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(Double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public String getTranxCode() {
        return tranxCode;
    }

    public void setTranxCode(String tranxCode) {
        this.tranxCode = tranxCode;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSaleSerialNumber() {
        return saleSerialNumber;
    }

    public void setSaleSerialNumber(String saleSerialNumber) {
        this.saleSerialNumber = saleSerialNumber;
    }

    public Integer getSundryDebtorId() {
        return sundryDebtorId;
    }

    public void setSundryDebtorId(Integer sundryDebtorId) {
        this.sundryDebtorId = sundryDebtorId;
    }

    public String getSundryDebtorName() {
        return sundryDebtorName;
    }

    public void setSundryDebtorName(String sundryDebtorName) {
        this.sundryDebtorName = sundryDebtorName;
    }


    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public String getSaleAccountName() {
        return saleAccountName;
    }

    public void setSaleAccountName(String saleAccountName) {
        this.saleAccountName = saleAccountName;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    @Override
    public String toString() {
        return "TranxSalesInvoiceListResDTO{" +
                "id=" + id +
                ", paymentMode='" + paymentMode + '\'' +
                ", transactionTrackingNo='" + transactionTrackingNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", referenceType='" + referenceType + '\'' +
                ", taxableAmt='" + taxableAmt + '\'' +
                ", tranxCode='" + tranxCode + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", saleSerialNumber='" + saleSerialNumber + '\'' +
                ", sundryDebtorId=" + sundryDebtorId +
                ", sundryDebtorName='" + sundryDebtorName + '\'' +
                ", taxAmt=" + taxAmt +
                ", totalAmount=" + totalAmount +
                ", narration='" + narration + '\'' +
                ", totalIgst=" + totalIgst +
                ", totalCgst=" + totalCgst +
                ", totalSgst=" + totalSgst +
                ", saleAccountName='" + saleAccountName + '\'' +
                '}';
    }
}
