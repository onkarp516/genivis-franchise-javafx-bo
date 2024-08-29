package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesInvoiceDataResDTO1 {
    //DTO for sale order to invoice conversion
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("invoice_no")
    @Expose
    private String invoiceNo;
    @SerializedName("tranx_unique_code")
    @Expose
    private String tranxUniqueCode;
    @SerializedName("sales_sr_no")
    @Expose
    private Integer purchaseSrNo;
    @SerializedName("sales_account_ledger_id")
    @Expose
    private Integer purchaseAccountLedgerId;
    @SerializedName("supplierId")
    @Expose
    private Integer supplierId;
    @SerializedName("debtors_id")
    @Expose
    private Integer debtorsId;       // for ledger id which is not going to change in conversion
    @SerializedName("debtors_name")
    @Expose
    private String debtorsName;     //for ledger name
    @SerializedName("so_transaction_dt")
    @Expose
    private String transactionDt;
    @SerializedName("additional_charges_total")
    @Expose
    private Float additionalChargesTotal;
    @SerializedName("gstNo")
    @Expose
    private String gstNo;
    @SerializedName("isRoundOffCheck")
    @Expose
    private Boolean isRoundOffCheck;
    @SerializedName("roundoff")
    @Expose
    private Float roundoff;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("sales_account_id")
    @Expose
    private String salesAccountId;

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

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTranxUniqueCode() {
        return tranxUniqueCode;
    }

    public void setTranxUniqueCode(String tranxUniqueCode) {
        this.tranxUniqueCode = tranxUniqueCode;
    }

    public Integer getPurchaseSrNo() {
        return purchaseSrNo;
    }

    public void setPurchaseSrNo(Integer purchaseSrNo) {
        this.purchaseSrNo = purchaseSrNo;
    }

    public Integer getPurchaseAccountLedgerId() {
        return purchaseAccountLedgerId;
    }

    public void setPurchaseAccountLedgerId(Integer purchaseAccountLedgerId) {
        this.purchaseAccountLedgerId = purchaseAccountLedgerId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public Float getAdditionalChargesTotal() {
        return additionalChargesTotal;
    }

    public void setAdditionalChargesTotal(Float additionalChargesTotal) {
        this.additionalChargesTotal = additionalChargesTotal;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Boolean getIsRoundOffCheck() {
        return isRoundOffCheck;
    }

    public void setIsRoundOffCheck(Boolean isRoundOffCheck) {
        this.isRoundOffCheck = isRoundOffCheck;
    }

    public Float getRoundoff() {
        return roundoff;
    }

    public void setRoundoff(Float roundoff) {
        this.roundoff = roundoff;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public String toString() {
        return "InvoiceDataResDTO{" +
                "id=" + id +
                ", invoiceDt='" + invoiceDt + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", tranxUniqueCode='" + tranxUniqueCode + '\'' +
                ", purchaseSrNo=" + purchaseSrNo +
                ", purchaseAccountLedgerId=" + purchaseAccountLedgerId +
                ", supplierId=" + supplierId +
                ", transactionDt='" + transactionDt + '\'' +
                ", additionalChargesTotal=" + additionalChargesTotal +
                ", gstNo='" + gstNo + '\'' +
                ", isRoundOffCheck=" + isRoundOffCheck +
                ", roundoff=" + roundoff +
                ", mode='" + mode + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getSalesAccountId() {
        return salesAccountId;
    }

    public void setSalesAccountId(String salesAccountId) {
        this.salesAccountId = salesAccountId;
    }
}

