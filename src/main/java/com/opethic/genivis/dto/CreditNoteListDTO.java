package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CreditNoteListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty creditNoteNo;
    private SimpleStringProperty transactionDate;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty narration;
    private SimpleStringProperty totalAmount;

    public CreditNoteListDTO(String id, String creditNoteNo, String transactionDate,
                             String supplierName, String narration, String totalAmount) {
        this.id = new SimpleStringProperty(id);
        this.creditNoteNo = new SimpleStringProperty(creditNoteNo);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.supplierName = new SimpleStringProperty(supplierName);
        this.narration = new SimpleStringProperty(narration);
        this.totalAmount = new SimpleStringProperty(totalAmount);
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
        return creditNoteNo.get();
    }

    public SimpleStringProperty creditNoteNoProperty() {
        return creditNoteNo;
    }

    public void setCreditNoteNo(String creditNoteNo) {
        this.creditNoteNo = new SimpleStringProperty(creditNoteNo);
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public SimpleStringProperty transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate =new SimpleStringProperty(transactionDate);
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
        this.narration  = new SimpleStringProperty(narration);
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
