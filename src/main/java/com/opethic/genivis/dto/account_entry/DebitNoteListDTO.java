package com.opethic.genivis.dto.account_entry;

import javafx.beans.property.SimpleStringProperty;

public class DebitNoteListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty debitNoteNo;
    private SimpleStringProperty transactionDate;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty narration;
    private SimpleStringProperty totalAmount;
    private SimpleStringProperty action;

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public DebitNoteListDTO(String id, String debitNoteNo, String transactionDate,
                            String supplierName, String narration, String totalAmount, String action) {
        this.id = new SimpleStringProperty(id);
        this.debitNoteNo = new SimpleStringProperty(debitNoteNo);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.narration = new SimpleStringProperty(narration);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.action = new SimpleStringProperty(action);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getCreditNoteNo() {
        return debitNoteNo.get();
    }

    public SimpleStringProperty debitNoteNoProperty() {
        return debitNoteNo;
    }

    public void setCreditNoteNo(String debitNoteNo) {
        this.debitNoteNo = new SimpleStringProperty(debitNoteNo);
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public SimpleStringProperty transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = new SimpleStringProperty(transactionDate);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = new SimpleStringProperty(supplierName);
    }

    public String getNarration() {
        return narration.get();
    }

    public SimpleStringProperty narrationProperty() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = new SimpleStringProperty(narration);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = new SimpleStringProperty(totalAmount);
    }
}
