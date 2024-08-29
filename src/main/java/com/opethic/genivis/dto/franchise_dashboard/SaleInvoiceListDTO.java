package com.opethic.genivis.dto.franchise_dashboard;

import javafx.beans.property.SimpleStringProperty;

public class SaleInvoiceListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty SrNo;
    private SimpleStringProperty ledgerName;
    private SimpleStringProperty amount;

    public SaleInvoiceListDTO(String id, String srNo, String ledgerName, String amount) {
        this.id = new SimpleStringProperty(id);
        SrNo = new SimpleStringProperty(srNo);
        this.ledgerName = new SimpleStringProperty(ledgerName);
        this.amount = new SimpleStringProperty(amount);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSrNo() {
        return SrNo.get();
    }

    public SimpleStringProperty srNoProperty() {
        return SrNo;
    }

    public void setSrNo(String srNo) {
        this.SrNo.set(srNo);
    }

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
}
