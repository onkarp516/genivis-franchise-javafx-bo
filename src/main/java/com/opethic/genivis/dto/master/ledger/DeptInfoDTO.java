package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleStringProperty;

public class DeptInfoDTO {
    private SimpleStringProperty personDeptName;
    private SimpleStringProperty personName;
    private SimpleStringProperty personEmail;
    private SimpleStringProperty personPhone;

    public DeptInfoDTO(String personDeptName, String personName, String personEmail, String personPhone) {
        this.personDeptName = new SimpleStringProperty(personDeptName);
        this.personName = new SimpleStringProperty(personName);
        this.personEmail = new SimpleStringProperty(personEmail);
        this.personPhone = new SimpleStringProperty(personPhone);
    }

    public String getPersonDeptName() {
        return personDeptName.get();
    }

    public SimpleStringProperty personDeptNameProperty() {
        return personDeptName;
    }

    public void setPersonDeptName(String personDeptName) {
        this.personDeptName.set(personDeptName);
    }

    public String getPersonName() {
        return personName.get();
    }

    public SimpleStringProperty personNameProperty() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName.set(personName);
    }

    public String getPersonEmail() {
        return personEmail.get();
    }

    public SimpleStringProperty personEmailProperty() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail.set(personEmail);
    }

    public String getPersonPhone() {
        return personPhone.get();
    }

    public SimpleStringProperty personPhoneProperty() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone.set(personPhone);
    }
}
