package com.opethic.genivis.dto;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.beans.property.SimpleStringProperty;

public class GstDTO {

    private SimpleStringProperty taxPer;
    private SimpleStringProperty cgst;

    private SimpleStringProperty sgst;
    private SimpleStringProperty igst;

    public GstDTO(String taxPer, String cgst, String sgst, String igst) {
        this.taxPer = new SimpleStringProperty( taxPer);
        this.cgst = new SimpleStringProperty(cgst);
        this.sgst =new SimpleStringProperty(sgst);
        this.igst = new SimpleStringProperty(igst);
    }

    public String getTaxPer() {
        return taxPer.get();
    }

    public SimpleStringProperty taxPerProperty() {
        return taxPer;
    }

    public void setTaxPer(String taxPer) {
        this.taxPer = new SimpleStringProperty(taxPer);
    }

    public String getCgst() {
        return cgst.get();
    }

    public SimpleStringProperty cgstProperty() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = new SimpleStringProperty(cgst);
    }

    public String getSgst() {
        return sgst.get();
    }

    public SimpleStringProperty sgstProperty() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = new SimpleStringProperty(sgst);
    }

    public String getIgst() {
        return igst.get();
    }

    public SimpleStringProperty igstProperty() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = new SimpleStringProperty(igst);
    }


    @Override
    public String toString() {
        return "GstDTO{" +
                "taxPer=" + taxPer +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", igst=" + igst +
                '}';
    }
}
