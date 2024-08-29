package com.opethic.genivis.dto.reqres.receipt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ReceiptByIdMainDTO implements Serializable {
    @SerializedName("receipt_id")
    @Expose
    private Long receiptId;
    @SerializedName("receipt_no")
    @Expose
    private String receiptNo;
    @SerializedName("receipt_sr_no")
    @Expose
    private Long receiptSrNo;
    @SerializedName("tranx_date")
    @Expose
    private String tranxDate;
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
    private List<ReceiptByIdRowDTO> perticulars;

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public Long getReceiptSrNo() {
        return receiptSrNo;
    }

    public void setReceiptSrNo(Long receiptSrNo) {
        this.receiptSrNo = receiptSrNo;
    }

    public String getTranxDate() {
        return tranxDate;
    }

    public void setTranxDate(String tranxDate) {
        this.tranxDate = tranxDate;
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

    public List<ReceiptByIdRowDTO> getPerticulars() {
        return perticulars;
    }

    public void setPerticulars(List<ReceiptByIdRowDTO> perticulars) {
        this.perticulars = perticulars;
    }


    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("receipt_id", receiptId);
        jsonObject.addProperty("receipt_no", receiptNo);
        jsonObject.addProperty("receipt_sr_no", receiptSrNo);
        jsonObject.addProperty("tranx_date", tranxDate);
        jsonObject.addProperty("total_amt", totalAmt);
        jsonObject.addProperty("narrations", narrations);
        jsonObject.addProperty("message", message);
        jsonObject.addProperty("responseStatus", responseStatus);
        jsonObject.addProperty("perticulars", perticulars.toString());
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
