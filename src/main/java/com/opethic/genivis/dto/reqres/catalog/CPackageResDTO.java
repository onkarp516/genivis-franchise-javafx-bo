package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class CPackageResDTO {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("list")
    @Expose
    private List<CPackageListDTO> list;

    @Override
    public String toString() {
        return "CPackageResDTO{" +
                "message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", list=" + list +
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

    public List<CPackageListDTO> getList() {
        return list;
    }

    public void setList(List<CPackageListDTO> list) {
        this.list = list;
    }
}
