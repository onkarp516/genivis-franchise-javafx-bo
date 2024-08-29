package com.opethic.genivis.dto.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMsg {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private Integer responseObject;
    @SerializedName("data")
    @Expose
    private String data;

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

    public Integer getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Integer responseObject) {
        this.responseObject = responseObject;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseMsg{" +
                "message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseObject=" + responseObject +
                ", data='" + data + '\'' +
                '}';
    }
}
