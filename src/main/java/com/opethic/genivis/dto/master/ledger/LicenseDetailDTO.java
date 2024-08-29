package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleStringProperty;

public class LicenseDetailDTO {
    private SimpleStringProperty licenseNo;
    private SimpleStringProperty licenseType;
    private SimpleStringProperty licenseExpDate;
    private SimpleStringProperty licenseDocURL;

    public LicenseDetailDTO(String licenseNo, String licenseType, String licenseExpDate, String licenseDocURL) {
        this.licenseNo = new SimpleStringProperty(licenseNo);
        this.licenseType = new SimpleStringProperty(licenseType);
        this.licenseExpDate = new SimpleStringProperty(licenseExpDate);
        this.licenseDocURL = new SimpleStringProperty(licenseDocURL);
    }

    public String getLicenseNo() {
        return licenseNo.get();
    }

    public SimpleStringProperty licenseNoProperty() {
        return licenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        this.licenseNo.set(licenseNo);
    }

    public String getLicenseType() {
        return licenseType.get();
    }

    public SimpleStringProperty licenseTypeProperty() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType.set(licenseType);
    }

    public String getLicenseExpDate() {
        return licenseExpDate.get();
    }

    public SimpleStringProperty licenseExpDateProperty() {
        return licenseExpDate;
    }

    public void setLicenseExpDate(String licenseExpDate) {
        this.licenseExpDate.set(licenseExpDate);
    }

    public String getLicenseDocURL() {
        return licenseDocURL.get();
    }

    public SimpleStringProperty licenseDocURLProperty() {
        return licenseDocURL;
    }

    public void setLicenseDocURL(String licenseDocURL) {
        this.licenseDocURL.set(licenseDocURL);
    }
}
