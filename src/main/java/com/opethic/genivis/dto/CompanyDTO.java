package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class CompanyDTO {

    private SimpleStringProperty text;
    private SimpleStringProperty id;

    public CompanyDTO() {
    }

    public CompanyDTO(String text, String id) {
        this.text = new SimpleStringProperty(text);
        this.id = new SimpleStringProperty(id);
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text =new SimpleStringProperty( text);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }
}
