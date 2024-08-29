package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class LedgerReport2DTO {
    private SimpleStringProperty id;
    private SimpleStringProperty date;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty voucher_type;
    private SimpleStringProperty voucher_no;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;
    private SimpleStringProperty closing_balance;
    private SimpleStringProperty type;

    public LedgerReport2DTO(
            String id,
            String date,
            String ledger_name,
            String voucher_type,
            String voucher_no,
            String debit,
            String credit,
            String closing_balance,
            String type){
        this.id = new SimpleStringProperty(id);
        this.date = new SimpleStringProperty(date);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.voucher_type = new SimpleStringProperty(voucher_type);
        this.voucher_no = new SimpleStringProperty(voucher_no);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
        this.closing_balance = new SimpleStringProperty(closing_balance);
        this.type = new SimpleStringProperty(type);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id){this.id = new SimpleStringProperty(id);}

    public String getDate() {return date.get();}
    public SimpleStringProperty dateProperty() {return date;}
    public void setDate(String date) {this.date = new SimpleStringProperty(date);}

    public String getLedger_name() {return ledger_name.get();}
    public SimpleStringProperty ledger_nameProperty() {return ledger_name;}
    public void setLedger_name (String ledger_name) {this.ledger_name = new SimpleStringProperty(ledger_name);}

    public String getVoucher_type() {return voucher_type.get();}
    public SimpleStringProperty voucher_typeProperty() {return voucher_type;}
    public void setVoucher_type (String voucher_type) {this.voucher_type = new SimpleStringProperty(voucher_type);}

    public String getVoucher_no() {return voucher_no.get();}
    public SimpleStringProperty voucher_noProperty() {return voucher_no;}
    public void setVoucher_no (String voucher_no) {this.voucher_no = new SimpleStringProperty(voucher_no);}

    public String getDebit() {return debit.get();}
    public SimpleStringProperty debitProperty() {return debit;}
    public void setDebit (String debit) {this.debit = new SimpleStringProperty(debit);}

    public String getCredit() {return credit.get();}
    public SimpleStringProperty creditProperty() {return credit;}
    public void setCredit (String credit) {this.credit = new SimpleStringProperty(credit);}

    public String getClosing_balance() {return closing_balance.get();}
    public SimpleStringProperty closing_balanceProperty() {return closing_balance;}
    public void setClosing_balance (String closing_balance) {this.closing_balance = new SimpleStringProperty(closing_balance);}

    public String getType() {return type.get();}
    public SimpleStringProperty typeProperty() {return type;}
    public void setType (String type) {this.type = new SimpleStringProperty(type);}
}