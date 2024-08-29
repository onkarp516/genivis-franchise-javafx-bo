package com.opethic.genivis.dto.account_entry;

import javafx.beans.property.SimpleStringProperty;

public class ContraListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty contraNo;
    private SimpleStringProperty contraTranxDate;
    private SimpleStringProperty contraSupplierName;
    private SimpleStringProperty contraNarration;
    private SimpleStringProperty contraTotalAmount;
    private SimpleStringProperty contraAction;

    public ContraListDTO(String id, String contraNo, String contraTranxDate, String contraSupplierName, String contraNarration, String contraTotalAmount, String contraAction) {
        this.id = new SimpleStringProperty(id);
        this.contraNo = new SimpleStringProperty(contraNo);
        this.contraTranxDate = new SimpleStringProperty(contraTranxDate);
        this.contraSupplierName = new SimpleStringProperty(contraSupplierName);
        this.contraNarration = new SimpleStringProperty(contraNarration);
        this.contraTotalAmount = new SimpleStringProperty(contraTotalAmount);
        this.contraAction = new SimpleStringProperty(contraAction);
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

    public String getContraNo() {
        return contraNo.get();
    }

    public SimpleStringProperty contraNoProperty() {
        return contraNo;
    }

    public void setContraNo(String contraNo) {
        this.contraNo.set(contraNo);
    }

    public String getContraTranxDate() {
        return contraTranxDate.get();
    }

    public SimpleStringProperty contraTranxDateProperty() {
        return contraTranxDate;
    }

    public void setContraTranxDate(String contraTranxDate) {
        this.contraTranxDate.set(contraTranxDate);
    }

    public String getContraSupplierName() {
        return contraSupplierName.get();
    }

    public SimpleStringProperty contraSupplierNameProperty() {
        return contraSupplierName;
    }

    public void setContraSupplierName(String contraSupplierName) {
        this.contraSupplierName.set(contraSupplierName);
    }

    public String getContraNarration() {
        return contraNarration.get();
    }

    public SimpleStringProperty contraNarrationProperty() {
        return contraNarration;
    }

    public void setContraNarration(String contraNarration) {
        this.contraNarration.set(contraNarration);
    }

    public String getContraTotalAmount() {
        return contraTotalAmount.get();
    }

    public SimpleStringProperty contraTotalAmountProperty() {
        return contraTotalAmount;
    }

    public void setContraTotalAmount(String contraTotalAmount) {
        this.contraTotalAmount.set(contraTotalAmount);
    }

    public String getContraAction() {
        return contraAction.get();
    }

    public SimpleStringProperty contraActionProperty() {
        return contraAction;
    }

    public void setContraAction(String contraAction) {
        this.contraAction.set(contraAction);
    }
}
