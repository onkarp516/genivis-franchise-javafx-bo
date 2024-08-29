package com.opethic.genivis.dto.reqres.creditNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreditNoteLastRecordResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("creditnoteNo")
    @Expose
    private String creditnoteNo;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCreditnoteNo() {
        return creditnoteNo;
    }

    public void setCreditnoteNo(String creditnoteNo) {
        this.creditnoteNo = creditnoteNo;
    }
}
