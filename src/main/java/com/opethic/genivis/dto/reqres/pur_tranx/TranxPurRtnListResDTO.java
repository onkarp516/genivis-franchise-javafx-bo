package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TranxPurRtnListResDTO implements Serializable {
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private TranxPurRtnDataListDTO responseObject;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public TranxPurRtnDataListDTO getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(TranxPurRtnDataListDTO responseObject) {
        this.responseObject = responseObject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
