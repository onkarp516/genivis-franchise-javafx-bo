package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchaseAcRes {


    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("list")
    @Expose
    private List<PurchaseAcList> list;

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

    public List<PurchaseAcList> getList() {
        return list;
    }

    public void setList(List<PurchaseAcList> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PurchaseAcRes{" +
                "message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", list=" + list +
                '}';
    }
}
