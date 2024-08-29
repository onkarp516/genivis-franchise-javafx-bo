package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CounterResDTO {

    @SerializedName("response")
    @Expose
    private Response response;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }











}
