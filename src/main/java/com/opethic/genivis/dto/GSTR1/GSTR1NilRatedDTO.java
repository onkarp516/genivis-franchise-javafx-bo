package com.opethic.genivis.dto.GSTR1;

import javafx.beans.property.SimpleStringProperty;

public class GSTR1NilRatedDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty voucherType;
    private SimpleStringProperty particulars;
    private SimpleStringProperty nilRated;
    private SimpleStringProperty exempted;
    private SimpleStringProperty taxType;

    public GSTR1NilRatedDTO(String id, String voucherType, String particulars, String nilRated, String exempted,String taxType) {
        this.id =new SimpleStringProperty(id);
        this.voucherType =new SimpleStringProperty(voucherType);
        this.particulars =new SimpleStringProperty(particulars);
        this.nilRated =new SimpleStringProperty(nilRated);
        this.exempted =new SimpleStringProperty(exempted);
        this.taxType =new SimpleStringProperty(taxType);
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

    public String getVoucherType() {
        return voucherType.get();
    }

    public SimpleStringProperty voucherTypeProperty() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType.set(voucherType);
    }

    public String getParticulars() {
        return particulars.get();
    }

    public SimpleStringProperty particularsProperty() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars.set(particulars);
    }

    public String getNilRated() {
        return nilRated.get();
    }

    public SimpleStringProperty nilRatedProperty() {
        return nilRated;
    }

    public void setNilRated(String nilRated) {
        this.nilRated.set(nilRated);
    }

    public String getExempted() {
        return exempted.get();
    }

    public SimpleStringProperty exemptedProperty() {
        return exempted;
    }

    public void setExempted(String exempted) {
        this.exempted.set(exempted);
    }

    public String getTaxType() {
        return taxType.get();
    }

    public SimpleStringProperty taxTypeProperty() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType.set(taxType);
    }
}
