package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class StockReport2DTO {
    private SimpleStringProperty id;
    private SimpleStringProperty date;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty particulars_name;
    private SimpleStringProperty voucher_type;
    private SimpleStringProperty purchase_qty;
    private SimpleStringProperty purchase_unit;
    private SimpleStringProperty purchase_value;
    private SimpleStringProperty sales_qty;
    private SimpleStringProperty sales_unit;
    private SimpleStringProperty sales_value;
    private SimpleStringProperty closing_balance_qty;
    private SimpleStringProperty closing_balance_unit;
    private SimpleStringProperty closing_balance_value;

    public StockReport2DTO(
            String id,
            String date,
            String invoice_no,
            String particulars_name,
            String voucher_type,
            String purchase_qty,
            String purchase_unit,
            String purchase_value,
            String sales_qty,
            String sales_unit,
            String sales_value,
            String closing_balance_qty,
            String closing_balance_unit,
            String closing_balance_value){
        this.id = new SimpleStringProperty(id);
        this.date = new SimpleStringProperty(date);
        this.invoice_no = new SimpleStringProperty(invoice_no);
        this.particulars_name = new SimpleStringProperty(particulars_name);
        this.voucher_type = new SimpleStringProperty(voucher_type);
        this.purchase_qty = new SimpleStringProperty(purchase_qty);
        this.purchase_unit = new SimpleStringProperty(purchase_unit);
        this.purchase_value = new SimpleStringProperty(purchase_value);
        this.sales_qty = new SimpleStringProperty(sales_qty);
        this.sales_unit = new SimpleStringProperty(sales_unit);
        this.sales_value = new SimpleStringProperty(sales_value);
        this.closing_balance_qty = new SimpleStringProperty(closing_balance_qty);
        this.closing_balance_unit = new SimpleStringProperty(closing_balance_unit);
        this.closing_balance_value = new SimpleStringProperty(closing_balance_value);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getDate() {return date.get();}
    public SimpleStringProperty dateProperty() {return date;}
    public void setDate(String date) {this.date = new SimpleStringProperty(date);}

    public String getInvoice_no() {return invoice_no.get();}
    public SimpleStringProperty invoice_noProperty() {return invoice_no;}
    public void setInvoice_no(String invoice_no) {this.invoice_no = new SimpleStringProperty(invoice_no);}

    public String getParticulars_name() {return particulars_name.get();}
    public SimpleStringProperty particulars_nameProperty() {return particulars_name;}
    public void setParticulars_name(String particulars_name) {this.particulars_name = new SimpleStringProperty(particulars_name);}

    public String getVoucher_type() {return voucher_type.get();}
    public SimpleStringProperty voucher_type() {return voucher_type;}
    public void setVoucher_type(String voucher_type) {this.voucher_type = new SimpleStringProperty(voucher_type);}

    public String getPurchase_qty() {return purchase_qty.get();}
    public SimpleStringProperty purchase_qtyProperty() {return purchase_qty;}
    public void setPurchase_qty(String purchase_qty) {this.purchase_qty = new SimpleStringProperty(purchase_qty);}

    public String getPurchase_unit() {return purchase_unit.get();}
    public SimpleStringProperty purchase_unitProperty() {return purchase_unit;}
    public void setPurchase_unit(String purchase_unit) {this.purchase_unit = new SimpleStringProperty(purchase_unit);}

    public String getPurchase_value() {return purchase_value.get();}
    public SimpleStringProperty purchase_valueProperty() {return purchase_value;}
    public void setPurchase_value(String purchase_value) {this.purchase_value = new SimpleStringProperty(purchase_value);}

    public String getSales_qty() {return sales_qty.get();}
    public SimpleStringProperty sales_qtyProperty() {return sales_qty;}
    public void setSales_qty(String sales_qty) {this.sales_qty = new SimpleStringProperty(sales_qty);}

    public String getSales_unit() {return sales_unit.get();}
    public SimpleStringProperty sales_unitProperty() {return sales_unit;}
    public void setSales_unit(String sales_unit) {this.sales_unit = new SimpleStringProperty(sales_unit);}

    public String getSales_value() {return sales_value.get();}
    public SimpleStringProperty sales_valueProperty() {return sales_value;}
    public void setSales_value(String sales_value) {this.sales_value = new SimpleStringProperty(sales_value);}

    public String getClosing_balance_qty() {return closing_balance_qty.get();}
    public SimpleStringProperty closing_balance_qtyProperty() {return closing_balance_qty;}
    public void setClosing_balance_qty(String closing_balance_qty) {this.closing_balance_qty = new SimpleStringProperty(closing_balance_qty);}

    public String getClosing_balance_unit() {return closing_balance_unit.get();}
    public SimpleStringProperty closing_balance_unitProperty() {return closing_balance_unit;}
    public void setClosing_balance_unit(String closing_balance_unit) {this.closing_balance_unit = new SimpleStringProperty(closing_balance_unit);}

    public String getClosing_balance_value() {return closing_balance_value.get();}
    public SimpleStringProperty closing_balance_valueProperty() {return closing_balance_value;}
    public void setClosing_balance_value(String closing_balance_value) {this.closing_balance_value = new SimpleStringProperty(closing_balance_value);}

}
