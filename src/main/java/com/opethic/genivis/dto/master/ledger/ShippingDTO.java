package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ShippingDTO {
    private SimpleStringProperty shippingName;
    private SimpleStringProperty shippingStateName;
    private SimpleIntegerProperty shippingStateId;

    public ShippingDTO(String shippingName, String shippingStateName, Integer shippingStateId) {
        this.shippingName = new SimpleStringProperty(shippingName);
        this.shippingStateName = new SimpleStringProperty(shippingStateName);
        this.shippingStateId = new SimpleIntegerProperty(shippingStateId);
    }

    public String getShippingName() {
        return shippingName.get();
    }

    public SimpleStringProperty shippingNameProperty() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName.set(shippingName);
    }

    public String getShippingStateName() {
        return shippingStateName.get();
    }

    public SimpleStringProperty shippingStateNameProperty() {
        return shippingStateName;
    }

    public void setShippingStateName(String shippingStateName) {
        this.shippingStateName.set(shippingStateName);
    }

    public int getShippingStateId() {
        return shippingStateId.get();
    }

    public SimpleIntegerProperty shippingStateIdProperty() {
        return shippingStateId;
    }

    public void setShippingStateId(int shippingStateId) {
        this.shippingStateId.set(shippingStateId);
    }
}
