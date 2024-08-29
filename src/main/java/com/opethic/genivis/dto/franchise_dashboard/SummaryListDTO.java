package com.opethic.genivis.dto.franchise_dashboard;

import javafx.beans.property.SimpleStringProperty;

public class SummaryListDTO {
    private SimpleStringProperty ledgerName;
    private SimpleStringProperty amount;
    private SimpleStringProperty overDueDays;

    public SummaryListDTO(String ledgerName, String amount, String overDueDays) {
        this.ledgerName = new SimpleStringProperty(ledgerName);
        this.amount = new SimpleStringProperty(amount);
        this.overDueDays = new SimpleStringProperty(overDueDays);
    }
// Getters and Property methods...


    public String getLedgerName() {
        return ledgerName.get();
    }

    public SimpleStringProperty ledgerNameProperty() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName.set(ledgerName);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getOverDueDays() {
        return overDueDays.get();
    }

    public SimpleStringProperty overDueDaysProperty() {
        return overDueDays;
    }

    public void setOverDueDays(String overDueDays) {
        this.overDueDays.set(overDueDays);
    }
}
