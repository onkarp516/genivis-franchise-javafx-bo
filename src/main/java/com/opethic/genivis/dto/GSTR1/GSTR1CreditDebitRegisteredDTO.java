package com.opethic.genivis.dto.GSTR1;

import javafx.beans.property.SimpleStringProperty;

public class GSTR1CreditDebitRegisteredDTO {
    private SimpleStringProperty table_no;
    private SimpleStringProperty id;
    private SimpleStringProperty dates;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty gstin_uin;
    private SimpleStringProperty voucher_type;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty igst_amt;
    private SimpleStringProperty cgst_amt;
    private SimpleStringProperty sgst_amt;
    private SimpleStringProperty cess_amt;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty inv_amt;

    public GSTR1CreditDebitRegisteredDTO(
            String table_no,
            String id,
            String dates,
            String invoice_no,
            String ledger_name,
            String gstin_uin,
            String voucher_type,
            String taxable_amt,
            String igst_amt,
            String cgst_amt,
            String sgst_amt,
            String cess_amt,
            String tax_amt,
            String inv_amt
    ){
        this.table_no = new SimpleStringProperty(table_no);
        this.id = new SimpleStringProperty(id);
        this.dates = new SimpleStringProperty(dates);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.gstin_uin = new SimpleStringProperty(gstin_uin);
        this.voucher_type = new SimpleStringProperty(voucher_type);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.igst_amt = new SimpleStringProperty(igst_amt);
        this.cgst_amt = new SimpleStringProperty(cgst_amt);
        this.sgst_amt = new SimpleStringProperty(sgst_amt);
        this.cess_amt = new SimpleStringProperty(cess_amt);
        this.tax_amt = new SimpleStringProperty(tax_amt);
        this.inv_amt = new SimpleStringProperty(inv_amt);
    }

    public String getTable_no() {return table_no.get();}
    public SimpleStringProperty table_noProperty() {return table_no;}
    public void setTable_no(String table_no) { this.table_no = new SimpleStringProperty(table_no);}
    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}


    public String getDates() {return dates.get();}
    public SimpleStringProperty datesProperty() {return dates;}
    public void setDates(String dates) { this.dates = new SimpleStringProperty(dates);}
    public String getInvoiceNo() {return invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return invoice_no;}
    public void setInvoiceNo(String invoice_no) { this.invoice_no = new SimpleStringProperty(invoice_no);}

    public String getLedger_name() {return ledger_name.get();}
    public SimpleStringProperty ledger_nameProperty() {return ledger_name;}
    public void setLedger_name(String ledger_name) {this.ledger_name = new SimpleStringProperty(ledger_name);}

    public String getGSTIN_UIN() {return gstin_uin.get();}
    public SimpleStringProperty gstin_uinProperty() {return gstin_uin;}
    public void setGSTIN_UIN(String gstin_uin) {this.gstin_uin = new SimpleStringProperty(gstin_uin);}
    public String getVoucherType() {return voucher_type.get();}
    public SimpleStringProperty voucher_typeProperty() {return voucher_type;}
    public String SetVoucherType() {return voucher_type.get();}

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
    public String getInv_amt() {return inv_amt.get();}
    public SimpleStringProperty inv_amtProperty() {return inv_amt;}
    public void setInv_amt(String inv_amt) {this.inv_amt = new SimpleStringProperty(inv_amt);}
}
