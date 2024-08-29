package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ContentPackingResDTO {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<ContentPackingListDTO> responseObject;

    @Override
    public String toString() {
        return "ContentPackingResDTO{" +
                "message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseObject=" + responseObject +
                '}';
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

    public List<ContentPackingListDTO> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<ContentPackingListDTO> responseObject) {
        this.responseObject = responseObject;
    }
}
