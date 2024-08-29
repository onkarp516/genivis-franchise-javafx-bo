package com.opethic.genivis.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty searchCode;
    private SimpleStringProperty productName;
    private SimpleStringProperty packing;
    private SimpleStringProperty barcode;
    private SimpleStringProperty brand;
    private SimpleStringProperty mrp;
    private SimpleStringProperty currentStock;
    private SimpleStringProperty unit;
    private SimpleStringProperty saleRate;


    public ProductDTO(String id, String searchCode, String productName, String packing,
                      String barcode, String brand, String mrp,
                      String currentStock, String unit,
                      String saleRate) {

        this.id = new SimpleStringProperty(id);
        this.searchCode = new SimpleStringProperty(searchCode);
        this.productName = new SimpleStringProperty(productName);
        this.packing = new SimpleStringProperty(packing);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.mrp = new SimpleStringProperty(mrp);
        this.currentStock = new SimpleStringProperty(currentStock);
        this.unit = new SimpleStringProperty(unit);
        this.saleRate = new SimpleStringProperty(saleRate);
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

    public String getSearchCode() {
        return searchCode.get();
    }

    public SimpleStringProperty searchCodeProperty() {
        return searchCode;
    }

    public void setSearchCode(String searchCode) {
        this.searchCode = new SimpleStringProperty(searchCode);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = new SimpleStringProperty(productName);
    }

    public String getPacking() {
        return packing.get();
    }

    public SimpleStringProperty packingProperty() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = new SimpleStringProperty(packing);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = new SimpleStringProperty(barcode);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = new SimpleStringProperty(brand);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = new SimpleStringProperty(mrp);
    }

    public String getCurrentStock() {
        return currentStock.get();
    }

    public SimpleStringProperty currentStockProperty() {
        return currentStock;
    }

    public void setCurrentStock(String currentStock) {
        this.currentStock = new SimpleStringProperty(currentStock);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = new SimpleStringProperty(unit);
    }

    public String getSaleRate() {
        return saleRate.get();
    }

    public SimpleStringProperty saleRateProperty() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate = new SimpleStringProperty(saleRate);
    }

}
