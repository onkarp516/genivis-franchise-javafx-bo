package com.opethic.genivis.dto.opening;

import javafx.beans.property.SimpleStringProperty;

public class ProAndUnisTable {
    private SimpleStringProperty productId = new SimpleStringProperty("");
    private SimpleStringProperty productName  = new SimpleStringProperty("");
    private SimpleStringProperty unitId  = new SimpleStringProperty("");
    private SimpleStringProperty unitName  = new SimpleStringProperty("");
    private SimpleStringProperty batchId = new SimpleStringProperty("");
    private SimpleStringProperty batchNo = new SimpleStringProperty("");
    private SimpleStringProperty mrp = new SimpleStringProperty("");
    private SimpleStringProperty purchaseRate = new SimpleStringProperty("");
    private SimpleStringProperty saleRate = new SimpleStringProperty("");
    private SimpleStringProperty freeQty = new SimpleStringProperty("");
    private SimpleStringProperty mfgDate = new SimpleStringProperty("");
    private SimpleStringProperty expireDate = new SimpleStringProperty("");
    private SimpleStringProperty openingQty = new SimpleStringProperty("");
    private SimpleStringProperty costing = new SimpleStringProperty("");


    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty productIdProperty() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getUnitId() {
        return unitId.get();
    }

    public SimpleStringProperty unitIdProperty() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId.set(unitId);
    }

    public String getUnitName() {
        return unitName.get();
    }

    public SimpleStringProperty unitNameProperty() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName.set(unitName);
    }

    public String getBatchId() {
        return batchId.get();
    }

    public SimpleStringProperty batchIdProperty() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId.set(batchId);
    }

    public String getBatchNo() {
        return batchNo.get();
    }

    public SimpleStringProperty batchNoProperty() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo.set(batchNo);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp);
    }

    public String getPurchaseRate() {
        return purchaseRate.get();
    }

    public SimpleStringProperty purchaseRateProperty() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate.set(purchaseRate);
    }

    public String getSaleRate() {
        return saleRate.get();
    }

    public SimpleStringProperty saleRateProperty() {
        return saleRate;
    }

    public void setSaleRate(String saleRate) {
        this.saleRate.set(saleRate);
    }

    public String getFreeQty() {
        return freeQty.get();
    }

    public SimpleStringProperty freeQtyProperty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty.set(freeQty);
    }

    public String getMfgDate() {
        return mfgDate.get();
    }

    public SimpleStringProperty mfgDateProperty() {
        return mfgDate;
    }

    public void setMfgDate(String mfgDate) {
        this.mfgDate.set(mfgDate);
    }

    public String getExpireDate() {
        return expireDate.get();
    }

    public SimpleStringProperty expireDateProperty() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate.set(expireDate);
    }

    public String getOpeningQty() {
        return openingQty.get();
    }

    public SimpleStringProperty openingQtyProperty() {
        return openingQty;
    }

    public void setOpeningQty(String openingQty) {
        this.openingQty.set(openingQty);
    }

    public String getCosting() {
        return costing.get();
    }

    public SimpleStringProperty costingProperty() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing.set(costing);
    }
}
