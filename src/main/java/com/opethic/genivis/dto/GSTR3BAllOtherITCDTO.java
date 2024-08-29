package com.opethic.genivis.dto;
import javafx.beans.property.SimpleStringProperty;

public class GSTR3BAllOtherITCDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty dates;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty particulars;
    private SimpleStringProperty gstin_uin;
    private SimpleStringProperty voucher_type;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty igst_amt;
    private SimpleStringProperty cgst_amt;
    private SimpleStringProperty sgst_amt;
    private SimpleStringProperty cess_amt;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty invoice_amt;

    public GSTR3BAllOtherITCDTO(
            String id,
            String dates,
            String invoice_no,
            String particulars,
            String gstin_uin,
            String voucher_type,
            String taxable_amt,
            String igst_amt,
            String cgst_amt,
            String sgst_amt,
            String cess_amt,
            String tax_amt,
            String invoice_amt
    ){
        this.id = new SimpleStringProperty(id);
        this.dates = new SimpleStringProperty(dates);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.particulars = new SimpleStringProperty(particulars);
        this.gstin_uin = new SimpleStringProperty(gstin_uin);
        this.voucher_type = new SimpleStringProperty(voucher_type);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.igst_amt = new SimpleStringProperty(igst_amt);
        this.cgst_amt = new SimpleStringProperty(cgst_amt);
        this.sgst_amt = new SimpleStringProperty(sgst_amt);
        this.cess_amt = new SimpleStringProperty(cess_amt);
        this.tax_amt = new SimpleStringProperty(tax_amt);
        this.invoice_amt = new SimpleStringProperty(invoice_amt);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getDates() {return dates.get();}
    public SimpleStringProperty datesProperty() {return dates;}
    public void setDates(String dates) {this.dates = new SimpleStringProperty(dates);}

    public String getInvoice_no() {return invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return invoice_no;}
    public void setInvoice_no(String invoice_no) {this.invoice_no = new SimpleStringProperty(invoice_no);}

    public String getParticulars() {return particulars.get();}
    public SimpleStringProperty particularsProperty() {return particulars;}
    public void setParticulars(String particulars) {this.particulars = new SimpleStringProperty(particulars);}

    public String getGstin_uin() {return gstin_uin.get();}
    public SimpleStringProperty gstin_uinProperty() {return gstin_uin;}
    public void setGstin_uin(String gstin_uin) {this.gstin_uin = new SimpleStringProperty(gstin_uin);}

    public String getVoucher_type() {return voucher_type.get();}
    public SimpleStringProperty voucher_typeProperty() {return voucher_type;}
    public void setVoucher_type(String voucher_type) {this.voucher_type = new SimpleStringProperty(voucher_type);}

    public String getTaxable_amt() {return taxable_amt.get();}
    public SimpleStringProperty taxable_amtProperty() {return taxable_amt;}
    public void setTaxable_amt(String taxable_amt) {this.taxable_amt = new SimpleStringProperty(taxable_amt);}

    public String getIgst_amt() {return igst_amt.get();}
    public SimpleStringProperty igst_amtProperty() {return igst_amt;}
    public void setIgst_amt(String igst_amt) {this.igst_amt = new SimpleStringProperty(igst_amt);}

    public String getCgst_amt() {return cgst_amt.get();}
    public SimpleStringProperty cgst_amtProperty() {return cgst_amt;}
    public void setCgst_amt(String cgst_amt) {this.cgst_amt = new SimpleStringProperty(cgst_amt);}

    public String getSgst_amt() {return sgst_amt.get();}
    public SimpleStringProperty sgst_amtProperty() {return sgst_amt;}
    public void setSgst_amt(String sgst_amt) {this.sgst_amt = new SimpleStringProperty(sgst_amt);}

    public String getCess_amt() {return cess_amt.get();}
    public SimpleStringProperty cess_amtProperty() {return cess_amt;}
    public void setCess_amt(String cess_amt) {this.cess_amt = new SimpleStringProperty(cess_amt);}

    public String getTax_amt() {return tax_amt.get();}
    public SimpleStringProperty tax_amtProperty() {return tax_amt;}
    public void setTax_amt(String tax_amt) {this.tax_amt = new SimpleStringProperty(tax_amt);}

    public String getInvoice_amt() {return invoice_amt.get();}
    public SimpleStringProperty invoice_amtProperty() {return invoice_amt;}
    public void setInvoice_amt(String invoice_amt) {this.invoice_amt = new SimpleStringProperty(invoice_amt);}
}
