package com.opethic.genivis.dto.counter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CSToSInvDTO {
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("row")
    @Expose
    private List<CSToSInvRowDTO> row;
    @SerializedName("ledger_id")
    @Expose
    private Long ledgerId;
    @SerializedName("responseStatus")
    @Expose
    private Long responseStatus;

    public CSToSInvDTO(String paymentMode, List<CSToSInvRowDTO> row, Long ledgerId, Long responseStatus) {
        this.paymentMode = paymentMode;
        this.row = row;
        this.ledgerId = ledgerId;
        this.responseStatus = responseStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public List<CSToSInvRowDTO> getRow() {
        return row;
    }

    public void setRow(List<CSToSInvRowDTO> row) {
        this.row = row;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Long getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Long responseStatus) {
        this.responseStatus = responseStatus;
    }
}
