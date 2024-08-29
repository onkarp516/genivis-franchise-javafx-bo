package com.opethic.genivis.dto.account_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GvBankDetailsDTO implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("list")
    @Expose
    private List<GVBankNameDTO> bankList;

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

    public List<GVBankNameDTO> getBankList() {
        return bankList;
    }

    public void setBankList(List<GVBankNameDTO> bankList) {
        this.bankList = bankList;
    }
}
