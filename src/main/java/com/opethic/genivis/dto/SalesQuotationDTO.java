package com.opethic.genivis.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesQuotationDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty sales_quotation_no;
    private SimpleStringProperty sales_quotation_date;
//    private SimpleStringProperty invoice_date;
    private SimpleStringProperty sundry_debtors_name;
    private SimpleStringProperty narration;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty total_amount;
    private SimpleStringProperty sales_quotation_status;
    private SimpleBooleanProperty is_row_selected;
    private SimpleStringProperty ledgerId;
//    private SimpleStringProperty tcSalesOrderListTranxStatus;

    public SalesQuotationDTO(String id,String sales_quotation_no, String sales_quotation_date, String sundry_debtors_name,
                              String narration, String taxable_amt,String tax_amt, String total_amount, String sales_quotation_status,String ledgerId) {
        this.id = new SimpleStringProperty(id);
        this.sales_quotation_no = new SimpleStringProperty(sales_quotation_no);
        this.sales_quotation_date =  new SimpleStringProperty(sales_quotation_date);
        this.sundry_debtors_name =  new SimpleStringProperty(sundry_debtors_name);
        this.narration =  new SimpleStringProperty(narration);
        this.taxable_amt =  new SimpleStringProperty(taxable_amt);
        this.tax_amt =  new SimpleStringProperty(tax_amt);
        this.total_amount =  new SimpleStringProperty(total_amount);
        this.sales_quotation_status =  new SimpleStringProperty(sales_quotation_status);

//        this.tcSalesOrderListTranxStatus = new SimpleStringProperty(tcSalesOrderListTranxStatus);
        this.is_row_selected=new SimpleBooleanProperty(false);
        this.ledgerId = new SimpleStringProperty(ledgerId);
    }

    public String getId() {return id.get();}

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) { this.id=new SimpleStringProperty(id);}
    public String getSales_quotation_no() {
        return sales_quotation_no.get();
    }
    public String getSales_quotation_date() {
        return sales_quotation_date.get();
    }
    public String getSundry_debtors_name() {
        return sundry_debtors_name.get();
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
    public String getSales_quotation_status() {
        return sales_quotation_status.get();
    }
    public String getTax_amt() {
        return tax_amt.get();
    }

    public SimpleStringProperty sales_quotation_noProperty() {
        return sales_quotation_no;
    }

    public void setSales_quotation_no(String sales_quotation_no) {
        this.sales_quotation_no=new SimpleStringProperty(sales_quotation_no);
    }

    public SimpleStringProperty sales_quotation_dateProperty() {
        return sales_quotation_date;
    }

    public void setInvoice_date(String sales_quotation_date) {
        this.sales_quotation_date=new SimpleStringProperty(sales_quotation_date);
    }
    public SimpleStringProperty sundry_debtors_nameProperty() {
        return sundry_debtors_name;
    }

    public void setSundry_debtors_name(String sundry_debtors_name) {
        this.sundry_debtors_name=new SimpleStringProperty(sundry_debtors_name);
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

    public SimpleStringProperty sales_quotation_statusProperty() {
        return sales_quotation_status;
    }

    public void setSales_quotation_status(String sales_quotation_status) {
        this.sales_quotation_status= new SimpleStringProperty(sales_quotation_status);
    }
    public SimpleStringProperty tax_amtProperty() {
        return tax_amt;
    }

    public void setTax_amt(String tax) {
        this.tax_amt= new SimpleStringProperty(tax);
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


    public String getLedgerId() {
        return ledgerId.get();
    }

    public SimpleStringProperty ledgerIdProperty() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId.set(ledgerId);
    }
}
