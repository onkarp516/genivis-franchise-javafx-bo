package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class FranchiseListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty franchiseName;
    private SimpleStringProperty franchiseCode;
    private SimpleStringProperty franchiseAddress;
    private SimpleStringProperty franchisemobileNumber;
    private SimpleStringProperty franchisepincode;

    public FranchiseListDTO(String id, String franchiseName, String franchiseCode,
                            String franchiseAddress, String franchisemobileNumber, String franchisepincode) {
        this.id = new SimpleStringProperty(id);
        this.franchiseName = new SimpleStringProperty(franchiseName);
        this.franchiseCode = new SimpleStringProperty(franchiseCode);
        this.franchiseAddress = new SimpleStringProperty(franchiseAddress);
        this.franchisemobileNumber = new SimpleStringProperty(franchisemobileNumber);
        this.franchisepincode = new SimpleStringProperty(franchisepincode);
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

    public String getFranchiseName() {
        return franchiseName.get();
    }

    public SimpleStringProperty franchiseNameProperty() {
        return franchiseName;
    }

    public void setFranchiseName(String franchiseName) {
        this.franchiseName=new SimpleStringProperty(franchiseName);
    }

    public String getFranchiseCode() {
        return franchiseCode.get();
    }

    public SimpleStringProperty franchiseCodeProperty() {
        return franchiseCode;
    }

    public void setFranchiseCode(String franchiseCode) {
        this.franchiseCode=new SimpleStringProperty(franchiseCode);
    }

    public String getFranchiseAddress() {
        return franchiseAddress.get();
    }

    public SimpleStringProperty franchiseAddressProperty() {
        return franchiseAddress;
    }

    public void setFranchiseAddress(String franchiseAddress) {
        this.franchiseAddress=new SimpleStringProperty(franchiseAddress);
    }

    public String getFranchisemobileNumber() {
        return franchisemobileNumber.get();
    }

    public SimpleStringProperty franchisemobilenumberProperty() {
        return franchisemobileNumber;
    }

    public void setFranchisemobileNumber(String franchisemobileNumber) {
        this.franchisemobileNumber=new SimpleStringProperty(franchisemobileNumber);
    }

    public String getFranchisepincode() {
        return franchisepincode.get();
    }

    public SimpleStringProperty franchisepincodeProperty() {
        return franchisepincode;
    }

    public void setFranchisepincode(String franchisepincode) {
        this.franchisepincode=new SimpleStringProperty(franchisepincode);
    }
}
