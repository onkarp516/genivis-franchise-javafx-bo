package com.opethic.genivis.dto.Reports;

import javafx.beans.property.SimpleStringProperty;

public class PurchaseRegisterDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty date;
    private SimpleStringProperty ledger;
    private SimpleStringProperty voucherType;
    private SimpleStringProperty voucherNo;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;

    public PurchaseRegisterDTO(String id, String date, String ledger,
                          String voucherType, String voucherNo, String debit, String credit) {
        this.id = new SimpleStringProperty(id);
        this.date = new SimpleStringProperty(date);
        this.ledger = new SimpleStringProperty(ledger);
        this.voucherType =new SimpleStringProperty( voucherType);
        this.voucherNo =new SimpleStringProperty( voucherNo);
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
        this.id = new SimpleStringProperty(id);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date = new SimpleStringProperty(date);
    }

    public String getLedger() {
        return ledger.get();
    }

    public SimpleStringProperty ledgerProperty() {
        return ledger;
    }

    public void setLedger(String ledger) {
        this.ledger = new SimpleStringProperty(ledger);
    }

    public String getVoucherType() {
        return voucherType.get();
    }

    public SimpleStringProperty voucherTypeProperty() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType =new SimpleStringProperty(voucherType);
    }

    public String getVoucherNo() {
        return voucherNo.get();
    }

    public SimpleStringProperty voucherNoProperty() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo =new SimpleStringProperty(voucherNo);
    }

    public String getDebit() {
        return debit.get();
    }

    public SimpleStringProperty debitProperty() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit =new SimpleStringProperty(debit);
    }

    public String getCredit() {
        return credit.get();
    }

    public SimpleStringProperty creditProperty() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit =new SimpleStringProperty(credit);
    }
}
