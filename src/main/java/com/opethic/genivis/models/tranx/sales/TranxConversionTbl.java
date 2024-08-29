package com.opethic.genivis.models.tranx.sales;

public class TranxConversionTbl {
    private boolean isSelected;
    private int id;
    private String billNo;
    private String billDate;
    private String ledgerName;
    private String narration;
    private Double taxableAmt;
    private Double taxAmt;
    private Double totalAmt;

    public TranxConversionTbl(boolean isSelected, int id, String billNo, String billDate, String ledgerName, String narration, Double taxableAmt, Double taxAmt, Double totalAmt) {
        this.isSelected = isSelected;
        this.id = id;
        this.billNo = billNo;
        this.billDate = billDate;
        this.ledgerName = ledgerName;
        this.narration = narration;
        this.taxableAmt = taxableAmt;
        this.taxAmt = taxAmt;
        this.totalAmt = totalAmt;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public Double getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(Double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }
}
