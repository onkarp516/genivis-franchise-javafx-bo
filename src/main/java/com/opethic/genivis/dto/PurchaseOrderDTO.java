package com.opethic.genivis.dto;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.http.cookie.SM;

public class PurchaseOrderDTO {

    private StringProperty id;
    private StringProperty invoice_no;
    private StringProperty invoice_date;
    private StringProperty sundry_creditor_name;
    private StringProperty narration;
    private StringProperty tax_amt;
    private StringProperty taxable_amt;
    private StringProperty total_amount;
    private StringProperty purchase_order_status;
    private SimpleStringProperty print;
    private SimpleStringProperty action;
    private BooleanProperty is_row_selected;
    private StringProperty transactionTrackingNo;
    private StringProperty order_status;

    public String getTax_amt() {
        return tax_amt.get();
    }

    public String getPrint() {
        return print.get();
    }

    public SimpleStringProperty printProperty() {
        return print;
    }

    public void setPrint(String print) {
        this.print.set(print);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public PurchaseOrderDTO(String id, String invoice_no, String invoice_date, String sundry_creditor_name,
                            String narration, String taxable_amt, String tax_amt, String total_amount, String purchase_order_status,String order_status, String print, String action,String transactionTrackingNo) {
        this.id = new SimpleStringProperty(id);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.invoice_date =  new SimpleStringProperty(invoice_date);
        this.sundry_creditor_name =  new SimpleStringProperty(sundry_creditor_name);
        this.narration =  new SimpleStringProperty(narration);
        this.taxable_amt =  new SimpleStringProperty(taxable_amt);
        this.tax_amt =  new SimpleStringProperty(tax_amt);
        this.total_amount =  new SimpleStringProperty(total_amount);
        this.purchase_order_status =  new SimpleStringProperty(purchase_order_status);
        this.order_status =  new SimpleStringProperty(order_status);
        this.print = new SimpleStringProperty(print);
        this.action = new SimpleStringProperty(action);
        this.is_row_selected=new SimpleBooleanProperty(false);
        this.transactionTrackingNo = new SimpleStringProperty(transactionTrackingNo);
    }

    public boolean isIs_row_selected() {
        return is_row_selectedProperty().get();
    }

    public BooleanProperty is_row_selectedProperty() {
        if(is_row_selected==null)
            is_row_selected=new SimpleBooleanProperty(this,"is_row_selected");
        return is_row_selected;
    }

    public void setIs_row_selected(boolean is_row_selected) {
        is_row_selectedProperty().set(is_row_selected);
    }

    public String getId() {return idProperty().get();}

    public StringProperty idProperty() {
        if (id == null)
            id = new SimpleStringProperty(this, "id");
        return id;
    }

    public void setId(String id) { idProperty().set(id);}

    public String getInvoice_no() {
        return invoice_noProperty().get();
    }
    public String getInvoice_date() {
        return invoice_dateProperty().get();
    }
    public String getSundry_creditor_name() {
        return sundry_creditor_nameProperty().get();
    }
    public String getNarration() {
        return narrationProperty().get();
    }
    public String getTaxable_amt() {
        return taxable_amtProperty().get();
    }
    public String getTotal_amount() {
        return total_amountProperty().get();
    }
    public String getPurchase_order_status() {
        return purchase_order_statusProperty().get();
    }
    public String getTax() {
        return tax_amtProperty().get();
    }


    public StringProperty invoice_noProperty() {
        if(invoice_no==null)
            invoice_no=new SimpleStringProperty(this,"invoice_no");
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        invoice_noProperty().set(invoice_no);
    }


    public StringProperty invoice_dateProperty() {
        if(invoice_date==null)
            invoice_date=new SimpleStringProperty("invoice_date");
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        invoice_dateProperty().set(invoice_date);
    }



    public StringProperty sundry_creditor_nameProperty() {
        if(sundry_creditor_name==null)
            sundry_creditor_name=new SimpleStringProperty(this,"sundry_creditor_name");
        return sundry_creditor_name;
    }

    public void setSundry_creditor_name(String sundry_creditor_name) {
        sundry_creditor_nameProperty().set(sundry_creditor_name);
    }



    public StringProperty narrationProperty() {
        if(narration==null)
            narration=new SimpleStringProperty(this,"narration");
        return narration;
    }

    public void setNarration(String narration) {
        narrationProperty().set(narration);
    }



    public StringProperty taxable_amtProperty() {
        if(taxable_amt==null)
            taxable_amt=new SimpleStringProperty(this,"taxable_amt");
        return taxable_amt;
    }

    public void setTaxable_amt(String taxable_amt) {
        taxable_amtProperty().set(taxable_amt);
    }



    public StringProperty total_amountProperty() {
        if(total_amount==null)
            total_amount=new SimpleStringProperty(this,"total_amount");
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        total_amountProperty().set(total_amount);
    }


    public StringProperty purchase_order_statusProperty() {
        if(purchase_order_status==null)
            purchase_order_status=new SimpleStringProperty(this,"purchase_order_status");
        return purchase_order_status;
    }

    public void setPurchase_order_status(String purchase_order_status) {
        purchase_order_statusProperty().set(purchase_order_status);
    }


    public StringProperty tax_amtProperty() {
        if(tax_amt==null)
            tax_amt=new SimpleStringProperty(this,"tax_amt");
        return tax_amt;
    }

    public void setTax_amt(String tax) {
        tax_amtProperty().set(tax);
    }

    public String getTransactionTrackingNo() {
        return transactionTrackingNo.get();
    }

    public StringProperty transactionTrackingNoProperty() {
        return transactionTrackingNo;
    }

    public void setTransactionTrackingNo(String transactionTrackingNo) {
        this.transactionTrackingNo.set(transactionTrackingNo);
    }

    public String getOrder_status() {
        return order_status.get();
    }

    public StringProperty order_statusProperty() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status.set(order_status);
    }
}
