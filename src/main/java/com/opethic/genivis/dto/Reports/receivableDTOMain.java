package com.opethic.genivis.dto.Reports;

import javafx.beans.property.SimpleStringProperty;

public class receivableDTOMain {
    private SimpleStringProperty id;   //now no need
    private SimpleStringProperty Ledger_name;
    private SimpleStringProperty Invoice_no;     //invoice no
    private SimpleStringProperty invoiceDate;
    private SimpleStringProperty total_amount;     //invoice amount
    private SimpleStringProperty paid_amt;
    private SimpleStringProperty balance;
    private SimpleStringProperty balancing_type;
    private SimpleStringProperty Due_date;
    private SimpleStringProperty overDueDays;
    private SimpleStringProperty CreditorsId;
    private SimpleStringProperty remaining_amt;
    private SimpleStringProperty balancing_method;

    public receivableDTOMain(
            String ledgName,
            String billNo,
            String billDate,
            String invAmt,
            String paidAmt,
            String balAmt,
            String ledgType,
            String dueDate,
            String overDueDays,
            String creditorId,
            String remAmt){
        this.Ledger_name = new SimpleStringProperty(ledgName);
        this.Invoice_no = new SimpleStringProperty(billNo);
        this.invoiceDate = new SimpleStringProperty(billDate);
        this.total_amount = new SimpleStringProperty(invAmt);
        this.paid_amt = new SimpleStringProperty(paidAmt);
        this.balance = new SimpleStringProperty(balAmt);
        this.balancing_type = new SimpleStringProperty(ledgType);
        this.Due_date = new SimpleStringProperty(dueDate);
        this.overDueDays = new SimpleStringProperty(overDueDays);
        this.CreditorsId = new SimpleStringProperty(creditorId);
        this.remaining_amt = new SimpleStringProperty(remAmt);

    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) {this.id = new SimpleStringProperty(id);}

    public String getLedger_name() {return Ledger_name.get();}
    public SimpleStringProperty Ledger_nameProperty() {return Ledger_name;}
    public void setLedger_name(String ledger_name) {this.Ledger_name = new SimpleStringProperty(ledger_name);}

    public String getInvoice_no() {return Invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return Invoice_no;}
    public void setInvoice_no(String invoice_no) {this.Invoice_no.set(invoice_no);}


    public String getInvoiceDate() {return invoiceDate.get();}
    public SimpleStringProperty invoiceDateProperty() {
        return invoiceDate;
    }
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = new SimpleStringProperty(invoiceDate);
    }

    public String getTotal_amount() {
        return total_amount.get();
    }
    public SimpleStringProperty total_amountProperty() {
        return total_amount;
    }
    public void setTotal_amount(String total_amount) {
        this.total_amount = new SimpleStringProperty(total_amount);
    }

    public String getPaid_amt() {
        return paid_amt.get();
    }
    public SimpleStringProperty paid_amtProperty() {
        return paid_amt;
    }
    public void setPaid_amt(String paid_amt) {
        this.paid_amt = new SimpleStringProperty(paid_amt);
    }

    public String getBalance() {
        return balance.get();
    }
    public SimpleStringProperty balanceProperty() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = new SimpleStringProperty(balance);
    }

    public String getBalancing_type() {
        return balancing_type.get();
    }
    public SimpleStringProperty balancing_typeProperty() {
        return balancing_type;
    }
    public void setBalancing_type(String balancing_type) {
        this.balancing_type = new SimpleStringProperty(balancing_type);
    }

    public String getDue_date() {
        return Due_date.get();
    }
    public SimpleStringProperty due_dateProperty() {
        return Due_date;
    }
    public void setDue_date(String due_date) {
        this.Due_date = new SimpleStringProperty(due_date);
    }

    public String getOverDueDays() {
        return overDueDays.get();
    }
    public SimpleStringProperty overDueDaysProperty() {
        return overDueDays;
    }
    public void setOverDueDays(String overDueDays) {
        this.overDueDays = new SimpleStringProperty(overDueDays);
    }

    public String getCreditorsId() {
        return CreditorsId.get();
    }
    public SimpleStringProperty creditorsIdProperty() {
        return CreditorsId;
    }
    public void setCreditorsId(String creditorsId) {
        this.CreditorsId = new SimpleStringProperty(creditorsId);
    }

    public String getBalancing_method() {
        return balancing_method.get();
    }
    public SimpleStringProperty balancing_methodProperty() {
        return balancing_method;
    }
    public void setBalancing_method(String balancing_method) {
        this.balancing_method = new SimpleStringProperty(balancing_method);
    }

    public String getRemaining_amt() {
        return remaining_amt.get();
    }
    public SimpleStringProperty remaining_amtProperty() {
        return remaining_amt;
    }
    public void setRemaining_amt(String remaining_amt) {
        this.remaining_amt = new SimpleStringProperty(remaining_amt);
    }
}

