package com.opethic.genivis.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProductListBatchHistDTO {

    private SimpleStringProperty batchNo;
    private SimpleStringProperty manufacturingDate;
    private SimpleStringProperty expiryDate;
    private SimpleStringProperty closingStock;
    private SimpleStringProperty mrp;
    private SimpleStringProperty purchaseRate;
    private SimpleStringProperty disPer;
    private SimpleStringProperty disAmt;
    private SimpleStringProperty cessPer;
    private SimpleStringProperty cessAmt;
    private SimpleStringProperty barcode;
    private SimpleStringProperty marginPer;
    private SimpleStringProperty totalAmount;
    private SimpleStringProperty FSR;
    private SimpleStringProperty CSR;
    private SimpleStringProperty SaleRate;

    public ProductListBatchHistDTO(String batchNo, String manufacturingDate, String expiryDate, String closingStock,
                                   String mrp, String purchaseRate, String disPer, String disAmt,
                                   String cessPer, String cessAmt, String barcode, String marginPer,
                                   String totalAmount, String FSR, String CSR, String saleRate) {
        this.batchNo = new SimpleStringProperty(batchNo);
        this.manufacturingDate = new SimpleStringProperty(manufacturingDate);
        this.expiryDate = new SimpleStringProperty(expiryDate);
        this.closingStock = new SimpleStringProperty(closingStock);
        this.mrp = new SimpleStringProperty(mrp);
        this.purchaseRate = new SimpleStringProperty(purchaseRate);
        this.disPer = new SimpleStringProperty(disPer);
        this.disAmt = new SimpleStringProperty(disAmt);
        this.cessPer = new SimpleStringProperty(cessPer);
        this.cessAmt = new SimpleStringProperty(cessAmt);
        this.barcode = new SimpleStringProperty(barcode);
        this.marginPer = new SimpleStringProperty(marginPer);
        this.totalAmount = new SimpleStringProperty(totalAmount);
        this.FSR = new SimpleStringProperty(FSR);
        this.CSR = new SimpleStringProperty(CSR);
        SaleRate = new SimpleStringProperty(saleRate);
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

    public String getManufacturingDate() {
        return manufacturingDate.get();
    }

    public SimpleStringProperty manufacturingDateProperty() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate.set(manufacturingDate);
    }

    public String getExpiryDate() {
        return expiryDate.get();
    }

    public SimpleStringProperty expiryDateProperty() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate.set(expiryDate);
    }

    public String getClosingStock() {
        return closingStock.get();
    }

    public SimpleStringProperty closingStockProperty() {
        return closingStock;
    }

    public void setClosingStock(String closingStock) {
        this.closingStock.set(closingStock);
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

    public String getDisPer() {
        return disPer.get();
    }

    public SimpleStringProperty disPerProperty() {
        return disPer;
    }

    public void setDisPer(String disPer) {
        this.disPer.set(disPer);
    }

    public String getDisAmt() {
        return disAmt.get();
    }

    public SimpleStringProperty disAmtProperty() {
        return disAmt;
    }

    public void setDisAmt(String disAmt) {
        this.disAmt.set(disAmt);
    }

    public String getCessPer() {
        return cessPer.get();
    }

    public SimpleStringProperty cessPerProperty() {
        return cessPer;
    }

    public void setCessPer(String cessPer) {
        this.cessPer.set(cessPer);
    }

    public String getCessAmt() {
        return cessAmt.get();
    }

    public SimpleStringProperty cessAmtProperty() {
        return cessAmt;
    }

    public void setCessAmt(String cessAmt) {
        this.cessAmt.set(cessAmt);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getMarginPer() {
        return marginPer.get();
    }

    public SimpleStringProperty marginPerProperty() {
        return marginPer;
    }

    public void setMinMargin(String marginPer) {
        this.marginPer.set(marginPer);
    }

    public String getTotalAmount() {
        return totalAmount.get();
    }

    public SimpleStringProperty totalAmountProperty() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public String getFSR() {
        return FSR.get();
    }

    public SimpleStringProperty FSRProperty() {
        return FSR;
    }

    public void setFSR(String FSR) {
        this.FSR.set(FSR);
    }

    public String getCSR() {
        return CSR.get();
    }

    public SimpleStringProperty CSRProperty() {
        return CSR;
    }

    public void setCSR(String CSR) {
        this.CSR.set(CSR);
    }

    public String getSaleRate() {
        return SaleRate.get();
    }

    public SimpleStringProperty saleRateProperty() {
        return SaleRate;
    }

    public void setSaleRate(String saleRate) {
        this.SaleRate.set(saleRate);
    }
}
