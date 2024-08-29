package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {


    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("outlet")
    @Expose
    private String outlet;
    @SerializedName("counterSaleSrNo")
    @Expose
    private Integer counterSaleSrNo;
    @SerializedName("counterSaleNo")
    @Expose
    private String counterSaleNo;
    @SerializedName("billDate")
    @Expose
    private String billDate;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("totalSalesDiscountAmt")
    @Expose
    private Double totalSalesDiscountAmt;
    @SerializedName("totalQty")
    @Expose
    private Double totalQty;
    @SerializedName("totalFreeQty")
    @Expose
    private Double totalFreeQty;
    @SerializedName("totalBill")
    @Expose
    private Double totalBill;
    @SerializedName("totalBaseAmt")
    @Expose
    private Double totalBaseAmt;
    @SerializedName("totalTaxableAmt")
    @Expose
    private Double totalTaxableAmt;
    @SerializedName("roundoff")
    @Expose
    private Object roundoff;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("cashAmt")
    @Expose
    private Double cashAmt;
    @SerializedName("p_totalAmount")
    @Expose
    private Double pTotalAmount;
    @SerializedName("row")
    @Expose
    private List<Row> row;
    @SerializedName("payment_type")
    @Expose
    private List<CSPaymentTypeResDTO> paymentType;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOutlet() {
        return outlet;
    }

    public void setOutlet(String outlet) {
        this.outlet = outlet;
    }

    public Integer getCounterSaleSrNo() {
        return counterSaleSrNo;
    }

    public void setCounterSaleSrNo(Integer counterSaleSrNo) {
        this.counterSaleSrNo = counterSaleSrNo;
    }

    public String getCounterSaleNo() {
        return counterSaleNo;
    }

    public void setCounterSaleNo(String counterSaleNo) {
        this.counterSaleNo = counterSaleNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Double getTotalSalesDiscountAmt() {
        return totalSalesDiscountAmt;
    }

    public void setTotalSalesDiscountAmt(Double totalSalesDiscountAmt) {
        this.totalSalesDiscountAmt = totalSalesDiscountAmt;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Double getTotalFreeQty() {
        return totalFreeQty;
    }

    public void setTotalFreeQty(Double totalFreeQty) {
        this.totalFreeQty = totalFreeQty;
    }

    public Double getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(Double totalBill) {
        this.totalBill = totalBill;
    }

    public Double getTotalBaseAmt() {
        return totalBaseAmt;
    }

    public void setTotalBaseAmt(Double totalBaseAmt) {
        this.totalBaseAmt = totalBaseAmt;
    }

    public Double getTotalTaxableAmt() {
        return totalTaxableAmt;
    }

    public void setTotalTaxableAmt(Double totalTaxableAmt) {
        this.totalTaxableAmt = totalTaxableAmt;
    }

    public Object getRoundoff() {
        return roundoff;
    }

    public void setRoundoff(Object roundoff) {
        this.roundoff = roundoff;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(Double cashAmt) {
        this.cashAmt = cashAmt;
    }

    public Double getpTotalAmount() {
        return pTotalAmount;
    }

    public void setpTotalAmount(Double pTotalAmount) {
        this.pTotalAmount = pTotalAmount;
    }

    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }

    public List<CSPaymentTypeResDTO> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<CSPaymentTypeResDTO> paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

}
