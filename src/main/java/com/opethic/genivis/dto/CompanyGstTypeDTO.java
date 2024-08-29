package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CompanyGstTypeDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty gstType;

    public CompanyGstTypeDTO(String id, String gstType) {
        this.id = new SimpleStringProperty(id);
        this.gstType = new SimpleStringProperty(gstType);
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

    public String getType() {
        return gstType.get();
    }

    public SimpleStringProperty gstTypeProperty() {
        return gstType;
    }

    public void setType(String gstType) {
        this.gstType.set(gstType);
    }
}
