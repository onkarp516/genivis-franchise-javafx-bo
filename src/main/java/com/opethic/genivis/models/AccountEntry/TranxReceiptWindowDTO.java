package com.opethic.genivis.models.AccountEntry;

import com.opethic.genivis.dto.GstDetailsDTO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

public class TranxReceiptWindowDTO {

    private CheckBox isSelect;
    private Integer id;
    private Integer invoiceId;
    private String invoiceUniqueid;
    private String invoiceDate;
    private String invoiceNo;
    private Integer ledgerId;
    private String source;
    private String dueDays;
    private String balancingType;
    private Double paidAmt;
    private Integer billDetailsId;
    private Double remainingAmt;
    private Double amount;
    private Double totalAmt;



    public TranxReceiptWindowDTO(Integer id, Integer invoiceId, String invoiceUniqueid, String invoiceDate, String invoiceNo, Integer ledgerId, String source, String dueDays, String balancingType, Double paidAmt, Integer billDetailsId, Double remainingAmt, Double amount, Double totalAmt) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.invoiceUniqueid = invoiceUniqueid;
        this.invoiceDate = invoiceDate;
        this.invoiceNo = invoiceNo;
        this.ledgerId = ledgerId;
        this.source = source;
        this.dueDays = dueDays;
        this.balancingType = balancingType;
        this.paidAmt = paidAmt;
        this.billDetailsId = billDetailsId;
        this.remainingAmt = remainingAmt;
        this.amount = amount;
        this.totalAmt = totalAmt;
        this.isSelect = isSelect;
    }

    public Integer getId(
    ) {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceUniqueid() {
        return invoiceUniqueid;
    }

    public void setInvoiceUniqueid(String invoiceUniqueid) {
        this.invoiceUniqueid = invoiceUniqueid;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Integer getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Integer ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDueDays() {
        return dueDays;
    }

    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Integer getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Integer billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public CheckBox getIsSelect() {
        return isSelect;
    }


}
