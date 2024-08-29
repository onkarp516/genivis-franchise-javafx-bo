package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class LedgerReport1DTO {
    private SimpleStringProperty id;
    private SimpleStringProperty principle;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty type;
    private SimpleStringProperty balancing_method;
    private SimpleStringProperty debit;
    private SimpleStringProperty unique_code;
    private SimpleStringProperty credit;


    public LedgerReport1DTO(
            String id,
            String principle,
            String ledger_name,
            String type,
            String balancing_method,
            String debit,
            String credit,
            String unique_code
            ){
        this.id = new SimpleStringProperty(id);
        this.principle = new SimpleStringProperty(principle);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.type = new SimpleStringProperty(type);
        this.balancing_method = new SimpleStringProperty(balancing_method);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
        this.unique_code = new SimpleStringProperty(unique_code);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getPrinciple() {return principle.get();}
    public SimpleStringProperty principleProperty() {return principle;}
    public void setPrinciple(String principle) {this.principle = new SimpleStringProperty(principle);}

    public String getLedger_name() {return ledger_name.get();}
    public SimpleStringProperty ledger_nameProperty() { return ledger_name;}
    public void setLedger_name(String ledger_name) {this.ledger_name = new SimpleStringProperty(ledger_name);}

    public String getType() {return type.get();}
    public SimpleStringProperty typeProperty() { return type;}
    public void setType(String type) {this.type = new SimpleStringProperty(type);}

    public String getBalancing_method() {return balancing_method.get();}
    public SimpleStringProperty balancing_methodProperty() { return balancing_method;}
    public void setBalancing_method(String balancing_method) {this.balancing_method = new SimpleStringProperty(balancing_method);}

    public String getDebit() {return debit.get();}

    public SimpleStringProperty debitProperty() { return debit;}
    public void setDebit(String debit) {this.debit = new SimpleStringProperty(debit);}

    public String getUniqueCode() {return unique_code.get();}
    public SimpleStringProperty uniqueCodeProperty() { return unique_code;}
    public void setUniqueCode(String unique_code) {this.unique_code = new SimpleStringProperty(unique_code);}



    public String getCredit() {return credit.get();}
    public SimpleStringProperty creditProperty() { return credit;}
    public void setCredit(String credit) {this.credit = new SimpleStringProperty(credit);}

}
