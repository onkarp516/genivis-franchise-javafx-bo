package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SalesQuotToChallanInvoiceMainDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("sales_quotation_no")
    @Expose
    private String salesQuotationNo;
    @SerializedName("sales_account_id")
    @Expose
    private Integer salesAccountId;
    @SerializedName("sales_account_name")
    @Expose
    private String salesAccountName;
    @SerializedName("sales_sr_no")
    @Expose
    private Integer salesSrNo;
    @SerializedName("sq_sr_no")
    @Expose
    private Integer sqSrNo;
    @SerializedName("sq_transaction_dt")
    @Expose
    private String sqTransactionDt;
    @SerializedName("reference")
    @Expose
    private Object reference;
    @SerializedName("debtors_id")
    @Expose
    private Integer debtorsId;
    @SerializedName("debtors_name")
    @Expose
    private String debtorsName;
    @SerializedName("ledgerStateCode")
    @Expose
    private String ledgerStateCode;
    @SerializedName("narration")
    @Expose
    private String narration;

    @SerializedName("transactionTrackingNo")
    @Expose
    private String trackingNumber;

    @SerializedName("discountInAmt")
    @Expose
    private Double discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Double discountInPer;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getLedgerStateCode() {
        return ledgerStateCode;
    }

    public void setLedgerStateCode(String ledgerStateCode) {
        this.ledgerStateCode = ledgerStateCode;
    }

    @SerializedName("gstNo")
    @Expose
    private String gstNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceDt() {
        return invoiceDt;
    }

    public void setInvoiceDt(String invoiceDt) {
        this.invoiceDt = invoiceDt;
    }

    public String getSalesQuotationNo() {
        return salesQuotationNo;
    }

    public void setSalesQuotationNo(String salesQuotationNo) {
        this.salesQuotationNo = salesQuotationNo;
    }

    public Integer getSalesAccountId() {
        return salesAccountId;
    }

    public void setSalesAccountId(Integer salesAccountId) {
        this.salesAccountId = salesAccountId;
    }

    public String getSalesAccountName() {
        return salesAccountName;
    }

    public void setSalesAccountName(String salesAccountName) {
        this.salesAccountName = salesAccountName;
    }

    public Integer getSalesSrNo() {
        return salesSrNo;
    }

    public void setSalesSrNo(Integer salesSrNo) {
        this.salesSrNo = salesSrNo;
    }

    public Integer getSqSrNo() {
        return sqSrNo;
    }

    public void setSqSrNo(Integer sqSrNo) {
        this.sqSrNo = sqSrNo;
    }

    public String getSqTransactionDt() {
        return sqTransactionDt;
    }

    public void setSqTransactionDt(String sqTransactionDt) {
        this.sqTransactionDt = sqTransactionDt;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public Integer getDebtorsId() {
        return debtorsId;
    }

    public void setDebtorsId(Integer debtorsId) {
        this.debtorsId = debtorsId;
    }

    public String getDebtorsName() {
        return debtorsName;
    }

    public void setDebtorsName(String debtorsName) {
        this.debtorsName = debtorsName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Double getDiscountInAmt() {
        return discountInAmt;
    }

    public void setDiscountInAmt(Double discountInAmt) {
        this.discountInAmt = discountInAmt;
    }

    public Double getDiscountInPer() {
        return discountInPer;
    }

    public void setDiscountInPer(Double discountInPer) {
        this.discountInPer = discountInPer;
    }
}


