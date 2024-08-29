package com.opethic.genivis.dto.reqres.sales_tranx;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConsumerResDTO {

    @SerializedName("response")
    @Expose
    private ConsumerResponse response;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public ConsumerResponse getResponse() {
        return response;
    }

    public void setResponse(ConsumerResponse response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

}