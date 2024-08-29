package com.opethic.genivis.dto.counter;

import javafx.beans.property.SimpleStringProperty;

public class CounterSaleAddPrescritDTO {
    private SimpleStringProperty details_id;

    private SimpleStringProperty Product;

    private SimpleStringProperty Package1;

    private SimpleStringProperty unit;
    private SimpleStringProperty qty;
    private SimpleStringProperty noOfTimes;
    private SimpleStringProperty days;
    private SimpleStringProperty total;

    private SimpleStringProperty action;

    public CounterSaleAddPrescritDTO(String details_id, String product,String Package1, String unit, String qty,
                                     String noOfTimes, String days, String total, String action) {
        this.details_id = new SimpleStringProperty(details_id);
        this.Product = new SimpleStringProperty(product);
        this.Package1 = new SimpleStringProperty(Package1);
        this.unit =new SimpleStringProperty(unit);
        this.qty =new SimpleStringProperty(qty);
        this.noOfTimes = new SimpleStringProperty(noOfTimes);
        this.days = new SimpleStringProperty(days);
        this.total = new SimpleStringProperty(total);
        this.action =new SimpleStringProperty(action);
    }

    public String getDetails_id() {
        return details_id.get();
    }

    public SimpleStringProperty details_idProperty() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id=new SimpleStringProperty(details_id);
    }

    public String getProduct() {
        return Product.get();
    }

    public SimpleStringProperty productProperty() {
        return Product;
    }

    public void setProduct(String product) {
        this.Product=new SimpleStringProperty(product);
    }

    public String getPackage1() {
        return Package1.get();
    }

    public SimpleStringProperty package1Property() {
        return Package1;
    }

    public void setPackage1(String package1) {
        this.Package1=new SimpleStringProperty(package1);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit=new SimpleStringProperty(unit);
    }

    public String getQty() {
        return qty.get();
    }

    public SimpleStringProperty qtyProperty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty=new SimpleStringProperty(qty);
    }

    public String getNoOfTimes() {
        return noOfTimes.get();
    }

    public SimpleStringProperty noOfTimesProperty() {
        return noOfTimes;
    }

    public void setNoOfTimes(String noOfTimes) {
        this.noOfTimes=new SimpleStringProperty(noOfTimes);
    }

    public String getDays() {
        return days.get();
    }

    public SimpleStringProperty daysProperty() {
        return days;
    }

    public void setDays(String days) {
        this.days=new SimpleStringProperty(days);
    }

    public String getTotal() {
        return total.get();
    }

    public SimpleStringProperty totalProperty() {
        return total;
    }

    public void setTotal(String total) {
        this.total=new SimpleStringProperty(total);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action=new SimpleStringProperty(action);
    }
}
