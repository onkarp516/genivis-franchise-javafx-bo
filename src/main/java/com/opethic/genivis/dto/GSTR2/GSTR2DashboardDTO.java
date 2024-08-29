package com.opethic.genivis.dto.GSTR2;

import javafx.beans.property.SimpleStringProperty;

public class GSTR2DashboardDTO {
    private SimpleStringProperty table_no;
    private SimpleStringProperty particulars;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty igst_amt;
    private SimpleStringProperty cgst_amt;
    private SimpleStringProperty sgst_amt;
    private SimpleStringProperty cess_amt;
    private SimpleStringProperty tax_amt;

    public GSTR2DashboardDTO(
            String table_no,
            String particulars,
            String taxable_amt,
            String igst_amt,
            String cgst_amt,
            String sgst_amt,
            String cess_amt,
            String tax_amt
    ){
        this.table_no = new SimpleStringProperty(table_no);
        this.particulars = new SimpleStringProperty(particulars);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.igst_amt = new SimpleStringProperty(igst_amt);
        this.cgst_amt = new SimpleStringProperty(cgst_amt);
        this.sgst_amt = new SimpleStringProperty(sgst_amt);
        this.cess_amt = new SimpleStringProperty(cess_amt);
        this.tax_amt = new SimpleStringProperty(tax_amt);
    }

    public String getTable_no() {return table_no.get();}
    public SimpleStringProperty table_noProperty() {return table_no;}
    public void setTable_no(String table_no) { this.table_no = new SimpleStringProperty(table_no);}

    public String getParticulars() {return particulars.get();}
    public SimpleStringProperty particularsProperty() {return particulars;}
    public void setParticulars(String particulars) {this.particulars = new SimpleStringProperty(particulars);}

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
}
