package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

//@AllArgsConstructor
public class PurchaseInvoiceDTO {
    private SimpleStringProperty purInvoiceId;
    private SimpleStringProperty purInvoiceNumber;
    private SimpleStringProperty purInvoiceTranxId;
    private SimpleStringProperty purInvoiceDate;
    private SimpleStringProperty purInvoiceSupplierName;
    private SimpleStringProperty purInvoiceNarration;
    private SimpleStringProperty purInvoiceTaxable;
    private SimpleStringProperty purInvoiceTax;
    private SimpleStringProperty purInvoiceBillAmount;
    private SimpleStringProperty purInvoicePrint;
    private SimpleStringProperty purInvoiceBarcode;
    private  SimpleStringProperty purInvoiceAction;
    private SimpleStringProperty purInvoiceAccId;
    private SimpleStringProperty purInvoiceAccName;


    public PurchaseInvoiceDTO() {
    }

    public PurchaseInvoiceDTO(String id, String purInvoiceNumber, String tranxId, String invoiceDate, String supplierName,   //for list
                              String narration, String taxable, String tax, String billAmount){
         this.purInvoiceId = new SimpleStringProperty(id);
         this.purInvoiceNumber = new SimpleStringProperty(purInvoiceNumber);
         this.purInvoiceTranxId = new SimpleStringProperty(tranxId);
         this.purInvoiceDate =new SimpleStringProperty(invoiceDate);
         this.purInvoiceSupplierName = new SimpleStringProperty(supplierName);
         this.purInvoiceNarration = new SimpleStringProperty(narration);
         this.purInvoiceTaxable = new SimpleStringProperty(taxable);
         this.purInvoiceTax = new SimpleStringProperty(tax);
         this.purInvoiceBillAmount = new SimpleStringProperty(billAmount);
    }
    public PurchaseInvoiceDTO(String purAccId, String purAccName){    //for purchase Account
        this.purInvoiceAccId = new SimpleStringProperty(purAccId);
        this.purInvoiceAccName = new SimpleStringProperty(purAccName);
    }
    public String getPurInvoiceId(){
        return purInvoiceId.get();
    }
    public  void setPurInvoiceId(String id){
        this.purInvoiceId.set(id);
    }
    public String getPurInvoiceNumber(){
            return purInvoiceNumber.get();
    }
    public void setPurInvoiceNumber(String purInvoiceNumber){
        this.purInvoiceNumber.set(purInvoiceNumber);
    }
    public String getPurInvoiceTranxId(){
        return purInvoiceTranxId.get();
    }
    public void setPurInvoiceTranxId(String tranxId){
        this.purInvoiceTranxId.set(tranxId);
    }

    public String getPurInvoiceAction() {
        return purInvoiceAction.get();
    }
    public void setPurInvoiceAction(String purInvoiceAction) {
        this.purInvoiceAction.set(purInvoiceAction);
    }

    public String getPurInvoiceDate() {
        return purInvoiceDate.get();
    }

    public SimpleStringProperty purInvoiceDateProperty() {
        return purInvoiceDate;
    }

    public void setPurInvoiceDate(String purInvoiceDate) {
        this.purInvoiceDate.set(purInvoiceDate);
    }

    public String getPurInvoiceSupplierName() {
        return purInvoiceSupplierName.get();
    }

    public SimpleStringProperty purInvoiceSupplierNameProperty() {
        return purInvoiceSupplierName;
    }

    public void setPurInvoiceSupplierName(String purInvoiceSupplierName) {
        this.purInvoiceSupplierName.set(purInvoiceSupplierName);
    }

    public String getPurInvoiceNarration() {
        return purInvoiceNarration.get();
    }
    public void setPurInvoiceNarration(String purInvoiceNarration) {
        this.purInvoiceNarration.set(purInvoiceNarration);
    }

    public String getPurInvoiceTaxable() {
        return purInvoiceTaxable.get();
    }
    public void setPurInvoiceTaxable(String purInvoiceTaxable) {
        this.purInvoiceTaxable.set(purInvoiceTaxable);
    }

    public String getPurInvoiceTax() {
        return purInvoiceTax.get();
    }
    public void setPurInvoiceTax(String purInvoiceTax) {
        this.purInvoiceTax.set(purInvoiceTax);
    }

    public String getPurInvoiceBillAmount() {
        return purInvoiceBillAmount.get();
    }
    public void setPurInvoiceBillAmount(String purInvoiceBillAmount) {
        this.purInvoiceBillAmount.set(purInvoiceBillAmount);
    }
    public String getPurInvoiceAccId(){
        return purInvoiceAccId.get();
    }
    public void setPurInvoiceAccId(String purInvoiceAccId){
        this.purInvoiceAccId.set(purInvoiceAccId);
    }
    public String getPurInvoiceAccName(){
        return purInvoiceAccName.get();
    }
    public  void setPurInvoiceAccName(String accName){
        this.purInvoiceAccName.set(accName);
    }

}
