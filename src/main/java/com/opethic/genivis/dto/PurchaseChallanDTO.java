package com.opethic.genivis.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class PurchaseChallanDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty referenceNo;
    private SimpleStringProperty invoice_date;
    private SimpleStringProperty sundry_creditor_name;
    private SimpleStringProperty narration;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty total_amount;
    private SimpleStringProperty purchase_challan_status;
    private SimpleBooleanProperty is_row_selected;

    public PurchaseChallanDTO(String id,String invoice_no,String referenceNo, String invoice_date, String sundry_creditor_name,
                            String narration, String taxable_amt,String tax_amt, String total_amount, String purchase_challan_status) {
        this.id = new SimpleStringProperty(id);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.referenceNo = new SimpleStringProperty(referenceNo);
        this.invoice_date =  new SimpleStringProperty(invoice_date);
        this.sundry_creditor_name =  new SimpleStringProperty(sundry_creditor_name);
        this.narration =  new SimpleStringProperty(narration);
        this.taxable_amt =  new SimpleStringProperty(taxable_amt);
        this.tax_amt =  new SimpleStringProperty(tax_amt);
        this.total_amount =  new SimpleStringProperty(total_amount);
        this.purchase_challan_status =  new SimpleStringProperty(purchase_challan_status);
        this.is_row_selected = new SimpleBooleanProperty(false);
    }

    public boolean isIs_row_selected() {
        return is_row_selected.get();
    }

    public SimpleBooleanProperty is_row_selectedProperty() {
        return is_row_selected;
    }

    public void setIs_row_selected(boolean is_row_selected) {
        this.is_row_selected.set(is_row_selected);
    }

    public String getId() {return id.get();}

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) { this.id=new SimpleStringProperty(id);}

    public String getInvoice_no() {
        return invoice_no.get();
    }
    public String getReferenceNo() {
        return referenceNo.get();
    }
    public String getInvoice_date() {
        return invoice_date.get();
    }
    public String getSundry_creditor_name() {
        return sundry_creditor_name.get();
    }
    public String getNarration() {
        return narration.get();
    }
    public String getTaxable_amt() {
        return taxable_amt.get();
    }
    public String getTotal_amount() {
        return total_amount.get();
    }
    public String getPurchase_challan_status() {
        return purchase_challan_status.get();
    }
    public String getTax_amt() {
        return tax_amt.get();
    }


    public SimpleStringProperty invoice_noProperty() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no=new SimpleStringProperty(invoice_no);
    }

    public SimpleStringProperty referenceNoProperty() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = new SimpleStringProperty(referenceNo);
    }

    public SimpleStringProperty invoice_dateProperty() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date=new SimpleStringProperty(invoice_date);
    }



    public SimpleStringProperty sundry_creditor_nameProperty() {
        return sundry_creditor_name;
    }

    public void setSundry_creditor_name(String sundry_creditor_name) {
        this.sundry_creditor_name=new SimpleStringProperty(sundry_creditor_name);
    }



    public SimpleStringProperty narrationProperty() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration=new SimpleStringProperty(narration);
    }



    public SimpleStringProperty taxable_amtProperty() {
        return taxable_amt;
    }

    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt=new SimpleStringProperty(taxable_amt);
    }



    public SimpleStringProperty total_amountProperty() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount=new SimpleStringProperty(total_amount);
    }


    public SimpleStringProperty purchase_challan_statusProperty() {
        return purchase_challan_status;
    }

    public void setPurchase_challan_status(String purchase_order_status) {
        this.purchase_challan_status= new SimpleStringProperty(purchase_order_status);
    }


    public SimpleStringProperty tax_amtProperty() {
        return tax_amt;
    }

    public void setTax_amt(String tax) {
        this.tax_amt= new SimpleStringProperty(tax);
    }

}
