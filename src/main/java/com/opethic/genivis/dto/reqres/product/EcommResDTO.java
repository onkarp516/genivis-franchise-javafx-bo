package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class EcommResDTO {
    @SerializedName("response")
    @Expose
    private List<EcommResListDTO> response;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<EcommResListDTO> getResponse() {
        return response;
    }

    public void setResponse(List<EcommResListDTO> response) {
        this.response = response;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }
}
