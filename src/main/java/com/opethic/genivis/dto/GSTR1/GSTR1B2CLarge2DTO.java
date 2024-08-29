package com.opethic.genivis.dto.GSTR1;

import javafx.beans.property.SimpleStringProperty;

public class GSTR1B2CLarge2DTO {

    private SimpleStringProperty sr_no;
    private SimpleStringProperty id;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty invoice_date;
    private SimpleStringProperty sale_serial_number;
    private SimpleStringProperty total_amount;
    private SimpleStringProperty sundry_debtor_name;
    private SimpleStringProperty sundry_debtor_id;
    private SimpleStringProperty sale_account_name;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty narration;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty totaligst;
    private SimpleStringProperty totalcgst;
    private SimpleStringProperty totalsgst;

    public GSTR1B2CLarge2DTO(String sr_no, String id, String invoice_no, String invoice_date, String sale_serial_number, String total_amount,
                             String sundry_debtor_name, String sundry_debtor_id, String sale_account_name, String tax_amt,
                             String narration, String taxable_amt, String totaligst, String totalcgst, String totalsgst) {
        this.sr_no = new SimpleStringProperty(sr_no);
        this.id = new SimpleStringProperty(id);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.invoice_date = new SimpleStringProperty(invoice_date);
        this.sale_serial_number = new SimpleStringProperty(sale_serial_number);
        this.total_amount = new SimpleStringProperty(total_amount);
        this.sundry_debtor_name = new SimpleStringProperty(sundry_debtor_name);
        this.sundry_debtor_id = new SimpleStringProperty(sundry_debtor_id);
        this.sale_account_name = new SimpleStringProperty(sale_account_name);
        this.tax_amt = new SimpleStringProperty(tax_amt);
        this.narration = new SimpleStringProperty(narration);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.totaligst = new SimpleStringProperty(totaligst);
        this.totalcgst = new SimpleStringProperty(totalcgst);
        this.totalsgst = new SimpleStringProperty(totalsgst);
    }

    public String getId() {return id.get();}

    public String getSr_no() {
        return sr_no.get();
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no.set(sr_no);
    }

    public SimpleStringProperty idProperty() {
        return id;
    }
    public void setId(String id) { this.id=new SimpleStringProperty(id);}


    public String getInvoice_no() {return invoice_no.get();}

    public SimpleStringProperty invoice_noProperty() {
        return invoice_no;
    }
    public void setInvoice_no(String invoice_no) { this.invoice_no=new SimpleStringProperty(invoice_no);}


    public String getInvoice_date() {return invoice_date.get();}

    public SimpleStringProperty invoice_dateProperty() {
        return invoice_date;
    }
    public void setInvoice_date(String invoice_date) { this.invoice_date=new SimpleStringProperty(invoice_date);}

    public String getSale_serial_number() {
        return sale_serial_number.get();
    }
    public SimpleStringProperty sale_serial_numberProperty() {
        return sale_serial_number;
    }
    public void setSale_serial_number(String sale_serial_number) {
        this.sale_serial_number=new SimpleStringProperty(sale_serial_number);
    }
    public String getTotal_amount() {
        return total_amount.get();
    }
    public SimpleStringProperty total_amountProperty() {
        return total_amount;
    }
    public void setTotal_amount(String total_amount) {
        this.total_amount=new SimpleStringProperty(total_amount);
    }

    public String getSundry_debtor_name() {
        return sundry_debtor_name.get();
    }
    public SimpleStringProperty sundry_debtor_nameProperty() {
        return sundry_debtor_name;
    }

    public void setSundry_debtor_name(String sundry_debtor_name) {
        this.sundry_debtor_name=new SimpleStringProperty(sundry_debtor_name);
    }

    public String getSundry_debtor_id() {return sundry_debtor_id.get();}
    public SimpleStringProperty sundry_debtor_idProperty() {
        return sundry_debtor_id;
    }
    public void setSundry_debtor_id(String sundry_debtor_id) { this.sundry_debtor_id=new SimpleStringProperty(sundry_debtor_id);}

    public String getSale_account_name() {return sale_account_name.get();}
    public SimpleStringProperty sale_account_nameProperty() {
        return sale_account_name;
    }    public void setSale_account_name(String sale_account_name) { this.sale_account_name=new SimpleStringProperty(sale_account_name);}


    public String getNarration() {return narration.get();}
    public SimpleStringProperty narrationProperty() {
        return narration;
    }
    public void setNarration(String narration) { this.narration=new SimpleStringProperty(narration);}

    public String getTaxable_amt() {
        return taxable_amt.get();
    }
    public SimpleStringProperty taxable_amtProperty() {
        return taxable_amt;
    }
    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt=new SimpleStringProperty(taxable_amt);
    }

    public String getTax_amt() {return tax_amt.get();}
    public SimpleStringProperty tax_amtProperty() {
        return tax_amt;
    }
    public void setTax_amt(String tax_amt) { this.tax_amt=new SimpleStringProperty(tax_amt);}

    public String getTotalcgst() {return totalcgst.get();}
    public SimpleStringProperty totalcgstProperty() {
        return totalcgst;
    }
    public void setTotalcgst(String totalcgst) { this.totalsgst=new SimpleStringProperty(totalcgst);}


    public String getTotalsgst() {return totalsgst.get();}
    public SimpleStringProperty totalsgstProperty() {
        return totalsgst;
    }
    public void setTotalsgst(String totalsgst) { this.totalsgst=new SimpleStringProperty(totalsgst);}

    public String getTotaligst() {return totaligst.get();}
    public SimpleStringProperty totaligstProperty() {
        return totaligst;
    }
    public void setTotaligst(String totaligst) { this.totaligst=new SimpleStringProperty(totaligst);}

}
