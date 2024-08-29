package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;
public class AccountsDayBookDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty date;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty voucher_type;
    private SimpleStringProperty voucher_no;
    private SimpleStringProperty debit_amt;
    private SimpleStringProperty credit_amt;

    public AccountsDayBookDTO(
            String id,
            String date,
            String ledger_name,
            String voucher_type,
            String voucher_no,
            String debit_amt,
            String credit_amt){
        this.id = new SimpleStringProperty(id);
        this.date =  new SimpleStringProperty(date);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.voucher_type = new SimpleStringProperty(voucher_type);
        this.voucher_no = new SimpleStringProperty(voucher_no);
        this.debit_amt = new SimpleStringProperty(debit_amt);
        this.credit_amt = new SimpleStringProperty(credit_amt);
    }
    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }

    public String getDate() {return date.get();}
    public SimpleStringProperty dateProperty(){return date;}
    public void setDate(String date){
        this.date = new SimpleStringProperty(date);
    }

    public String getLedger_name() {return ledger_name.get();}
    public SimpleStringProperty ledger_nameProperty(){return ledger_name;}
    public void setLedger_name(String ledger_name){
        this.ledger_name = new SimpleStringProperty(ledger_name);
    }

    public String getVoucher_type(){return voucher_type.get();}
    public SimpleStringProperty voucher_typeProperty(){return voucher_type;}
    public void setVoucher_type(String voucher_type){
        this.voucher_type = new SimpleStringProperty(voucher_type);
    }

    public String getVoucher_no(){return voucher_no.get();}
    public SimpleStringProperty voucher_noProperty(){return voucher_no;}
    public void setVoucher_no(String voucher_no){
        this.voucher_no = new SimpleStringProperty(voucher_no);
    }

    public String getDebit_amt(){return debit_amt.get();}
    public SimpleStringProperty debit_amtProperty(){return debit_amt;}
    public void setDebit_amt(String debit_amt){
        this.debit_amt = new SimpleStringProperty(debit_amt);
    }

    public String getCredit_amt(){return credit_amt.get();}
    public SimpleStringProperty credit_amtProperty(){return credit_amt;}
    public void setCredit_amt(String credit_amt){
         this.credit_amt = new SimpleStringProperty(credit_amt);
    }

}
