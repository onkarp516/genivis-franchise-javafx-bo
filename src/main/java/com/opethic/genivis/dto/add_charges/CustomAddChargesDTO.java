package com.opethic.genivis.dto.add_charges;

public class CustomAddChargesDTO{
    private String additional_charges_details_id;
    private String ledgerId;
    private String amt;

    public CustomAddChargesDTO(String additional_charges_details_id, String ledgerId, String amt, String percent) {
        this.additional_charges_details_id = additional_charges_details_id;
        this.ledgerId = ledgerId;
        this.amt = amt;
        this.percent = percent;
    }
    public CustomAddChargesDTO() {
    }

    private String percent;

    // Getter and setter methods
    public String getAdditional_charges_details_id() {
        return additional_charges_details_id;
    }

    public void setAdditional_charges_details_id(String additional_charges_details_id) {
        this.additional_charges_details_id = additional_charges_details_id;
    }

    public String getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "{\"ledgerId\":" + ledgerId + ",\"additional_charges_details_id\":" + additional_charges_details_id +
                ",\"amt\":" + amt + ",\"percent\":" + percent + "}";
    }
}
