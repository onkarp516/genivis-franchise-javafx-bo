package com.opethic.genivis.dto.master.ledger;

import com.opethic.genivis.utils.DateConvertUtil;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.json.JSONArray;
import org.json.JSONObject;

public class OBListDTO {
    private Integer id;
    private String invoiceNo;
    private String invoiceDate;
    private String dueDate;
    private Integer dueDays;
    private Double billAmt;
    private Double invoicePaidAmt;
    private Double invoiceBalAmt;
    private String type;

    public OBListDTO(Integer id, String invoiceNo, String invoiceDate, String dueDate, Integer dueDays, Double billAmt, Double invoicePaidAmt, Double invoiceBalAmt, String type) {
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.dueDays = dueDays;
        this.billAmt = billAmt;
        this.invoicePaidAmt = invoicePaidAmt;
        this.invoiceBalAmt = invoiceBalAmt;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }

    public Double getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(Double billAmt) {
        this.billAmt = billAmt;
    }

    public Double getInvoicePaidAmt() {
        return invoicePaidAmt;
    }

    public void setInvoicePaidAmt(Double invoicePaidAmt) {
        this.invoicePaidAmt = invoicePaidAmt;
    }

    public Double getInvoiceBalAmt() {
        return invoiceBalAmt;
    }

    public void setInvoiceBalAmt(Double invoiceBalAmt) {
        this.invoiceBalAmt = invoiceBalAmt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject toAPIData() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("invoice_no", invoiceNo);
        obj.put("invoice_date", DateConvertUtil.convertStringDatetoAPIDateString(invoiceDate));
        obj.put("due_date", DateConvertUtil.convertStringDatetoAPIDateString(dueDate));
        obj.put("due_days", dueDays);
        obj.put("bill_amt", billAmt);
        obj.put("invoice_paid_amt", invoicePaidAmt);
        obj.put("invoice_bal_amt", invoiceBalAmt);
        JSONObject typeObj = new JSONObject();
        typeObj.put("label", type);
        obj.put("type", typeObj);
        return obj;
    }

    @Override
    public String toString() {
        return "OBListDTO{" +
                "id=" + id +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", dueDays=" + dueDays +
                ", billAmt=" + billAmt +
                ", invoicePaidAmt=" + invoicePaidAmt +
                ", invoiceBalAmt=" + invoiceBalAmt +
                ", type='" + type + '\'' +
                '}';
    }
}
