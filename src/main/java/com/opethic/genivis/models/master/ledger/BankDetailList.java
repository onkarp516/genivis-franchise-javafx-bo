package com.opethic.genivis.models.master.ledger;


public class BankDetailList {
    private int id;
    private String bankName;
    private String bankAccNo;
    private String bankIFSCCode;
    private String bankBranch;


    public BankDetailList(int id, String bankName, String bankAccNo, String bankIFSCCode, String bankBranch) {
        this.id = id;
        this.bankName = bankName;
        this.bankAccNo = bankAccNo;
        this.bankIFSCCode = bankIFSCCode;
        this.bankBranch = bankBranch;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccNo() {
        return bankAccNo;
    }

    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }

    public String getBankIFSCCode() {
        return bankIFSCCode;
    }

    public void setBankIFSCCode(String bankIFSCCode) {
        this.bankIFSCCode = bankIFSCCode;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }
}
