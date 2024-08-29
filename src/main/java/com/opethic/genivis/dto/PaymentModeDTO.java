package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class PaymentModeDTO {
    private SimpleStringProperty id;

    private SimpleStringProperty paymentMode;

    public PaymentModeDTO() {
    }

    public PaymentModeDTO(String id, String paymentMode) {
        this.id = new SimpleStringProperty(id);
        this.paymentMode =new SimpleStringProperty( paymentMode);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }

    public String getPaymentMode() {
        return paymentMode.get();
    }

    public SimpleStringProperty paymentModeProperty() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode=new SimpleStringProperty(paymentMode);
    }



}
