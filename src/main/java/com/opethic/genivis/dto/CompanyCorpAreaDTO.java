package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CompanyCorpAreaDTO {
    private SimpleStringProperty area;
    private SimpleStringProperty id;
    private SimpleStringProperty pincode;
    private SimpleStringProperty district;
    private SimpleStringProperty state;
    private SimpleStringProperty stateCode;

    public CompanyCorpAreaDTO(String area, String id, String pincode, String district, String state, String stateCode) {
        this.area = new SimpleStringProperty(area);
        this.id = new SimpleStringProperty(id);
        this.pincode = new SimpleStringProperty(pincode);
        this.district = new SimpleStringProperty(district);
        this.state = new SimpleStringProperty(state);
        this.stateCode = new SimpleStringProperty(stateCode);
    }



    public String getArea() {
        return area.get();
    }

    public SimpleStringProperty areaProperty() {
        return area;
    }

    public void setArea(String area) {
        this.area.set(area);
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

    public String getPincode() {
        return pincode.get();
    }

    public SimpleStringProperty pincodeProperty() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode.set(pincode);
    }

    public String getDistrict() {
        return district.get();
    }

    public SimpleStringProperty districtProperty() {
        return district;
    }

    public void setDistrict(String district) {
        this.district.set(district);
    }

    public String getState() {
        return state.get();
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state.set(state);
    }

    public String getStateCode() {
        return stateCode.get();
    }

    public SimpleStringProperty stateCodeProperty() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode.set(stateCode);
    }
}
