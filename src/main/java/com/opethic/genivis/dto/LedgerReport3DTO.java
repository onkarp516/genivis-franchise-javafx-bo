package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class LedgerReport3DTO {
    private SimpleStringProperty particulars;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;
    private SimpleStringProperty closing_balance;
    private SimpleStringProperty type;

    public LedgerReport3DTO(
            String particulars,
            String debit,
            String credit,
            String closing_balance,
            String type){
        this.particulars = new SimpleStringProperty(particulars);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
        this.closing_balance = new SimpleStringProperty(closing_balance);
        this.type = new SimpleStringProperty(type);
    }

    public String getParticulars() {return particulars.get();}
    public SimpleStringProperty particularsProperty() {return particulars;}
    public void setParticulars(String particulars) {this.particulars = new SimpleStringProperty(particulars);}

    public String getDebit() {return debit.get();}
    public SimpleStringProperty debitProperty() {return debit;}
    public void setDebit(String debit) {this.debit = new SimpleStringProperty(debit);}

    public String getCredit() {return credit.get();}
    public SimpleStringProperty creditProperty() {return credit;}
    public void setCredit(String credit) {this.credit = new SimpleStringProperty(credit);}

    public String getClosing_balance() {return closing_balance.get();}
    public SimpleStringProperty closing_balanceProperty() {return closing_balance;}
    public void setClosing_balance(String closing_balance) {this.closing_balance = new SimpleStringProperty(closing_balance);}

    public String getType() {return type.get();}
    public SimpleStringProperty typeProperty() {return type;}
    public void setType(String type) {this.type = new SimpleStringProperty(type);}
}
