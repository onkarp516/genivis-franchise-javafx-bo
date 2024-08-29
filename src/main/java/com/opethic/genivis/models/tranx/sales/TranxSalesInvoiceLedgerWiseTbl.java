package com.opethic.genivis.models.tranx.sales;

public class TranxSalesInvoiceLedgerWiseTbl {
    private int id;
    private String billNo;
    private String billDate;
    private Double billAmount;
    private String source;

    public TranxSalesInvoiceLedgerWiseTbl(int id, String billNo, String billDate, Double billAmount, String source) {
        this.id = id;
        this.billNo = billNo;
        this.billDate = billDate;
        this.billAmount = billAmount;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Double billAmount) {
        this.billAmount = billAmount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
