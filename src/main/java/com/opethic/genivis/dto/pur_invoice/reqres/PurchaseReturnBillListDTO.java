package com.opethic.genivis.dto.pur_invoice.reqres;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class PurchaseReturnBillListDTO {
    private SimpleStringProperty index = new SimpleStringProperty("");
    private SimpleStringProperty total_amt;
    private CheckBox action;

    private SimpleStringProperty debit_note_date;
    private SimpleStringProperty id;
    private SimpleStringProperty debit_note_no;
    private SimpleStringProperty invoice_id;
    private SimpleStringProperty isSelected;
    private SimpleStringProperty source;


    public PurchaseReturnBillListDTO(CheckBox action,String total_amt, String debit_note_date, String id,
                                     String debit_note_no, String invoice_id, String isSelected, String source) {
//        this.action = new SimpleBooleanProperty(action);
        this.action = action;

        this.total_amt = new SimpleStringProperty(total_amt);
        this.debit_note_date = new SimpleStringProperty(debit_note_date);
        this.id = new  SimpleStringProperty(id);
        this.debit_note_no =  new SimpleStringProperty(debit_note_no);
        this.invoice_id =new SimpleStringProperty(invoice_id);
        this.isSelected = new SimpleStringProperty(isSelected);
        this.source =new SimpleStringProperty(source);
    }

    public CheckBox getAction() {
        return action;
    }

   /* public SimpleBooleanProperty actionProperty() {
        return action;
    }*/

    public void setAction(CheckBox action) {
        this.action=action;
    }

    public String getTotal_amt() {
        return total_amt.get();
    }

    public SimpleStringProperty total_amtProperty() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt=new SimpleStringProperty(total_amt);
    }

    public String getDebit_note_date() {
        return debit_note_date.get();
    }

    public SimpleStringProperty debit_note_dateProperty() {
        return debit_note_date;
    }

    public void setDebit_note_date(String debit_note_date) {
        this.debit_note_date=new SimpleStringProperty(debit_note_date);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }

    public String getDebit_note_no() {
        return debit_note_no.get();
    }

    public SimpleStringProperty debit_note_noProperty() {
        return debit_note_no;
    }

    public void setDebit_note_no(String debit_note_no) {
        this.debit_note_no=new SimpleStringProperty(debit_note_no);
    }

    public String getInvoice_id() {
        return invoice_id.get();
    }

    public SimpleStringProperty invoice_idProperty() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id=new SimpleStringProperty(invoice_id);
    }

    public String getIsSelected() {
        return isSelected.get();
    }

    public SimpleStringProperty isSelectedProperty() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected=new SimpleStringProperty(isSelected);
    }

    public String getSource() {
        return source.get();
    }

    public SimpleStringProperty sourceProperty() {
        return source;
    }

    public void setSource(String source) {
        this.source=new SimpleStringProperty(source);
    }


}
