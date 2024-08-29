package com.opethic.genivis.dto.utilities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class DispatchMngtDeliveredSalesInvoiceDTO {
    private SimpleStringProperty order_id;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty invoice_date;
    private SimpleStringProperty customer_name;
    private SimpleStringProperty narration;
    private SimpleStringProperty order_status;
    private SimpleStringProperty bill_amount;
    private SimpleStringProperty item_count;
    private SimpleBooleanProperty is_row_selected;

    public DispatchMngtDeliveredSalesInvoiceDTO(
            String order_id,
            String invoice_no,
            String invoice_date,
            String customer_name,
            String narration,
            String order_status,
            String bill_amount,
            String item_count){
        this.order_id = new SimpleStringProperty(order_id);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.invoice_date = new SimpleStringProperty(invoice_date);
        this.customer_name = new SimpleStringProperty(customer_name);
        this.narration = new SimpleStringProperty(narration);
        this.order_status = new SimpleStringProperty(order_status);
        this.bill_amount = new SimpleStringProperty(bill_amount);
        this.item_count = new SimpleStringProperty(item_count);
        this.is_row_selected=new SimpleBooleanProperty(false);
    }

    public String getOrder_id() {return order_id.get();}
    public SimpleStringProperty order_idProperty() {return order_id;}
    public void setOrder_id(String order_id) { this.order_id = new SimpleStringProperty(order_id);}

    public String getInvoice_no() {return invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return invoice_no;}
    public void setInvoice_no(String invoice_no) { this.invoice_no = new SimpleStringProperty(invoice_no);}

    public String getInvoice_date() {return invoice_date.get();}
    public SimpleStringProperty invoice_dateProperty() {return invoice_date;}
    public void setInvoice_date(String invoice_date) { this.invoice_date = new SimpleStringProperty(invoice_date);}

    public String getCustomer_name() {return customer_name.get();}
    public SimpleStringProperty customer_nameProperty() {return customer_name;}
    public void setCustomer_name(String customer_name) { this.customer_name = new SimpleStringProperty(customer_name);}

    public String getNarration() {return narration.get();}
    public SimpleStringProperty narrationProperty() {return narration;}
    public void setNarration(String narration) { this.narration = new SimpleStringProperty(narration);}

    public String getOrder_status() {return order_status.get();}
    public SimpleStringProperty order_statusProperty() {return order_status;}
    public void setOrder_status(String order_status) { this.order_status = new SimpleStringProperty(order_status);}

    public String getBill_amount() {return bill_amount.get();}
    public SimpleStringProperty bill_amountProperty() {return bill_amount;}
    public void setBill_amount(String bill_amount) { this.bill_amount = new SimpleStringProperty(bill_amount);}

    public String getItem_count() {return item_count.get();}
    public SimpleStringProperty item_countProperty() {return item_count;}
    public void setItem_count(String item_count) { this.item_count = new SimpleStringProperty(item_count);}

    public boolean isIs_row_selected() {
        return is_row_selected.get();
    }
    public SimpleBooleanProperty is_row_selectedProperty() {
        return is_row_selected;
    }
    public void setIs_row_selected(boolean is_row_selected) {
        this.is_row_selected.set(is_row_selected);
    }
}
