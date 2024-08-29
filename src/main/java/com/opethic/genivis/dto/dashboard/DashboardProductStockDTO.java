package com.opethic.genivis.dto.dashboard;

import javafx.beans.property.SimpleStringProperty;

public class DashboardProductStockDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty product_name;
    private SimpleStringProperty brand;
    private SimpleStringProperty closing_stocks;

    public DashboardProductStockDTO(String id, String product_name, String brand, String closing_stocks) {
        this.id = new SimpleStringProperty(id);
        this.product_name = new SimpleStringProperty(product_name);
        this.brand = new SimpleStringProperty(brand);
        this.closing_stocks = new SimpleStringProperty(closing_stocks);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public SimpleStringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name.set(product_name);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public String getClosing_stocks() {
        return closing_stocks.get();
    }

    public SimpleStringProperty closing_stocksProperty() {
        return closing_stocks;
    }

    public void setClosing_stocks(String closing_stocks) {
        this.closing_stocks.set(closing_stocks);
    }
}
