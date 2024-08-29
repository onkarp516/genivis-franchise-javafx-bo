package com.opethic.genivis.dto.reqres.journal;

import javafx.beans.property.SimpleStringProperty;

public class JournalRowDTO {

    private SimpleStringProperty id = new SimpleStringProperty("0");
    private SimpleStringProperty current_index;

    private SimpleStringProperty type;
    private SimpleStringProperty particulars;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;

    public JournalRowDTO() {
    }

    public JournalRowDTO(String current_index, String type, String particulars, String debit, String credit) {
        this.current_index = new SimpleStringProperty(current_index);
        this.type = new SimpleStringProperty(type);
        this.particulars = new SimpleStringProperty(particulars);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
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

    public String getCurrent_index() {
        return current_index.get();
    }

    public SimpleStringProperty current_indexProperty() {
        return current_index;
    }

    public void setCurrent_index(String current_index) {
        this.current_index.set(current_index);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
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

    public String getDebit() {
        return debit.get();
    }

    public SimpleStringProperty debitProperty() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit.set(debit);
    }

    public String getCredit() {
        return credit.get();
    }

    public SimpleStringProperty creditProperty() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit.set(credit);
    }
}
