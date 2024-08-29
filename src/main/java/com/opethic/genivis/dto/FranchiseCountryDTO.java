package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class FranchiseCountryDTO {

    private SimpleStringProperty text;
    private SimpleStringProperty id;

    public FranchiseCountryDTO(String text, String id) {
        this.text = new SimpleStringProperty(text);
        this.id = new SimpleStringProperty(id);
    }

    public String getText() {
        return text.get();
    }

    public SimpleStringProperty textProperty() {
        return text;
    }

    public void setText(String text) {
        this.text.set(text);
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
}
