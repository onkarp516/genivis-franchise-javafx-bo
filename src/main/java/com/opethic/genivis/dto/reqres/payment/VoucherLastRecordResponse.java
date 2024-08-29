package com.opethic.genivis.dto.reqres.payment;

import com.google.gson.annotations.SerializedName;

public class VoucherLastRecordResponse {

    @SerializedName("message")
    private String message;
    @SerializedName("responseStatus")
    private Integer responseStatus;
    @SerializedName("payment_sr_no")
    private Integer paymentSrNo;
    @SerializedName("payment_code")
    private String paymentCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Integer getPaymentSrNo() {
        return paymentSrNo;
    }

    public void setPaymentSrNo(Integer paymentSrNo) {
        this.paymentSrNo = paymentSrNo;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

}
