package com.opethic.genivis.dto.reqres.creditNote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillByBillPerticulars {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("details_id")
    @Expose
    private Long detailsId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ledger_name")
    @Expose
    private String ledgerName;
    @SerializedName("balancing_method")
    @Expose
    private String balancingMethod;
    @SerializedName("payableAmt")
    @Expose
    private Double payableAmt;
    @SerializedName("remainingAmt")
    @Expose
    private Double remainingAmt;
    @SerializedName("selectedAmt")
    @Expose
    private Double selectedAmt;
    @SerializedName("isAdvanceCheck")
    @Expose
    private Boolean isAdvanceCheck;
    @SerializedName("billids")
    @Expose
    private List<BillByBillId> billids;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long detailsId) {
        this.detailsId = detailsId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getBalancingMethod() {
        return balancingMethod;
    }

    public void setBalancingMethod(String balancingMethod) {
        this.balancingMethod = balancingMethod;
    }

    public Double getPayableAmt() {
        return payableAmt;
    }

    public void setPayableAmt(Double payableAmt) {
        this.payableAmt = payableAmt;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public Double getSelectedAmt() {
        return selectedAmt;
    }

    public void setSelectedAmt(Double selectedAmt) {
        this.selectedAmt = selectedAmt;
    }

    public Boolean getAdvanceCheck() {
        return isAdvanceCheck;
    }

    public void setAdvanceCheck(Boolean advanceCheck) {
        isAdvanceCheck = advanceCheck;
    }

    public List<BillByBillId> getBillids() {
        return billids;
    }

    public void setBillids(List<BillByBillId> billids) {
        this.billids = billids;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("ledger_name", ledgerName);
        jsonObject.addProperty("balancing_method", balancingMethod);
        jsonObject.addProperty("payableAmt", payableAmt);
        jsonObject.addProperty("remainingAmt", remainingAmt);
        jsonObject.addProperty("selectedAmt", remainingAmt);
        jsonObject.addProperty("isAdvanceCheck", isAdvanceCheck);

        jsonObject.addProperty("billids",billids!=null? billids.toString():null);
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
