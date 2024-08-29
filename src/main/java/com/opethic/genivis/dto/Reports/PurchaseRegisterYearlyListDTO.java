package com.opethic.genivis.dto.Reports;

import javafx.beans.property.SimpleStringProperty;

public class PurchaseRegisterYearlyListDTO {
    private SimpleStringProperty month;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;
    private SimpleStringProperty closingBalance;
    private SimpleStringProperty type;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;

    public PurchaseRegisterYearlyListDTO(String month, String debit, String credit,
                               String closingBalance, String type, String startDate, String endDate) {
        this.month = new SimpleStringProperty(month);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
        this.closingBalance =new SimpleStringProperty( closingBalance);
        this.type =new SimpleStringProperty( type);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String getDebit() {
        return debit.get();
    }

    public SimpleStringProperty debitProperty() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit.set(debit);
    }

    public String getCredit() {
        return credit.get();
    }

    public SimpleStringProperty creditProperty() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit.set(credit);
    }

    public String getClosingBalance() {
        return closingBalance.get();
    }

    public SimpleStringProperty closingBalanceProperty() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance.set(closingBalance);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }
}
