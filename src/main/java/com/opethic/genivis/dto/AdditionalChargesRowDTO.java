//Model CmpTRow
package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class AdditionalChargesRowDTO {
    private SimpleStringProperty particulars;
    private SimpleStringProperty amt;
    private SimpleStringProperty percentage;
//    private SimpleStringProperty action;



    public AdditionalChargesRowDTO(String particulars, String amt, String percentage) {
        this.particulars = new SimpleStringProperty(particulars);
        this.amt = new SimpleStringProperty(amt);
        this.percentage =  new SimpleStringProperty(percentage);

    }

    public String getParticulars() {
        return particulars.get();
    }

    public SimpleStringProperty particularsProperty() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars=new SimpleStringProperty(particulars);
    }

    public String getAmt() {
        return amt.get();
    }

    public SimpleStringProperty amtProperty() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt=new SimpleStringProperty(amt);
    }

    public String getPercentage() {
        return percentage.get();
    }

    public SimpleStringProperty percentageProperty() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage=new SimpleStringProperty(percentage);
    }
}