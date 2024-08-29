package com.opethic.genivis.dto.reqres.tranx.sales.invoice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TranxPedingsRes {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bill_no")
    @Expose
    private String billNo;
    @SerializedName("bill_date")
    @Expose
    private String billDate;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("sundry_debtors_name")
    @Expose
    private String sundryDebtorsName;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("tax_amt")
    @Expose
    private Double taxAmt;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;

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
}
