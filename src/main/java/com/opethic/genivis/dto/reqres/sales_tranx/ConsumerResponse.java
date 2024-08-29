package com.opethic.genivis.dto.reqres.sales_tranx;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ConsumerResponse
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("consumerSaleSrNo")
    @Expose
    private Integer consumerSaleSrNo;
    @SerializedName("consumerSaleNo")
    @Expose
    private String consumerSaleNo;
    @SerializedName("billDate")
    @Expose
    private String billDate;
    @SerializedName("clientName")
    @Expose
    private String clientName;
    @SerializedName("clientAddress")
    @Expose
    private String clientAddress;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("doctorId")
    @Expose
    private String doctorId;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("doctorAddress")
    @Expose
    private String doctorAddress;
    @SerializedName("debtorId")
    @Expose
    private String debtorId;
    @SerializedName("totalSalesDiscountAmt")
    @Expose
    private Double totalSalesDiscountAmt;
    @SerializedName("sales_discount")
    @Expose
    private Double salesDiscount;
    @SerializedName("totalQty")
    @Expose
    private Integer totalQty;
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
    private List<ConsumerRow> row;
    @SerializedName("prescRow")
    @Expose
    private List<Object> prescRow;
    @SerializedName("payment_type")
    @Expose
    private List<CSPaymentTypeResDTO> paymentType;

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

    public Integer getConsumerSaleSrNo() {
        return consumerSaleSrNo;
    }

    public void setConsumerSaleSrNo(Integer consumerSaleSrNo) {
        this.consumerSaleSrNo = consumerSaleSrNo;
    }

    public String getConsumerSaleNo() {
        return consumerSaleNo;
    }

    public void setConsumerSaleNo(String consumerSaleNo) {
        this.consumerSaleNo = consumerSaleNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientAddress() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress = clientAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public Double getTotalSalesDiscountAmt() {
        return totalSalesDiscountAmt;
    }

    public void setTotalSalesDiscountAmt(Double totalSalesDiscountAmt) {
        this.totalSalesDiscountAmt = totalSalesDiscountAmt;
    }

    public Double getSalesDiscount() {
        return salesDiscount;
    }

    public void setSalesDiscount(Double salesDiscount) {
        this.salesDiscount = salesDiscount;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
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

    public List<ConsumerRow> getRow() {
        return row;
    }

    public void setRow(List<ConsumerRow> row) {
        this.row = row;
    }

    public List<Object> getPrescRow() {
        return prescRow;
    }

    public void setPrescRow(List<Object> prescRow) {
        this.prescRow = prescRow;
    }

    public List<CSPaymentTypeResDTO> getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(List<CSPaymentTypeResDTO> paymentType) {
        this.paymentType = paymentType;
    }

}
