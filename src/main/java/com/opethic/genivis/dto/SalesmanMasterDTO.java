package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesmanMasterDTO {
    private SimpleIntegerProperty id;
    private SimpleStringProperty first_name;
    private SimpleStringProperty middle_name;
    private SimpleStringProperty last_name;
    private SimpleStringProperty mobile_number;
    private SimpleStringProperty salesman_add;
    private SimpleStringProperty pin_code;
    private SimpleStringProperty date_of_birth;

    public SalesmanMasterDTO(int id, String first_name, String middle_name, String last_name, String mobilenumber, String salesmanadd, String pincode, String dateofbirth) {
        this.id = new SimpleIntegerProperty(id);
        this.first_name = new SimpleStringProperty(first_name);
        this.middle_name = new SimpleStringProperty(middle_name);
        this.last_name = new SimpleStringProperty(last_name);
        this.mobile_number = new SimpleStringProperty(mobilenumber);
        this.salesman_add = new SimpleStringProperty(salesmanadd);
        this.pin_code = new SimpleStringProperty(pincode);
        this.date_of_birth =new SimpleStringProperty(dateofbirth);

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

    public String getFirst_name() {
        return first_name.get();
    }

    public SimpleStringProperty first_nameProperty() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name.set(first_name);
    }

    public String getMiddle_name() {
        return middle_name.get();
    }

    public SimpleStringProperty middle_nameProperty() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name.set(middle_name);
    }

    public String getLast_name() {
        return last_name.get();
    }

    public SimpleStringProperty last_nameProperty() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name.set(last_name);
    }

    public String getMobile_number() {
        return mobile_number.get();
    }

    public SimpleStringProperty mobile_numberProperty() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number.set(mobile_number);
    }

    public String getSalesman_add() {
        return salesman_add.get();
    }

    public SimpleStringProperty salesman_addProperty() {
        return salesman_add;
    }

    public void setSalesman_add(String salesman_add) {
        this.salesman_add.set(salesman_add);
    }

    public String getPin_code() {
        return pin_code.get();
    }

    public SimpleStringProperty pin_codeProperty() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code.set(pin_code);
    }

    public String getDate_of_birth() {
        return date_of_birth.get();
    }

    public SimpleStringProperty date_of_birthProperty() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth.set(date_of_birth);
    }
}


