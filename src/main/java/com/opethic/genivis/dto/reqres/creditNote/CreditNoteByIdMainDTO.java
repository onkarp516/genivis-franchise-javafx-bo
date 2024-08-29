package com.opethic.genivis.dto.reqres.creditNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CreditNoteByIdMainDTO implements Serializable {

    @SerializedName("credit_no")
    @Expose
    private String creditNo;
    @SerializedName("tranx_unique_code")
    @Expose
    private String tranxUniqueCode;
    @SerializedName("credit_sr_no")
    @Expose
    private Integer creditSrNo;
    @SerializedName("tranx_date")
    @Expose
    private String tranxDate;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("narrations")
    @Expose
    private String narrations;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("credit_details")
    @Expose
    private List<CreditNoteByIdRowDTO> creditDetails;

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getTranxUniqueCode() {
        return tranxUniqueCode;
    }

    public void setTranxUniqueCode(String tranxUniqueCode) {
        this.tranxUniqueCode = tranxUniqueCode;
    }

    public Integer getCreditSrNo() {
        return creditSrNo;
    }

    public void setCreditSrNo(Integer creditSrNo) {
        this.creditSrNo = creditSrNo;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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

    public List<CreditNoteByIdRowDTO> getCreditDetails() {
        return creditDetails;
    }

    public void setCreditDetails(List<CreditNoteByIdRowDTO> creditDetails) {
        this.creditDetails = creditDetails;
    }
}
