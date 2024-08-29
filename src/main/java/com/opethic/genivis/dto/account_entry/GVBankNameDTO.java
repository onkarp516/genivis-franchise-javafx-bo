package com.opethic.genivis.dto.account_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GVBankNameDTO implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("ledger_code")
    @Expose
    private String ledgerCode;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getLedgerCode() {
        return ledgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        this.ledgerCode = ledgerCode;
    }

    public GVBankNameDTO(Long id, String bankName, String accountNo, String ledgerCode) {
        this.id = id;
        this.bankName = bankName;
        this.accountNo = accountNo;
        this.ledgerCode = ledgerCode;
    }

    public GVBankNameDTO() {
    }
}
