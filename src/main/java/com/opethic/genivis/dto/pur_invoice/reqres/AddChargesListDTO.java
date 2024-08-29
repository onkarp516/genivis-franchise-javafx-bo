package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddChargesListDTO {

    @SerializedName("additional_charges_details_id")
    @Expose
    private Integer additionalChargesDetailsId;
    @SerializedName("ledgerId")
    @Expose
    private Integer ledgerId;
    @SerializedName("amt")
    @Expose
    private Float amt;
    @SerializedName("percent")
    @Expose
    private Float percent;

    public Integer getAdditionalChargesDetailsId() {
        return additionalChargesDetailsId;
    }

    public void setAdditionalChargesDetailsId(Integer additionalChargesDetailsId) {
        this.additionalChargesDetailsId = additionalChargesDetailsId;
    }

    public Integer getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Integer ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Float getAmt() {
        return amt;
    }

    public void setAmt(Float amt) {
        this.amt = amt;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }


    @Override
    public String toString() {
        return "AddChargesListDTO{" +
                "additionalChargesDetailsId=" + additionalChargesDetailsId +
                ", ledgerId=" + ledgerId +
                ", amt=" + amt +
                ", percent=" + percent +
                '}';
    }
}
