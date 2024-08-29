package com.opethic.genivis.dto.reqres.sales_tranx;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SalesQuotToOrderInvData {
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
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("gstNo")
    @Expose
    private String gstNo;
    @SerializedName("total_base_amount")
    @Expose
    private Double totalBaseAmount;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("total_cgst")
    @Expose
    private Double totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private Double totalSgst;
    @SerializedName("total_igst")
    @Expose
    private Double totalIgst;
    @SerializedName("total_tax")
    @Expose
    private Double totalTax;
    @SerializedName("taxable_amount")
    @Expose
    private Double taxableAmount;
    @SerializedName("total_dis")
    @Expose
    private Double totalDis;
    @SerializedName("gross_amt")
    @Expose
    private Double grossAmt;

    //new
    @SerializedName("ledgerStateCode")
    @Expose
    private String ledgerStateCode;

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
    public Double getTotalBaseAmount() {
        return totalBaseAmount;
    }

    public void setTotalBaseAmount(Double totalBaseAmount) {
        this.totalBaseAmount = totalBaseAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(Double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public Double getTotalDis() {
        return totalDis;
    }

    public void setTotalDis(Double totalDis) {
        this.totalDis = totalDis;
    }

    public Double getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Double grossAmt) {
        this.grossAmt = grossAmt;
    }

    //new
    public String getLedgerStateCode() {
        return ledgerStateCode;
    }

    public void setLedgerStateCode(String ledgerStateCode) {
        this.ledgerStateCode = ledgerStateCode;
    }

}
