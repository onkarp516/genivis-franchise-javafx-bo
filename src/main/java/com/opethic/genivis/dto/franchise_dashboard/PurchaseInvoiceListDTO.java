package com.opethic.genivis.dto.franchise_dashboard;

import javafx.beans.property.SimpleStringProperty;

public class PurchaseInvoiceListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty SrNo;
    private SimpleStringProperty ledgerName;
    private SimpleStringProperty amount;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty sundry_debtor_name;
    private SimpleStringProperty sundry_debtor_id;
    private SimpleStringProperty sale_account_name;
    private SimpleStringProperty baseamt;
    private SimpleStringProperty tax;

    public PurchaseInvoiceListDTO(String id, String srNo, String ledgerName, String amount, String invoice_no, String sundry_debtor_name, String sundry_debtor_id, String sale_account_name, String baseamt, String tax) {
        this.id = new SimpleStringProperty(id);
        SrNo = new SimpleStringProperty(srNo);
        this.ledgerName = new SimpleStringProperty(ledgerName);
        this.amount = new SimpleStringProperty(amount);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.sundry_debtor_name = new SimpleStringProperty(sundry_debtor_name);
        this.sundry_debtor_id = new SimpleStringProperty(sundry_debtor_id);
        this.sale_account_name = new SimpleStringProperty(sale_account_name);
        this.baseamt = new SimpleStringProperty(baseamt);
        this.tax = new SimpleStringProperty(tax);
    }

    // Getters and Property methods...

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getSrNo() {
        return SrNo.get();
    }

    public SimpleStringProperty srNoProperty() {
        return SrNo;
    }

    public void setSrNo(String srNo) {
        this.SrNo.set(srNo);
    }

    public String getLedgerName() {
        return ledgerName.get();
    }

    public SimpleStringProperty ledgerNameProperty() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName.set(ledgerName);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getInvoice_no() {
        return invoice_no.get();
    }

    public SimpleStringProperty invoice_noProperty() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no.set(invoice_no);
    }

    public String getSundry_debtor_name() {
        return sundry_debtor_name.get();
    }

    public SimpleStringProperty sundry_debtor_nameProperty() {
        return sundry_debtor_name;
    }

    public void setSundry_debtor_name(String sundry_debtor_name) {
        this.sundry_debtor_name.set(sundry_debtor_name);
    }

    public String getSundry_debtor_id() {
        return sundry_debtor_id.get();
    }

    public SimpleStringProperty sundry_debtor_idProperty() {
        return sundry_debtor_id;
    }

    public void setSundry_debtor_id(String sundry_debtor_id) {
        this.sundry_debtor_id.set(sundry_debtor_id);
    }

    public String getSale_account_name() {
        return sale_account_name.get();
    }

    public SimpleStringProperty sale_account_nameProperty() {
        return sale_account_name;
    }

    public void setSale_account_name(String sale_account_name) {
        this.sale_account_name.set(sale_account_name);
    }

    public String getBaseamt() {
        return baseamt.get();
    }

    public SimpleStringProperty baseamtProperty() {
        return baseamt;
    }

    public void setBaseamt(String baseamt) {
        this.baseamt.set(baseamt);
    }

    public String getTax() {
        return tax.get();
    }

    public SimpleStringProperty taxProperty() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax.set(tax);
    }
}
