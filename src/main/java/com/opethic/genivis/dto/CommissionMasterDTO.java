package com.opethic.genivis.dto;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CommissionMasterDTO {

    private SimpleDoubleProperty tdsPer;

    private SimpleStringProperty productLevel;
    private SimpleIntegerProperty id;

    private SimpleStringProperty roleType;

    public Double getTdsPer() {
        return tdsPer.get();
    }

    public SimpleDoubleProperty tdsPerProperty() {
        return tdsPer;
    }

    public void setTdsPer(int tdsPer) {
        this.tdsPer.set(tdsPer);
    }

    public String getProductLevel() {
        return productLevel.get();
    }

    public SimpleStringProperty productLevelProperty() {
        return productLevel;
    }

    public void setProductLevel(String productLevel) {
        this.productLevel.set(productLevel);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getRoleType() {
        return roleType.get();
    }

    public SimpleStringProperty roleTypeProperty() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType.set(roleType);
    }

    public CommissionMasterDTO(Double tdsPer, String productLevel, Integer id, String roleType) {
        this.tdsPer = new SimpleDoubleProperty(tdsPer);
        this.productLevel = new SimpleStringProperty(productLevel);
        this.id = new SimpleIntegerProperty(id);
        this.roleType = new SimpleStringProperty(roleType);
    }
}
