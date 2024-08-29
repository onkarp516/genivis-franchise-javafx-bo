package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class StockReport3DTO {
    private SimpleStringProperty particulars;
    private SimpleStringProperty purchase_qty;
    private SimpleStringProperty purchase_unit;
    private SimpleStringProperty purchase_value;
    private SimpleStringProperty sales_qty;
    private SimpleStringProperty sales_unit;
    private SimpleStringProperty sales_value;
    private SimpleStringProperty closing_balance_qty;
    private SimpleStringProperty closing_balance_unit;
    private SimpleStringProperty closing_balance_value;

    public StockReport3DTO(
        String particulars,
        String purchase_qty,
        String purchase_unit,
        String purchase_value,
        String sales_qty,
        String sales_unit,
        String sales_value,
        String closing_balance_qty,
        String closing_balance_unit,
        String closing_balance_value){
        this.particulars = new SimpleStringProperty(particulars);
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

    public String getParticulars() {return particulars.get();}
    public SimpleStringProperty particularsProperty() {return particulars;}
    public void setParticulars(String particulars) {this.particulars = new SimpleStringProperty(particulars);}

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
