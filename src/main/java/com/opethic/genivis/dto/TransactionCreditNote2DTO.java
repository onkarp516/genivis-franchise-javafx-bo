package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class TransactionCreditNote2DTO {
    private SimpleStringProperty months;
    private SimpleStringProperty no_vouchers;
    private SimpleStringProperty total_amt;

    public TransactionCreditNote2DTO(
            String months,
            String no_vouchers,
            String total_amt) {
        this.months = new SimpleStringProperty(months);
        this.no_vouchers = new SimpleStringProperty(no_vouchers);
        this.total_amt = new SimpleStringProperty(total_amt);
    }

    public String getMonth() {
        return getMonths();
    }

    public String getNoVoucher() {
        return getNo_vouchers();
    }

    public String getTotalAmt() {
        return getTotal_amt();
    }

    public String getMonths() {
        return months.get();
    }

    public SimpleStringProperty monthsProperty() {
        return months;
    }

    public void setMonths(String months) {
        this.months.set(months);
    }

    public String getNo_vouchers() {
        return no_vouchers.get();
    }

    public SimpleStringProperty no_vouchersProperty() {
        return no_vouchers;
    }

    public void setNo_vouchers(String no_vouchers) {
        this.no_vouchers.set(no_vouchers);
    }

    public String getTotal_amt() {
        return total_amt.get();
    }

    public SimpleStringProperty total_amtProperty() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt.set(total_amt);
    }
}