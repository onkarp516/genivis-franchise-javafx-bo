package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GstListDTO {
    private SimpleIntegerProperty id;
    private SimpleIntegerProperty gstTypeId;
    private SimpleStringProperty gstTypeName;
    private SimpleStringProperty gstRegDate;
    private SimpleStringProperty gstNo;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getGstTypeId() {
        return gstTypeId.get();
    }

    public SimpleIntegerProperty gstTypeIdProperty() {
        return gstTypeId;
    }

    public void setGstTypeId(int gstTypeId) {
        this.gstTypeId.set(gstTypeId);
    }

    public String getGstTypeName() {
        return gstTypeName.get();
    }

    public SimpleStringProperty gstTypeNameProperty() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName.set(gstTypeName);
    }

    public String getGstRegDate() {
        return gstRegDate.get();
    }

    public SimpleStringProperty gstRegDateProperty() {
        return gstRegDate;
    }

    public void setGstRegDate(String gstRegDate) {
        this.gstRegDate.set(gstRegDate);
    }

    public String getGstNo() {
        return gstNo.get();
    }

    public SimpleStringProperty gstNoProperty() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo.set(gstNo);
    }

    public String getPanNo() {
        return panNo.get();
    }

    public SimpleStringProperty panNoProperty() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo.set(panNo);
    }

    private SimpleStringProperty panNo;

    public GstListDTO(int inId, int inGstTypeId, String inGSTTypeName, String inGSTRegDate, String inGstNo, String inPanNo) {
        id = new SimpleIntegerProperty(inId);
        gstTypeId = new SimpleIntegerProperty(inGstTypeId);
        gstTypeName = new SimpleStringProperty(inGSTTypeName);
        gstRegDate = new SimpleStringProperty(inGSTRegDate);
        gstNo = new SimpleStringProperty(inGstNo);
        panNo = new SimpleStringProperty(inPanNo);
    }
}
