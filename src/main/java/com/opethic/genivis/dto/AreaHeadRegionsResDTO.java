package com.opethic.genivis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaHeadRegionsResDTO {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<AreaHeadRegionDTO> responseObject;


    @Override
    public String toString() {
        return "AreaHeadIndianStateResDTO{" +
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

    public List<AreaHeadRegionDTO> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<AreaHeadRegionDTO> responseObject) {
        this.responseObject = responseObject;
    }
}
