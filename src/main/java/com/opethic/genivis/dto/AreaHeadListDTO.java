package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class AreaHeadListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty fullName;

    private SimpleStringProperty firstName;
    private SimpleStringProperty middleName;
    private SimpleStringProperty lastName;
    private SimpleStringProperty email;
    private SimpleStringProperty mobileNumber;
    private SimpleStringProperty whatsAppNumber;
    private SimpleStringProperty DOB;
    private SimpleStringProperty address;
    private SimpleStringProperty gender;
    private SimpleStringProperty aadharCard;
    private SimpleStringProperty panCard;
    private SimpleStringProperty areaRole;
    private SimpleStringProperty isActive;
    private SimpleStringProperty state;

    public AreaHeadListDTO(String id, String fullName,
//
                           String address,
                           String mobileNumber,
                           String areaRole
//                           String dob
//
                           ) {
        this.id = new SimpleStringProperty(id);
        this.fullName = new SimpleStringProperty(fullName);
        this.address = new SimpleStringProperty(address);
        this.mobileNumber = new SimpleStringProperty(mobileNumber);
        this.areaRole = new SimpleStringProperty(areaRole);
//        this.DOB = new SimpleStringProperty(dob);
//        this.state = new SimpleStringProperty(state);

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




    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName=new SimpleStringProperty(fullName);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address=new SimpleStringProperty(address);
    }

    public String getMobileNumber() {
        return mobileNumber.get();
    }

    public SimpleStringProperty mobileNumberProperty() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber.set(mobileNumber);
    }

    public String getAreaRole() {
        return areaRole.get();
    }

    public SimpleStringProperty areaRoleProperty() {
        return areaRole;
    }

    public void setAreaRole(String areaRole) {
        this.areaRole=new SimpleStringProperty(areaRole);
    }

//    public String getDob() {
//        return dob.get();
//    }
//
//    public SimpleStringProperty DobProperty() {
//        return dob;
//    }
//
//    public void setDob(String dob) {
//        this.dob=new SimpleStringProperty(dob);
//    }



    public String getState() {
        return state.get();
    }

    public SimpleStringProperty StateProperty() {
        return state;
    }

    public void setState(String state) {
        this.state=new SimpleStringProperty(state);
    }

    public SimpleStringProperty stateProperty() {
        return state;
    }



}
