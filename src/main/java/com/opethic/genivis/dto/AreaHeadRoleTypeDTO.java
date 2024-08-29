package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreaHeadRoleTypeDTO {

    private Integer id;
    private String firstName;
    private String value;


//    public AreaHeadRoleTypeDTO(int id,String firstName) {
//        this.id = new SimpleStringProperty(id);
//        this.firstName = new SimpleStringProperty(firstName);
//    }


    public AreaHeadRoleTypeDTO(Integer id, String firstName, String value) {
        this.id = id;
        this.firstName = firstName;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return firstName;
    }

}
