package com.opethic.genivis.dto.reqres.debitNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DebitNoteByIdMainDTO implements Serializable {

    @SerializedName("debit_no")
    @Expose
    private String debitNo;
    @SerializedName("tranx_unique_code")
    @Expose
    private String tranxUniqueCode;
    @SerializedName("debit_sr_no")
    @Expose
    private Long debitSrNo;
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
    @SerializedName("debit_details")
    @Expose
    private List<DebitNoteByIdRowDTO> debitDetails;

    public String getDebitNo() {
        return debitNo;
    }

    public void setDebitNo(String debitNo) {
        this.debitNo = debitNo;
    }

    public String getTranxUniqueCode() {
        return tranxUniqueCode;
    }

    public void setTranxUniqueCode(String tranxUniqueCode) {
        this.tranxUniqueCode = tranxUniqueCode;
    }

    public Long getDebitSrNo() {
        return debitSrNo;
    }

    public void setDebitSrNo(Long debitSrNo) {
        this.debitSrNo = debitSrNo;
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

    public List<DebitNoteByIdRowDTO> getDebitDetails() {
        return debitDetails;
    }

    public void setDebitDetails(List<DebitNoteByIdRowDTO> debitDetails) {
        this.debitDetails = debitDetails;
    }
}
