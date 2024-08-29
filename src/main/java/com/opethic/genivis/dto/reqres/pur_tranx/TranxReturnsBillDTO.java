package com.opethic.genivis.dto.reqres.pur_tranx;

import com.opethic.genivis.dto.GstDetailsDTO;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class TranxReturnsBillDTO {
    private String id;
    private String source;
    private String invoiceNo;
    private String invoiceAmount;
    private String invoiceDate;


    public TranxReturnsBillDTO() {
    }

    public TranxReturnsBillDTO(String id, String source, String invoiceNo, String invoiceAmount, String invoiceDate) {
        this.id = id;
        this.source = source;
        this.invoiceNo = invoiceNo;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
