package com.opethic.genivis.dto;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class CounterSaleDTO {

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }
    private CheckBox select;
    private SimpleStringProperty id;
    private SimpleStringProperty invoiceNo;

    private SimpleStringProperty invoiceDate;
    private SimpleStringProperty mobile;

    private SimpleStringProperty qty;
    private SimpleStringProperty billAmt;
    private SimpleStringProperty billAmtInv;

    private SimpleStringProperty paymentMode;

    private SimpleStringProperty productName;

    private SimpleStringProperty packaging;
    private SimpleStringProperty unit;

    private SimpleStringProperty batch;

    private SimpleStringProperty rate;
    private  SimpleStringProperty discount;


    private SimpleStringProperty clientName;

    private SimpleStringProperty clientAddress;
    private SimpleBooleanProperty isRowSelected;
    public CounterSaleDTO(String id,String invoiceNo, String invoiceDate, String mobile, String qty,
                          String billAmt, String paymentMode, String productName, String packaging,
                          String unit, String batch, String rate, String discount,String clientName,String clientAddress, String billAmtInv) {
        this.id =new SimpleStringProperty(id);
        this.invoiceNo =new SimpleStringProperty(invoiceNo);
        this.invoiceDate =new SimpleStringProperty(invoiceDate);
        this.mobile =new SimpleStringProperty(mobile);
        this.qty =new SimpleStringProperty(qty);
        this.billAmt =new SimpleStringProperty(billAmt);
        this.paymentMode =new SimpleStringProperty(paymentMode);
        this.productName =new SimpleStringProperty(productName);
        this.packaging =new SimpleStringProperty(packaging);
        this.unit =new SimpleStringProperty(unit);
        this.batch =new SimpleStringProperty(batch);
        this.rate =new SimpleStringProperty(rate);
        this.discount =new SimpleStringProperty(discount);
        this.clientName =new SimpleStringProperty(clientName);
        this.clientAddress =new SimpleStringProperty(clientAddress);
        this.billAmtInv =new SimpleStringProperty(billAmtInv);
        this.isRowSelected =new SimpleBooleanProperty(false);
    }

    public CheckBox getSelect() {
        return select;
    }

   /* public SimpleBooleanProperty actionProperty() {
        return action;
    }*/

    public void setSelect(CheckBox select) {
        this.select=select;
    }

    public boolean isIsRowSelected() {
        return isRowSelected.get();
    }

    public SimpleBooleanProperty isRowSelectedProperty() {
        return isRowSelected;
    }

    public void setIsRowSelected(boolean isRowSelected) {
        this.isRowSelected.set(isRowSelected);
    }

    public String getInvoiceNo() {
        return invoiceNo.get();
    }

    public SimpleStringProperty invoiceNoProperty() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo=new SimpleStringProperty(invoiceNo);
    }

    public String getInvoiceDate() {
        return invoiceDate.get();
    }

    public SimpleStringProperty invoiceDateProperty() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate=new SimpleStringProperty(invoiceDate);
    }

    public String getMobile() {
        return mobile.get();
    }

    public SimpleStringProperty mobileProperty() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile=new SimpleStringProperty(mobile);
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

    public String getBillAmt() {
        return billAmt.get();
    }

    public SimpleStringProperty billAmtProperty() {
        return billAmt;
    }

    public void setBillAmt(String billAmt) {
        this.billAmt=new SimpleStringProperty(billAmt);
    }

    public String getPaymentMode() {
        return paymentMode.get();
    }

    public SimpleStringProperty paymentModeProperty() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode=new SimpleStringProperty(paymentMode);
    }

    public String getProductName() {
        return productName.get();
    }

    public SimpleStringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName=new SimpleStringProperty(productName);
    }

    public String getPackaging() {
        return packaging.get();
    }

    public SimpleStringProperty packagingProperty() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging=new SimpleStringProperty(packaging);
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

    public String getBatch() {
        return batch.get();
    }

    public SimpleStringProperty batchProperty() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch=new SimpleStringProperty(batch);
    }

    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate=new SimpleStringProperty(rate);
    }

    public String getDiscount() {
        return discount.get();
    }

    public SimpleStringProperty discountProperty() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount=new SimpleStringProperty(discount);
    }

    public String getClientName() {
        return clientName.get();
    }

    public SimpleStringProperty clientNameProperty() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName=new SimpleStringProperty(clientName);
    }

    public String getClientAddress() {
        return clientAddress.get();
    }

    public SimpleStringProperty clientAddressProperty() {
        return clientAddress;
    }

    public void setClientAddress(String clientAddress) {
        this.clientAddress=new SimpleStringProperty(clientAddress);
    }

    public String getBillAmtInv() {
        return billAmtInv.get();
    }

    public SimpleStringProperty billAmtInvProperty() {
        return billAmtInv;
    }

    public void setBillAmtInv(String billAmtInv) {
        this.billAmtInv.set(billAmtInv);
    }


    @Override
    public String toString() {
        return "CounterSaleDTO{" +
                "select=" + select +
                ", id=" + id +
                ", invoiceNo=" + invoiceNo +
                ", invoiceDate=" + invoiceDate +
                ", mobile=" + mobile +
                ", qty=" + qty +
                ", billAmt=" + billAmt +
                ", billAmtInv=" + billAmtInv +
                ", paymentMode=" + paymentMode +
                ", productName=" + productName +
                ", packaging=" + packaging +
                ", unit=" + unit +
                ", batch=" + batch +
                ", rate=" + rate +
                ", discount=" + discount +
                ", clientName=" + clientName +
                ", clientAddress=" + clientAddress +
                ", isRowSelected=" + isRowSelected +
                '}';
    }
}
