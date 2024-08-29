package com.opethic.genivis.dto.pur_invoice.reqres;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class PurRtnBillListDTO {
    private String index="";
    private String total_amt;
    private CheckBox action;

    private String debit_note_date;
    private String id;
    private String debit_note_no;
    private String invoice_id;
    private String isSelected;
    private String source;

    public PurRtnBillListDTO() {
    }

    public PurRtnBillListDTO(String index, String total_amt,
                             CheckBox action, String debit_note_date, String id, String debit_note_no,
                             String invoice_id, String isSelected, String source) {
        this.index = index;
        this.total_amt = total_amt;
        this.action = action;
        this.debit_note_date = debit_note_date;
        this.id = id;
        this.debit_note_no = debit_note_no;
        this.invoice_id = invoice_id;
        this.isSelected = isSelected;
        this.source = source;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public CheckBox getAction() {
        return action;
    }

    public void setAction(CheckBox action) {
        this.action = action;
    }

    public String getDebit_note_date() {
        return debit_note_date;
    }

    public void setDebit_note_date(String debit_note_date) {
        this.debit_note_date = debit_note_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDebit_note_no() {
        return debit_note_no;
    }

    public void setDebit_note_no(String debit_note_no) {
        this.debit_note_no = debit_note_no;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
