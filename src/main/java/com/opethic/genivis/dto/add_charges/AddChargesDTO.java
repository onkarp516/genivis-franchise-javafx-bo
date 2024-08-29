package com.opethic.genivis.dto.add_charges;

import javafx.beans.property.SimpleStringProperty;

public class AddChargesDTO {
    private SimpleStringProperty current_index;

    private SimpleStringProperty sr_no = new SimpleStringProperty("0");
    private SimpleStringProperty additional_charges_details_id= new SimpleStringProperty("0");
    private SimpleStringProperty ledgerId ;

    private SimpleStringProperty particular;
    private SimpleStringProperty amt;
    private SimpleStringProperty percent;
    private SimpleStringProperty actions;

    public AddChargesDTO(String current_index, String ledgerId,String particular, String amt, String percent, String actions) {
        this.current_index = new SimpleStringProperty(current_index);

        this.ledgerId = new SimpleStringProperty(ledgerId);
        this.amt = new SimpleStringProperty(amt);
        this.percent = new SimpleStringProperty(percent);
        this.actions = new SimpleStringProperty(actions);
        this.particular = new SimpleStringProperty(particular);
    }

    public AddChargesDTO(String current_index, String additional_charges_details_id,  String ledgerId,String particular, String amt, String percent, String actions) {
        this.current_index = new SimpleStringProperty(current_index);
        this.ledgerId = new SimpleStringProperty(ledgerId);
        this.amt = new SimpleStringProperty(amt);
        this.percent = new SimpleStringProperty(percent);
        this.actions = new SimpleStringProperty(actions);
        this.particular = new SimpleStringProperty(particular);
        this.additional_charges_details_id = new SimpleStringProperty(additional_charges_details_id);
    }


    public String getCurrent_index() {
        return current_index.get();
    }

    public void setParticular(String particular) {
        this.particular.set(particular);
    }

    public SimpleStringProperty current_indexProperty() {
        return current_index;
    }

    public String getSr_no() {
        return sr_no.get();
    }

    public String getParticular() {
        return particular.get();
    }

    public SimpleStringProperty particularProperty() {
        return particular;
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public String getAdditional_charges_details_id() {
        return additional_charges_details_id.get();
    }

    public SimpleStringProperty additional_charges_details_idProperty() {
        return additional_charges_details_id;
    }

    public String getLedgerId() {
        return ledgerId.get();
    }

    public SimpleStringProperty ledgerIdProperty() {
        return ledgerId;
    }

    public String getAmt() {
        return amt.get();
    }

    public SimpleStringProperty amtProperty() {
        return amt;
    }

    public String getPercent() {
        return percent.get();
    }

    public SimpleStringProperty percentProperty() {
        return percent;
    }

    public String getActions() {
        return actions.get();
    }

    public SimpleStringProperty actionsProperty() {
        return actions;
    }

    public void setCurrent_index(String current_index) {
        this.current_index.set(current_index);
    }

    public void setSr_no(String sr_no) {
        this.sr_no.set(sr_no);
    }

    public void setAdditional_charges_details_id(String additional_charges_details_id) {
        this.additional_charges_details_id.set(additional_charges_details_id);
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId.set(ledgerId);
    }

    public void setAmt(String amt) {
        this.amt.set(amt);
    }

    public void setPercent(String percent) {
        this.percent.set(percent);
    }

    public void setActions(String actions) {
        this.actions.set(actions);
    }


    @Override
    public String toString() {
        return "AddChargesDTO{" +
                "current_index=" + current_index +
                ", sr_no=" + sr_no +
                ", additional_charges_details_id=" + additional_charges_details_id +
                ", ledgerId=" + ledgerId +
                ", particular=" + particular +
                ", amt=" + amt +
                ", percent=" + percent +
                ", actions=" + actions +
                '}';
    }
}
