package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class PaymentDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty paymentNo;
    private SimpleStringProperty transactionDate;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty narration;
    private SimpleStringProperty totalAmount;

    public PaymentDTO(String id, String paymentNo, String transactionDate, String supplierName, String Narration,
                            String totalAmount) {
        this.id = new SimpleStringProperty(id);
        this.paymentNo = new SimpleStringProperty(paymentNo);
        this.transactionDate = new SimpleStringProperty(transactionDate);
        this.supplierName =  new SimpleStringProperty(supplierName);
        this.narration =  new SimpleStringProperty(Narration);
        this.totalAmount =  new SimpleStringProperty(totalAmount);
    }
    public String getId() {return id.get();}

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) { this.id=new SimpleStringProperty(id);}

    public String getPaymentNo() {
        return paymentNo.get();
    }
    public String getTransactionDate() {
        return transactionDate.get();
    }
    public String getSupplierName() {
        return supplierName.get();
    }
    public String getNarration() {
        return narration.get();
    }
    public String getTotalamount() {
        return totalAmount.get();
    }

    public SimpleStringProperty paymentNoProperty() {
        return paymentNo;
    }

    public void setInvoice_no(String paymentNo) {
        this.paymentNo=new SimpleStringProperty(paymentNo);
    }

    public SimpleStringProperty transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate=new SimpleStringProperty(transactionDate);
    }
    public SimpleStringProperty sundry_creditor_nameProperty() {
        return supplierName;
    }

    public void setsupplierName(String supplierName) {
        this.supplierName=new SimpleStringProperty(supplierName);
    }



    public SimpleStringProperty narrationProperty() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration=new SimpleStringProperty(narration);
    }
    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String total_amount) {
        this.totalAmount=new SimpleStringProperty(total_amount);
    }

}
