package com.opethic.genivis.dto.reqres.receipt;

import javafx.beans.property.SimpleStringProperty;

public class ReceiptListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty receiptNo;
    private SimpleStringProperty transactionDate;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty narration;
    private SimpleStringProperty totalAmount;
    private SimpleStringProperty receiptSrNo;
    private SimpleStringProperty autoGenerated;
    private SimpleStringProperty isFrReceipt;


    public ReceiptListDTO(String id, String receiptNo, String transactionDate, String supplierName,
                          String narration, String totalAmount, String receiptSrNo,String autoGenerated,String isFrReceipt) {
        this.id = new SimpleStringProperty(id);
        this.receiptNo = new SimpleStringProperty(receiptNo);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.supplierName =new SimpleStringProperty( supplierName);
        this.narration =new SimpleStringProperty( narration);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.receiptSrNo = new SimpleStringProperty(receiptSrNo);
        this.autoGenerated = new SimpleStringProperty(autoGenerated);
        this.isFrReceipt = new SimpleStringProperty(isFrReceipt);
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

    public String getReceiptNo() {
        return receiptNo.get();
    }

    public SimpleStringProperty receiptNoProperty() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo.set(receiptNo);
    }

    public String getTransactionDate() {
        return transactionDate.get();
    }

    public SimpleStringProperty transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate.set(transactionDate);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public String getNarration() {
        return narration.get();
    }

    public SimpleStringProperty narrationProperty() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration.set(narration);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getReceiptSrNo() {
        return receiptSrNo.get();
    }

    public SimpleStringProperty receiptSrNoProperty() {
        return receiptSrNo;
    }

    public void setReceiptSrNo(String receiptSrNo) {
        this.receiptSrNo.set(receiptSrNo);
    }

    public String getAutoGenerated() {
        return autoGenerated.get();
    }

    public SimpleStringProperty autoGeneratedProperty() {
        return autoGenerated;
    }

    public void setAutoGenerated(String autoGenerated) {
        this.autoGenerated.set(autoGenerated);
    }

    public String getIsFrReceipt() {
        return isFrReceipt.get();
    }

    public SimpleStringProperty isFrReceiptProperty() {
        return isFrReceipt;
    }

    public void setIsFrReceipt(String isFrReceipt) {
        this.isFrReceipt.set(isFrReceipt);
    }
}
