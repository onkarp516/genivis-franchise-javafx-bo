package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreaMasterDTO {
    private SimpleIntegerProperty id;
    private SimpleStringProperty areaName;
    private SimpleStringProperty areaCode;
    private SimpleStringProperty pinCode;

    // Constructor for creating an instance with provided values
    public AreaMasterDTO(Integer id, String areaName, String areaCode, String pinCode) {
        this.id = new SimpleIntegerProperty(id);
        this.areaName = new SimpleStringProperty(areaName);
        this.areaCode = new SimpleStringProperty(areaCode);
        this.pinCode = new SimpleStringProperty(pinCode);
    }

    // Constructor for creating an instance from TextFields (useful in UI contexts)

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }


    // Getters and property accessors

    public String getAreaName() { return areaName.get(); }
    public SimpleStringProperty areaNameProperty() { return areaName; }

    public String getAreaCode() { return areaCode.get(); }
    public SimpleStringProperty areaCodeProperty() { return areaCode; }

    public String getPinCode() { return pinCode.get(); }
    public SimpleStringProperty pinCodeProperty() { return pinCode; }

    // Setters

    public void setAreaName(String areaName) { this.areaName.set(areaName); }
    public void setAreaCode(String areaCode) { this.areaCode.set(areaCode); }
    public void setPinCode(String pinCode) { this.pinCode.set(pinCode); }
}
