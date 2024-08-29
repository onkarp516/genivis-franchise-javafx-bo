package com.opethic.genivis.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesOrderListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty tcSalesOrderListSONo;
    private SimpleStringProperty tcSalesOrderListRefNo;
    private SimpleStringProperty tcSalesOrderListSODate;
    private SimpleStringProperty tcSalesOrderListClientName;
    private SimpleStringProperty tcSalesOrderListNarration;
    private SimpleStringProperty tcSalesOrderListTaxable;
    private SimpleStringProperty tcSalesOrderListTax;
    private SimpleStringProperty tcSalesOrderListBillAmount;
    private SimpleStringProperty tcSalesOrderListTranxStatus;
    private SimpleStringProperty tcSalesOrderListPrint;
    private SimpleStringProperty tcSalesOrderListAction;
    private SimpleBooleanProperty is_row_selected;
    private SimpleStringProperty ledgerId;      //for conversion

    public SalesOrderListDTO(String id, String tcSalesOrderListSONo, String tcSalesOrderListRefNo,
                             String tcSalesOrderListSODate, String tcSalesOrderListClientName,String tcSalesOrderListNarration, String tcSalesOrderListTaxable,String tcSalesOrderListTax,
                             String tcSalesOrderListBillAmount,String tcSalesOrderListTranxStatus,String tcSalesOrderListPrint,String tcSalesOrderListAction, String ledgerId) {
        this.id = new SimpleStringProperty(id);
        this.tcSalesOrderListSONo = new SimpleStringProperty(tcSalesOrderListSONo);
        this.tcSalesOrderListRefNo = new SimpleStringProperty(tcSalesOrderListRefNo);
        this.tcSalesOrderListSODate = new SimpleStringProperty(tcSalesOrderListSODate);
        this.tcSalesOrderListClientName = new SimpleStringProperty(tcSalesOrderListClientName);
        this.tcSalesOrderListNarration = new SimpleStringProperty(tcSalesOrderListNarration);
        this.tcSalesOrderListTaxable = new SimpleStringProperty(tcSalesOrderListTaxable);
        this.tcSalesOrderListTax = new SimpleStringProperty(tcSalesOrderListTax);
        this.tcSalesOrderListBillAmount = new SimpleStringProperty(tcSalesOrderListBillAmount);
        this.tcSalesOrderListTranxStatus = new SimpleStringProperty(tcSalesOrderListTranxStatus);
        this.tcSalesOrderListPrint = new SimpleStringProperty(tcSalesOrderListPrint);
        this.tcSalesOrderListAction = new SimpleStringProperty(tcSalesOrderListAction);
        this.is_row_selected = new SimpleBooleanProperty(false);
        this.ledgerId = new SimpleStringProperty(ledgerId);
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

    public String getTcSalesOrderListSONo() {
        return tcSalesOrderListSONo.get();
    }

    public SimpleStringProperty tcSalesOrderListSONoProperty() {
        return tcSalesOrderListSONo;
    }

    public void setTcSalesOrderListSONo(String tcSalesOrderListSONo) {
        this.tcSalesOrderListSONo.set(tcSalesOrderListSONo);
    }

    public String getTcSalesOrderListRefNo() {
        return tcSalesOrderListRefNo.get();
    }

    public SimpleStringProperty tcSalesOrderListRefNoProperty() {
        return tcSalesOrderListRefNo;
    }

    public void setTcSalesOrderListRefNo(String tcSalesOrderListRefNo) {
        this.tcSalesOrderListRefNo.set(tcSalesOrderListRefNo);
    }

    public String getTcSalesOrderListSODate() {
        return tcSalesOrderListSODate.get();
    }

    public SimpleStringProperty tcSalesOrderListSODateProperty() {
        return tcSalesOrderListSODate;
    }

    public void setTcSalesOrderListSODate(String tcSalesOrderListSODate) {
        this.tcSalesOrderListSODate.set(tcSalesOrderListSODate);
    }

    public String getTcSalesOrderListClientName() {
        return tcSalesOrderListClientName.get();
    }

    public SimpleStringProperty tcSalesOrderListClientNameProperty() {
        return tcSalesOrderListClientName;
    }

    public void setTcSalesOrderListClientName(String tcSalesOrderListClientName) {
        this.tcSalesOrderListClientName.set(tcSalesOrderListClientName);
    }

    public String getTcSalesOrderListNarration() {
        return tcSalesOrderListNarration.get();
    }

    public SimpleStringProperty tcSalesOrderListNarrationProperty() {
        return tcSalesOrderListNarration;
    }

    public void setTcSalesOrderListNarration(String tcSalesOrderListNarration) {
        this.tcSalesOrderListNarration.set(tcSalesOrderListNarration);
    }

    public String getTcSalesOrderListTaxable() {
        return tcSalesOrderListTaxable.get();
    }

    public SimpleStringProperty tcSalesOrderListTaxableProperty() {
        return tcSalesOrderListTaxable;
    }

    public void setTcSalesOrderListTaxable(String tcSalesOrderListTaxable) {
        this.tcSalesOrderListTaxable.set(tcSalesOrderListTaxable);
    }

    public String getTcSalesOrderListTax() {
        return tcSalesOrderListTax.get();
    }

    public SimpleStringProperty tcSalesOrderListTaxProperty() {
        return tcSalesOrderListTax;
    }

    public void setTcSalesOrderListTax(String tcSalesOrderListTax) {
        this.tcSalesOrderListTax.set(tcSalesOrderListTax);
    }

    public String getTcSalesOrderListBillAmount() {
        return tcSalesOrderListBillAmount.get();
    }

    public SimpleStringProperty tcSalesOrderListBillAmountProperty() {
        return tcSalesOrderListBillAmount;
    }

    public void setTcSalesOrderListBillAmount(String tcSalesOrderListBillAmount) {
        this.tcSalesOrderListBillAmount.set(tcSalesOrderListBillAmount);
    }

    public String getTcSalesOrderListTranxStatus() {
        return tcSalesOrderListTranxStatus.get();
    }

    public SimpleStringProperty tcSalesOrderListTranxStatusProperty() {
        return tcSalesOrderListTranxStatus;
    }

    public void setTcSalesOrderListTranxStatus(String tcSalesOrderListTranxStatus) {
        this.tcSalesOrderListTranxStatus.set(tcSalesOrderListTranxStatus);
    }

    public String getTcSalesOrderListPrint() {
        return tcSalesOrderListPrint.get();
    }

    public SimpleStringProperty tcSalesOrderListPrintProperty() {
        return tcSalesOrderListPrint;
    }

    public void setTcSalesOrderListPrint(String tcSalesOrderListPrint) {
        this.tcSalesOrderListPrint.set(tcSalesOrderListPrint);
    }

    public String getTcSalesOrderListAction() {
        return tcSalesOrderListAction.get();
    }

    public SimpleStringProperty tcSalesOrderListActionProperty() {
        return tcSalesOrderListAction;
    }

    public void setTcSalesOrderListAction(String tcSalesOrderListAction) {
        this.tcSalesOrderListAction.set(tcSalesOrderListAction);
    }

    public boolean isIs_row_selected() {
        return is_row_selected.get();
    }

    public SimpleBooleanProperty is_row_selectedProperty() {
        return is_row_selected;
    }

    public void setIs_row_selected(boolean is_row_selected) {
        this.is_row_selected.set(is_row_selected);
    }

    public String getLedgerId() {
        return ledgerId.get();
    }

    public SimpleStringProperty ledgerIdProperty() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId.set(ledgerId);
    }
    @Override
    public String toString() {
        return String.format("{\"id\": \"%s\"}",
                id.getValue());
    }
}
