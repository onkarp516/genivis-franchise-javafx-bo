package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdditionalCharge {
    @SerializedName("additional_charges_details_id")
    @Expose
    private Long additionalChargesDetailsId;
    @SerializedName("ledgerId")
    @Expose
    private Long ledgerId;
    @SerializedName("amt")
    @Expose
    private Double amt;
    @SerializedName("percent")
    @Expose
    private Double percent;

    public Long getAdditionalChargesDetailsId() {
        return additionalChargesDetailsId;
    }

    public void setAdditionalChargesDetailsId(Long additionalChargesDetailsId) {
        this.additionalChargesDetailsId = additionalChargesDetailsId;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }


}
