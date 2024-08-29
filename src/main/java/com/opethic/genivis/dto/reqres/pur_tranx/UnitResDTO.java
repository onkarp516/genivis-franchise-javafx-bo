package com.opethic.genivis.dto.reqres.pur_tranx;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UnitResDTO
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private ResponseObject responseObject;


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

    public ResponseObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(ResponseObject responseObject) {
        this.responseObject = responseObject;
    }
}