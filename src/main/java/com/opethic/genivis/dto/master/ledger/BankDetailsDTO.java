package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BankDetailsDTO {
    private SimpleIntegerProperty id;
    private SimpleStringProperty bankName;
    private SimpleStringProperty bankAccNo;
    private SimpleStringProperty bankIFSCCode;
    private SimpleStringProperty bankBranch;

    public BankDetailsDTO(Integer id, String bankName, String bankAccNo, String bankIFSCCode, String bankBranch) {
        this.id = new SimpleIntegerProperty(id);
        this.bankName = new SimpleStringProperty(bankName);
        this.bankAccNo = new SimpleStringProperty(bankAccNo);
        this.bankIFSCCode = new SimpleStringProperty(bankIFSCCode);
        this.bankBranch = new SimpleStringProperty(bankBranch);
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

    public String getBankName() {
        return bankName.get();
    }

    public SimpleStringProperty bankNameProperty() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName.set(bankName);
    }

    public String getBankAccNo() {
        return bankAccNo.get();
    }

    public SimpleStringProperty bankAccNoProperty() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo.set(bankAccNo);
    }

    public String getBankIFSCCode() {
        return bankIFSCCode.get();
    }

    public SimpleStringProperty bankIFSCCodeProperty() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(String bankIFSCCode) {
        this.bankIFSCCode.set(bankIFSCCode);
    }

    public String getBankBranch() {
        return bankBranch.get();
    }

    public SimpleStringProperty bankBranchProperty() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch.set(bankBranch);
    }
}
