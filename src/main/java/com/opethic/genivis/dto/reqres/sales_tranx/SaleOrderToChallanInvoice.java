package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SaleOrderToChallanInvoice implements Serializable {
    //class for invoice data sale order to challan conversion
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_dt")
    @Expose
    private String invoiceDt;
    @SerializedName("sales_order_no")
    @Expose
    private String salesOrderNo;
    @SerializedName("sales_account_id")
    @Expose
    private Integer salesAccountId;
    @SerializedName("sales_account_name")
    @Expose
    private String salesAccountName;
    @SerializedName("sales_sr_no")
    @Expose
    private Integer salesSrNo;
    @SerializedName("so_sr_no")
    @Expose
    private Integer soSrNo;
    @SerializedName("so_transaction_dt")
    @Expose
    private String soTransactionDt;
    @SerializedName("reference")
    @Expose
    private Object reference;
    @SerializedName("debtors_id")
    @Expose
    private Integer debtorsId;
    @SerializedName("debtors_name")
    @Expose
    private String debtorsName;
    @SerializedName("narration")
    @Expose
    private Object narration;
    @SerializedName("gstNo")
    @Expose
    private String gstNo;
    @SerializedName("roundoff")
    @Expose
    private Double roundoff;
    @SerializedName("isRoundOffCheck")
    @Expose
    private Boolean isRoundOffCheck;
    @SerializedName("transactionTrackingNo")
    @Expose
    private String trackingNumber;

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }


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

    public String getSalesOrderNo() {
        return salesOrderNo;
    }

    public void setSalesOrderNo(String salesOrderNo) {
        this.salesOrderNo = salesOrderNo;
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

    public Integer getSoSrNo() {
        return soSrNo;
    }

    public void setSoSrNo(Integer soSrNo) {
        this.soSrNo = soSrNo;
    }

    public String getSoTransactionDt() {
        return soTransactionDt;
    }

    public void setSoTransactionDt(String soTransactionDt) {
        this.soTransactionDt = soTransactionDt;
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

    public Object getNarration() {
        return narration;
    }

    public void setNarration(Object narration) {
        this.narration = narration;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public Double getRoundoff() {
        return roundoff;
    }

    public void setRoundoff(Double roundoff) {
        this.roundoff = roundoff;
    }

    public Boolean getRoundOffCheck() {
        return isRoundOffCheck;
    }

    public void setRoundOffCheck(Boolean roundOffCheck) {
        isRoundOffCheck = roundOffCheck;
    }
}
