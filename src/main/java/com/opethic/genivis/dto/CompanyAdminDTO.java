package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CompanyAdminDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty companyName;
    private SimpleStringProperty fullName;
    private SimpleStringProperty mobileNumber;
    private SimpleStringProperty email;
    private SimpleStringProperty gender;
    private SimpleStringProperty usercode;
    private SimpleStringProperty isSwitch;

    public CompanyAdminDTO(){

    }

    public CompanyAdminDTO(String id, String companyName, String fullName, String mobileNumber,
                           String email, String gender, String usercode, String isSwitch) {
        this.id = new SimpleStringProperty(id);
        this.companyName = new SimpleStringProperty( companyName);
        this.fullName = new SimpleStringProperty(fullName);
        this.mobileNumber =new SimpleStringProperty( mobileNumber);
        this.email =new SimpleStringProperty( email);
        this.gender =new SimpleStringProperty( gender);
        this.usercode = new SimpleStringProperty(usercode);
        this.isSwitch =new SimpleStringProperty( isSwitch);
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

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = new SimpleStringProperty(companyName);
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

    public String getMobileNumber() {
        return mobileNumber.get();
    }

    public SimpleStringProperty mobileNumberProperty() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = new SimpleStringProperty(mobileNumber);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email = new SimpleStringProperty(email);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender =new SimpleStringProperty(gender);
    }

    public String getUsercode() {
        return usercode.get();
    }

    public SimpleStringProperty usercodeProperty() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode =new SimpleStringProperty(usercode);
    }

    public String getIsSwitch() {
        return isSwitch.get();
    }

    public SimpleStringProperty isSwitchProperty() {
        return isSwitch;
    }

    public void setIsSwitch(String isSwitch) {
        this.isSwitch =new SimpleStringProperty(isSwitch);
    }
}
