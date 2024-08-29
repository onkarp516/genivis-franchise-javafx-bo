package com.opethic.genivis.dto.pur_invoice;

import javafx.beans.property.SimpleStringProperty;

public class InvoiceProductHistoryTable {
    private SimpleStringProperty supplier_name;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty invoice_date;
    private SimpleStringProperty batch;
    private SimpleStringProperty mrp;
    private SimpleStringProperty qty;
    private SimpleStringProperty rate;
    private SimpleStringProperty cost;
    private SimpleStringProperty dis_per;
    private SimpleStringProperty dis_amt;

    public InvoiceProductHistoryTable(String supplier_name, String invoice_no, String invoice_date, String batch, String mrp, String qty, String rate, String cost, String dis_per, String dis_amt) {
        this.supplier_name = new SimpleStringProperty(supplier_name); ;
        this.invoice_no = new SimpleStringProperty(invoice_no) ;
        this.invoice_date = new SimpleStringProperty(invoice_date) ;
        this.batch = new SimpleStringProperty(batch) ;
        this.mrp = new SimpleStringProperty(mrp) ;
        this.qty = new SimpleStringProperty(qty) ;
        this.rate = new SimpleStringProperty(rate) ;
        this.cost = new SimpleStringProperty(cost) ;
        this.dis_per = new SimpleStringProperty(dis_per) ;
        this.dis_amt = new SimpleStringProperty(dis_amt) ;
    }

    public String getSupplier_name() {
        return supplier_name.get();
    }

    public SimpleStringProperty supplier_nameProperty() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name.set(supplier_name);
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

    public String getInvoice_date() {
        return invoice_date.get();
    }

    public SimpleStringProperty invoice_dateProperty() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date.set(invoice_date);
    }

    public String getBatch() {
        return batch.get();
    }

    public SimpleStringProperty batchProperty() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch.set(batch);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp);
    }

    public String getQty() {
        return qty.get();
    }

    public SimpleStringProperty qtyProperty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty.set(qty);
    }

    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public String getCost() {
        return cost.get();
    }

    public SimpleStringProperty costProperty() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost.set(cost);
    }

    public String getDis_per() {
        return dis_per.get();
    }

    public SimpleStringProperty dis_perProperty() {
        return dis_per;
    }

    public void setDis_per(String dis_per) {
        this.dis_per.set(dis_per);
    }

    public String getDis_amt() {
        return dis_amt.get();
    }

    public SimpleStringProperty dis_amtProperty() {
        return dis_amt;
    }

    public void setDis_amt(String dis_amt) {
        this.dis_amt.set(dis_amt);
    }
}
