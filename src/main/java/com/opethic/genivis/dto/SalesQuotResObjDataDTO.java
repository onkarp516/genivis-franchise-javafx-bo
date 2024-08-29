package com.opethic.genivis.dto;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SalesQuotResObjDataDTO implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("bill_date")
    @Expose
    private String billDate;
    @SerializedName("sale_serial_number")
    @Expose
    private Object saleSerialNumber;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("sundry_debtors_name")
    @Expose
    private String sundryDebtorsName;
    @SerializedName("sundry_debtors_id")
    @Expose
    private Integer sundryDebtorsId;
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
    private Object paymentMode;
    @SerializedName("sales_quotation_status")
    @Expose
    private String salesQuotationStatus;
    @SerializedName("total_base_amount")
    @Expose
    private Double totalBaseAmount;
    @SerializedName("referenceNo")
    @Expose
    private Object referenceNo;
    @SerializedName("referenceType")
    @Expose
    private Object referenceType;
    @SerializedName("transactionTrackingNo")
    @Expose
    private Object transactionTrackingNo;
    @SerializedName("tranxCode")
    @Expose
    private String tranxCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Object getSaleSerialNumber() {
        return saleSerialNumber;
    }

    public void setSaleSerialNumber(Object saleSerialNumber) {
        this.saleSerialNumber = saleSerialNumber;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSundryDebtorsName() {
        return sundryDebtorsName;
    }

    public void setSundryDebtorsName(String sundryDebtorsName) {
        this.sundryDebtorsName = sundryDebtorsName;
    }

    public Integer getSundryDebtorsId() {
        return sundryDebtorsId;
    }

    public void setSundryDebtorsId(Integer sundryDebtorsId) {
        this.sundryDebtorsId = sundryDebtorsId;
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

    public Object getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(Object paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getSalesQuotationStatus() {
        return salesQuotationStatus;
    }

    public void setSalesQuotationStatus(String salesQuotationStatus) {
        this.salesQuotationStatus = salesQuotationStatus;
    }

    public Double getTotalBaseAmount() {
        return totalBaseAmount;
    }

    public void setTotalBaseAmount(Double totalBaseAmount) {
        this.totalBaseAmount = totalBaseAmount;
    }

    public Object getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(Object referenceNo) {
        this.referenceNo = referenceNo;
    }

    public Object getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(Object referenceType) {
        this.referenceType = referenceType;
    }

    public Object getTransactionTrackingNo() {
        return transactionTrackingNo;
    }

    public void setTransactionTrackingNo(Object transactionTrackingNo) {
        this.transactionTrackingNo = transactionTrackingNo;
    }

    public String getTranxCode() {
        return tranxCode;
    }

    public void setTranxCode(String tranxCode) {
        this.tranxCode = tranxCode;
    }
}
