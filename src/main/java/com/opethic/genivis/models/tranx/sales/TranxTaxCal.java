package com.opethic.genivis.models.tranx.sales;

public class TranxTaxCal {
    private Double tax;
    private Double taxAmt;

    public TranxTaxCal(Double tax, Double taxAmt) {
        this.tax = tax;
        this.taxAmt = taxAmt;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    @Override
    public String toString() {
        return "TranxTaxCal{" +
                "tax=" + tax +
                ", taxAmt=" + taxAmt +
                '}';
    }
}
