package com.opethic.genivis.dto.reqres.payment;

import javafx.beans.property.SimpleStringProperty;

public class PaymentListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty paymentNo;
    private SimpleStringProperty transactionDate;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty narration;
    private SimpleStringProperty totalAmount;
    private SimpleStringProperty paymentSrNo;
    private SimpleStringProperty action;

    public PaymentListDTO(String id, String paymentNo, String transactionDate,
                          String supplierName, String narration, String totalAmount, String paymentSrNo) {
        this.id = new SimpleStringProperty(id);
        this.paymentNo = new SimpleStringProperty(paymentNo);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.supplierName =new SimpleStringProperty( supplierName);
        this.narration =new SimpleStringProperty( narration);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.paymentSrNo = new SimpleStringProperty(paymentSrNo);
//        this.action = new SimpleStringProperty(action);
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

    public String getPaymentNo() {
        return paymentNo.get();
    }

    public SimpleStringProperty paymentNoProperty() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo.set(paymentNo);
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

    public String getPaymentSrNo() {
        return paymentSrNo.get();
    }

    public SimpleStringProperty paymentSrNoProperty() {
        return paymentSrNo;
    }

    public void setPaymentSrNo(String paymentSrNo) {
        this.paymentSrNo.set(paymentSrNo);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }
}
