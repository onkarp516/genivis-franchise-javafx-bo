package com.opethic.genivis.models.tranx.sales;

public class TranxSalesInvoiceListMDL {
    private Integer id;
    private String paymentMode;
    private String transactionTrackingNo;
    private String referenceNo;
    private String referenceType;
    private String invoiceNo;
    private Double taxableAmt;
    private String tranxCode;
    private String invoiceDate;
    private String saleSerialNumber;
    private Integer sundryDebtorId;
    private String sundryDebtorName;
    private Double taxAmt;
    private String narration;
    private Double totalIgst;
    private Double totalCgst;
    private Double totalSgst;
    private String saleAccountName;
    private Double totalAmount;


    public TranxSalesInvoiceListMDL(Integer id, String paymentMode, String transactionTrackingNo, String referenceNo, String referenceType, String invoiceNo, Double taxableAmt, String tranxCode, String invoiceDate, String saleSerialNumber, Integer sundryDebtorId, String sundryDebtorName, Double taxAmt, String narration, Double totalIgst, Double totalCgst, Double totalSgst, String saleAccountName, Double totalAmount) {
        this.id = id;
        this.paymentMode = paymentMode;
        this.transactionTrackingNo = transactionTrackingNo;
        this.referenceNo = referenceNo;
        this.referenceType = referenceType;
        this.invoiceNo = invoiceNo;
        this.taxableAmt = taxableAmt;
        this.tranxCode = tranxCode;
        this.invoiceDate = invoiceDate;
        this.saleSerialNumber = saleSerialNumber;
        this.sundryDebtorId = sundryDebtorId;
        this.sundryDebtorName = sundryDebtorName;
        this.taxAmt = taxAmt;
        this.narration = narration;
        this.totalIgst = totalIgst;
        this.totalCgst = totalCgst;
        this.totalSgst = totalSgst;
        this.saleAccountName = saleAccountName;
        this.totalAmount = totalAmount;
    }

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

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
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

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "TranxSalesInvoiceListMDL{" +
                "id=" + id +
                ", paymentMode='" + paymentMode + '\'' +
                ", transactionTrackingNo='" + transactionTrackingNo + '\'' +
                ", referenceNo='" + referenceNo + '\'' +
                ", referenceType='" + referenceType + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", taxableAmt=" + taxableAmt +
                ", tranxCode='" + tranxCode + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", saleSerialNumber='" + saleSerialNumber + '\'' +
                ", sundryDebtorId=" + sundryDebtorId +
                ", sundryDebtorName='" + sundryDebtorName + '\'' +
                ", taxAmt=" + taxAmt +
                ", narration='" + narration + '\'' +
                ", totalIgst=" + totalIgst +
                ", totalCgst=" + totalCgst +
                ", totalSgst=" + totalSgst +
                ", saleAccountName='" + saleAccountName + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
