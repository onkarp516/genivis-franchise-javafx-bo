package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class StockReport1BatchDTO {
    private SimpleStringProperty batch_no;
    private SimpleStringProperty expiry_date;

    public StockReport1BatchDTO(
            String batch_no,
            String expiry_date){
        this.batch_no = new SimpleStringProperty(batch_no);
        this.expiry_date = new SimpleStringProperty(expiry_date);
    }
    public String getBatch_no() {return batch_no.get();}
    public SimpleStringProperty batch_noProperty() {return batch_no;}
    public void setBatch_no(String batch_no) {this.batch_no = new SimpleStringProperty(batch_no);}

    public String getExpiry_date() {return expiry_date.get();}
    public SimpleStringProperty expiry_dateProperty() {return expiry_date;}
    public void setExpiry_date(String expiry_date) {this.expiry_date = new SimpleStringProperty(expiry_date);}
}
