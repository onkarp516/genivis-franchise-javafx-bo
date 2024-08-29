package com.opethic.genivis.dto.account_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PartnerPaymentDTO {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("list")
    @Expose
    private List<PartnerPaymentModel> list;

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

    public List<PartnerPaymentModel> getList() {
        return list;
    }

    public void setList(List<PartnerPaymentModel> list) {
        this.list = list;
    }
}
