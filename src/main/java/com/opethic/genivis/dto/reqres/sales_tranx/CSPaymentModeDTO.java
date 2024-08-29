package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CSPaymentModeDTO {

    @SerializedName("details_id")
    @Expose
    private Long detailsId;
    @SerializedName("modeId")
    @Expose
    private Long modeId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("refId")
    @Expose
    private String refId;
    @SerializedName("custBank")
    @Expose
    private String custBank;

    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long detailsId) {
        this.detailsId = detailsId;
    }

    public Long getModeId() {
        return modeId;
    }

    public void setModeId(Long modeId) {
        this.modeId = modeId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCustBank() {
        return custBank;
    }

    public void setCustBank(String custBank) {
        this.custBank = custBank;
    }

    @Override
    public String toString() {
        return "CSPaymentModeDTO{" +
                "detailsId=" + detailsId +
                ", modeId=" + modeId +
                ", label='" + label + '\'' +
                ", amount=" + amount +
                ", refId='" + refId + '\'' +
                ", custBank='" + custBank + '\'' +
                '}';
    }
}
