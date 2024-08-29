package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MasterLedgerReportDTO {
    private SimpleStringProperty particulars;
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty type;
    private SimpleStringProperty tran_date;
    private SimpleIntegerProperty ledgerId;

    public MasterLedgerReportDTO(
            int ledgerId,
            String tran_date,
            String particulars,
            String debit,
            String credit,
            String invoice_no,
            String type){
        this.ledgerId = new SimpleIntegerProperty(ledgerId);
        this.tran_date = new SimpleStringProperty(tran_date);
        this.particulars = new SimpleStringProperty(particulars);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.type = new SimpleStringProperty(type);
    }

    public Integer getId() {return ledgerId.get();}
    public String getParticulars() {return particulars.get();}
    public SimpleStringProperty particularsProperty() {return particulars;}
    public void setParticulars(String particulars) {this.particulars = new SimpleStringProperty(particulars);}

    public String getDebit() {return debit.get();}
    public SimpleStringProperty debitProperty() {return debit;}
    public void setDebit(String debit) {this.debit = new SimpleStringProperty(debit);}

    public String getCredit() {return credit.get();}
    public SimpleStringProperty creditProperty() {return credit;}
    public void setCredit(String credit) {this.credit = new SimpleStringProperty(credit);}

    public String getInvoiceNo() {return invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return invoice_no;}
    public void setClosing_balance(String invoice_no) {this.invoice_no = new SimpleStringProperty(invoice_no);}

    public String getType() {return type.get();}
    public SimpleStringProperty typeProperty() {return type;}
    public void setType(String type) {this.type = new SimpleStringProperty(type);}

    public String getTranDate() {return tran_date.get();}
    public SimpleStringProperty tran_dateProperty() {return tran_date;}
    public void setTranDate(String tran_date) {this.tran_date = new SimpleStringProperty(tran_date);}
}
