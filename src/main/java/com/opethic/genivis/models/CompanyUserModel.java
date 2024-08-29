package com.opethic.genivis.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CompanyUserModel {
    private SimpleStringProperty fullName;
    private SimpleStringProperty id;       //id of comp user created
    private SimpleStringProperty roleName;
    private SimpleIntegerProperty roleId;     //id of role ex. manager, operator
    private SimpleIntegerProperty srNo;
    private SimpleStringProperty companyId;
    private SimpleStringProperty mobileNumber;
    private SimpleStringProperty email;
    private SimpleStringProperty gender;
    private SimpleStringProperty  usercode;
    private SimpleStringProperty  companyName;
    private SimpleStringProperty isSwitch;

    public CompanyUserModel(int id,String roleName){
        this.roleId = new SimpleIntegerProperty(id);
        this.roleName = new SimpleStringProperty(roleName);

    }
    public CompanyUserModel(String fullName, String companyId) {
        this.companyName = new SimpleStringProperty(fullName);
        this.companyId = new SimpleStringProperty(companyId);
    }

    public CompanyUserModel(Integer serialNo,String id, String comName, String name, String moNo, String mail, String gender, String usercode,String isSwitch) { //constructor for user list
        this.srNo = new SimpleIntegerProperty(serialNo);
        this.id = new SimpleStringProperty(id);
        this.companyName = new SimpleStringProperty(comName);
        this.fullName=new SimpleStringProperty(name);
        this.mobileNumber = new SimpleStringProperty(moNo);
        this.email = new SimpleStringProperty(mail);
        this.gender = new SimpleStringProperty(gender);
        this.usercode = new SimpleStringProperty(usercode);
        this.isSwitch = new SimpleStringProperty(isSwitch);
    }


    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
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

    public String getRoleName() {
        return roleName.get();
    }

    public SimpleStringProperty roleNameProperty() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName.set(roleName);
    }

    public int getRoleId() {
        return roleId.get();
    }

    public SimpleIntegerProperty roleIdProperty() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId.set(roleId);
    }

    public String getCompanyId() {
        return companyId.get();
    }

    public SimpleStringProperty companyIdProperty() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId.set(companyId);
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

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getUsercode() {
        return usercode.get();
    }

    public SimpleStringProperty usercodeProperty() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode.set(usercode);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public Integer getSrNo() {
        return srNo.get();
    }

    public SimpleIntegerProperty srNoProperty() {
        return srNo;
    }

    public void setSrNo(Integer srNo) {
        this.srNo.set(srNo);
    }

    public String getIsSwitch() {
        return isSwitch.get();
    }

    public SimpleStringProperty isSwitchProperty() {
        return isSwitch;
    }

    public void setIsSwitch(String isSwitch) {
        this.isSwitch.set(isSwitch);
    }
}
