package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CompanyListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty companyName;
    private SimpleStringProperty companyCode;
    private SimpleStringProperty registeredAddress;
    private SimpleStringProperty corporateAddress;
    private SimpleStringProperty companyMobileNo;


    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public CompanyListDTO(String id, String companyName, String companyCode, String registeredAddress, String corporateAddress, String companyMobileNo) {
        this.id = new SimpleStringProperty(id);
        this.companyName = new SimpleStringProperty(companyName);
        this.companyCode = new SimpleStringProperty(companyCode);
        this.registeredAddress = new SimpleStringProperty(registeredAddress);
        this.corporateAddress = new SimpleStringProperty(corporateAddress);
        this.companyMobileNo = new SimpleStringProperty(companyMobileNo);
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

    public String getCompanyCode() {
        return companyCode.get();
    }

    public SimpleStringProperty companyCodeProperty() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode.set(companyCode);
    }

    public String getRegisteredAddress() {
        return registeredAddress.get();
    }

    public SimpleStringProperty registeredAddressProperty() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress.set(registeredAddress);
    }

    public String getCorporateAddress() {
        return corporateAddress.get();
    }

    public SimpleStringProperty corporateAddressProperty() {
        return corporateAddress;
    }

    public void setCorporateAddress(String corporateAddress) {
        this.corporateAddress.set(corporateAddress);
    }

    public String getCompanyMobileNo() {
        return companyMobileNo.get();
    }

    public SimpleStringProperty companyMobileNoProperty() {
        return companyMobileNo;
    }

    public void setCompanyMobileNo(String companyMobileNo) {
        this.companyMobileNo.set(companyMobileNo);
    }


}
