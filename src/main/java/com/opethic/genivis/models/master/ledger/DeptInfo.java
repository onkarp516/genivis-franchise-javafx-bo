package com.opethic.genivis.models.master.ledger;

public class DeptInfo {
    private int id;
    private String personDeptName;
    private String personName;
    private String personEmail;
    private String personPhone;

    public DeptInfo(int id, String personDeptName, String personName, String personEmail, String personPhone) {
        this.id = id;
        this.personDeptName = personDeptName;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personPhone = personPhone;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonDeptName() {
        return personDeptName;
    }

    public void setPersonDeptName(String personDeptName) {
        this.personDeptName = personDeptName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonEmail() {
        return personEmail;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }

    public String getPersonPhone() {
        return personPhone;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }
}
