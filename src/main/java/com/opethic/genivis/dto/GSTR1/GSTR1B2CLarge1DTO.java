package com.opethic.genivis.dto.GSTR1;

import javafx.beans.property.SimpleStringProperty;

public class GSTR1B2CLarge1DTO {

    private SimpleStringProperty sr_no;
    private SimpleStringProperty state_name;
    private SimpleStringProperty state_id;
    private SimpleStringProperty state_code;
    private SimpleStringProperty rate_of_tax;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty igst_amt;
    private SimpleStringProperty sgst_amt;
    private SimpleStringProperty cgst_amt;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty invoice_amt;

    public GSTR1B2CLarge1DTO(String sr_no, String state_name, String state_id, String state_code, String rate_of_tax, String taxable_amt,
                             String igst_amt, String sgst_amt, String cgst_amt, String tax_amt, String invoice_amt) {
        this.sr_no = new SimpleStringProperty(sr_no);
        this.state_name = new SimpleStringProperty(state_name);
        this.state_id = new SimpleStringProperty(state_id);
        this.state_code = new SimpleStringProperty(state_code);
        this.rate_of_tax = new SimpleStringProperty(rate_of_tax);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.igst_amt = new SimpleStringProperty(igst_amt);
        this.sgst_amt = new SimpleStringProperty(sgst_amt);
        this.cgst_amt = new SimpleStringProperty(cgst_amt);
        this.tax_amt = new SimpleStringProperty(tax_amt);
        this.invoice_amt = new SimpleStringProperty(invoice_amt);
    }

    public String getSr_no() {return sr_no.get();}
    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }
    public void setSr_no(String sr_no) { this.sr_no=new SimpleStringProperty(sr_no);}


    public String getState_name() {return state_name.get();}

    public SimpleStringProperty state_nameProperty() {
        return state_name;
    }
    public void setState_name(String state_name) { this.state_name=new SimpleStringProperty(state_name);}

    public String getState_id() {return state_id.get();}
    public SimpleStringProperty state_idProperty() {
        return state_id;
    }
    public void setState_id(String state_id) { this.state_id=new SimpleStringProperty(state_id);}


    public String getState_code() {return state_code.get();}

    public SimpleStringProperty gst_numberProperty() {
        return state_code;
    }
    public void setGst_number(String state_code) { this.state_code=new SimpleStringProperty(state_code);}



    public String getRate_of_tax() {
        return rate_of_tax.get();
    }
    public SimpleStringProperty rate_of_taxProperty() {
        return rate_of_tax;
    }
    public void setRate_of_tax(String rate_of_tax) {
        this.rate_of_tax=new SimpleStringProperty(rate_of_tax);
    }


    public String getIgst_amt() {
        return igst_amt.get();
    }
    public SimpleStringProperty igst_amtProperty() {
        return igst_amt;
    }
    public void setIgst_amt(String igst_amt) {
        this.igst_amt=new SimpleStringProperty(igst_amt);
    }
    public String getCgst_amt() {
        return cgst_amt.get();
    }
    public SimpleStringProperty cgst_amtProperty() {
        return cgst_amt;
    }

    public void setCgst_amt(String cgst_amt) {
        this.cgst_amt=new SimpleStringProperty(cgst_amt);
    }

    public String getSgst_amt() {
        return sgst_amt.get();
    }
    public SimpleStringProperty sgst_amtProperty() {
        return sgst_amt;
    }
    public void setSgst_amt(String sgst_amt) {
        this.sgst_amt=new SimpleStringProperty(sgst_amt);
    }

    public String getTax_amt() {
        return tax_amt.get();
    }
    public SimpleStringProperty tax_amtProperty() {
        return tax_amt;
    }
    public void setTax_amt(String tax_amt) {
        this.tax_amt=new SimpleStringProperty(tax_amt);
    }


    public String getInvoice_amt() {
        return invoice_amt.get();
    }
    public SimpleStringProperty invoice_amtProperty() {
        return invoice_amt;
    }

    public void setInvoice_amt(String invoice_amt) {
        this.invoice_amt=new SimpleStringProperty(invoice_amt);
    }
    public String getTaxable_amt() {
        return taxable_amt.get();
    }
    public SimpleStringProperty taxable_amtProperty() {
        return taxable_amt;
    }
    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt=new SimpleStringProperty(taxable_amt);
    }


}
