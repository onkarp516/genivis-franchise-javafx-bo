package com.opethic.genivis.dto.account_entry;

import javafx.beans.property.SimpleStringProperty;

public class JournalListDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty journalNo;
    private SimpleStringProperty journalTranxDate;
    private SimpleStringProperty journalSupplierName;
    private SimpleStringProperty journalNarration;
    private SimpleStringProperty journalTotalAmount;
    private SimpleStringProperty journalAction;

    public JournalListDTO(String id, String journalNo, String journalTranxDate, String journalSupplierName, String journalNarration, String journalTotalAmount, String journalAction) {
        this.id = new SimpleStringProperty(id);
        this.journalNo = new SimpleStringProperty(journalNo);
        this.journalTranxDate = new SimpleStringProperty(journalTranxDate);
        this.journalSupplierName = new SimpleStringProperty(journalSupplierName);
        this.journalNarration = new SimpleStringProperty(journalNarration);
        this.journalTotalAmount = new SimpleStringProperty(journalTotalAmount);
        this.journalAction = new SimpleStringProperty(journalAction);
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

    public String getJournalNo() {
        return journalNo.get();
    }

    public SimpleStringProperty journalNoProperty() {
        return journalNo;
    }

    public void setJournalNo(String journalNo) {
        this.journalNo.set(journalNo);
    }

    public String getJournalTranxDate() {
        return journalTranxDate.get();
    }

    public SimpleStringProperty journalTranxDateProperty() {
        return journalTranxDate;
    }

    public void setJournalTranxDate(String journalTranxDate) {
        this.journalTranxDate.set(journalTranxDate);
    }

    public String getJournalSupplierName() {
        return journalSupplierName.get();
    }

    public SimpleStringProperty journalSupplierNameProperty() {
        return journalSupplierName;
    }

    public void setJournalSupplierName(String journalSupplierName) {
        this.journalSupplierName.set(journalSupplierName);
    }

    public String getJournalNarration() {
        return journalNarration.get();
    }

    public SimpleStringProperty journalNarrationProperty() {
        return journalNarration;
    }

    public void setJournalNarration(String journalNarration) {
        this.journalNarration.set(journalNarration);
    }

    public String getJournalTotalAmount() {
        return journalTotalAmount.get();
    }

    public SimpleStringProperty journalTotalAmountProperty() {
        return journalTotalAmount;
    }

    public void setJournalTotalAmount(String journalTotalAmount) {
        this.journalTotalAmount.set(journalTotalAmount);
    }

    public String getJournalAction() {
        return journalAction.get();
    }

    public SimpleStringProperty journalActionProperty() {
        return journalAction;
    }

    public void setJournalAction(String journalAction) {
        this.journalAction.set(journalAction);
    }
}
