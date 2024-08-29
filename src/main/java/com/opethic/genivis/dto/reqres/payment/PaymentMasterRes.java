package com.opethic.genivis.dto.reqres.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PaymentMasterRes implements Serializable {
    @SerializedName("payment_id")
    @Expose
    private Long paymentId;
    @SerializedName("payment_code")
    @Expose
    private String paymentCode;
    @SerializedName("payment_sr_no")
    @Expose
    private Long paymentSrNo;
    @SerializedName("transaction_dt")
    @Expose
    private String transactionDt;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("narrations")
    @Expose
    private String narrations;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("perticulars")
    @Expose
    private List<PaymentParticularRes> perticulars;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public Long getPaymentSrNo() {
        return paymentSrNo;
    }

    public void setPaymentSrNo(Long paymentSrNo) {
        this.paymentSrNo = paymentSrNo;
    }

    public String getTransactionDt() {
        return transactionDt;
    }

    public void setTransactionDt(String transactionDt) {
        this.transactionDt = transactionDt;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getNarrations() {
        return narrations;
    }

    public void setNarrations(String narrations) {
        this.narrations = narrations;
    }

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

    public List<PaymentParticularRes> getPerticulars() {
        return perticulars;
    }

    public void setPerticulars(List<PaymentParticularRes> perticulars) {
        this.perticulars = perticulars;
    }

    @Override
    public String toString() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("payment_id", paymentId);
        jsonObject.addProperty("payment_code", paymentCode);
        jsonObject.addProperty("payment_sr_no", paymentSrNo);
        jsonObject.addProperty("transaction_dt", transactionDt);
        jsonObject.addProperty("total_amt", totalAmt);
        jsonObject.addProperty("narrations", narrations);
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("responseStatus", responseStatus);
        jsonObject.addProperty("perticulars", perticulars.toString());
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
