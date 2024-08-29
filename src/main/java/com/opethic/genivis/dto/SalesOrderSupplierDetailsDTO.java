package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class SalesOrderSupplierDetailsDTO {
    private SimpleStringProperty supplierName;
    private SimpleStringProperty supplierInvNo;
    private SimpleStringProperty supplierInvDate;
    private SimpleStringProperty supplierBatch;
    private SimpleStringProperty supplierMrp;
    private SimpleStringProperty supplierQty;
    private SimpleStringProperty supplierRate;
    private SimpleStringProperty supplierCost;
    private SimpleStringProperty supplierDisPer;
    private SimpleStringProperty supplierDisAmt;

    public SalesOrderSupplierDetailsDTO(String supplierName, String supplierInvNo, String supplierInvDate, String supplierBatch, String supplierMrp, String supplierQty, String supplierRate, String supplierCost, String supplierDisPer, String supplierDisAmt) {
        this.supplierName = new SimpleStringProperty(supplierName);
        this.supplierInvNo = new SimpleStringProperty(supplierInvNo);
        this.supplierInvDate = new SimpleStringProperty(supplierInvDate);
        this.supplierBatch = new SimpleStringProperty(supplierBatch);
        this.supplierMrp = new SimpleStringProperty(supplierMrp);
        this.supplierQty = new SimpleStringProperty(supplierQty);
        this.supplierRate = new SimpleStringProperty(supplierRate);
        this.supplierCost = new SimpleStringProperty(supplierCost);
        this.supplierDisPer = new SimpleStringProperty(supplierDisPer);
        this.supplierDisAmt = new SimpleStringProperty(supplierDisAmt);
    }

    public String getSupplierName() {
        return supplierName.get();
    }

    public SimpleStringProperty supplierNameProperty() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.set(supplierName);
    }

    public String getSupplierInvNo() {
        return supplierInvNo.get();
    }

    public SimpleStringProperty supplierInvNoProperty() {
        return supplierInvNo;
    }

    public void setSupplierInvNo(String supplierInvNo) {
        this.supplierInvNo.set(supplierInvNo);
    }

    public String getSupplierInvDate() {
        return supplierInvDate.get();
    }

    public SimpleStringProperty supplierInvDateProperty() {
        return supplierInvDate;
    }

    public void setSupplierInvDate(String supplierInvDate) {
        this.supplierInvDate.set(supplierInvDate);
    }

    public String getSupplierBatch() {
        return supplierBatch.get();
    }

    public SimpleStringProperty supplierBatchProperty() {
        return supplierBatch;
    }

    public void setSupplierBatch(String supplierBatch) {
        this.supplierBatch.set(supplierBatch);
    }

    public String getSupplierMrp() {
        return supplierMrp.get();
    }

    public SimpleStringProperty supplierMrpProperty() {
        return supplierMrp;
    }

    public void setSupplierMrp(String supplierMrp) {
        this.supplierMrp.set(supplierMrp);
    }

    public String getSupplierQty() {
        return supplierQty.get();
    }

    public SimpleStringProperty supplierQtyProperty() {
        return supplierQty;
    }

    public void setSupplierQty(String supplierQty) {
        this.supplierQty.set(supplierQty);
    }

    public String getSupplierRate() {
        return supplierRate.get();
    }

    public SimpleStringProperty supplierRateProperty() {
        return supplierRate;
    }

    public void setSupplierRate(String supplierRate) {
        this.supplierRate.set(supplierRate);
    }

    public String getSupplierCost() {
        return supplierCost.get();
    }

    public SimpleStringProperty supplierCostProperty() {
        return supplierCost;
    }

    public void setSupplierCost(String supplierCost) {
        this.supplierCost.set(supplierCost);
    }

    public String getSupplierDisPer() {
        return supplierDisPer.get();
    }

    public SimpleStringProperty supplierDisPerProperty() {
        return supplierDisPer;
    }

    public void setSupplierDisPer(String supplierDisPer) {
        this.supplierDisPer.set(supplierDisPer);
    }

    public String getSupplierDisAmt() {
        return supplierDisAmt.get();
    }

    public SimpleStringProperty supplierDisAmtProperty() {
        return supplierDisAmt;
    }

    public void setSupplierDisAmt(String supplierDisAmt) {
        this.supplierDisAmt.set(supplierDisAmt);
    }
}
