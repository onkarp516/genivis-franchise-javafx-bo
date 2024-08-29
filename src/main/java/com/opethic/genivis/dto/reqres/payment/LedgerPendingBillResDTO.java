package com.opethic.genivis.dto.reqres.payment;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LedgerPendingBillResDTO implements Serializable {
    @SerializedName("message")
    private String message;
    @SerializedName("responseStatus")
    private Integer responseStatus;
    @SerializedName("totalBalance")
    private Double totalBalance;
    @SerializedName("totalDnBalance")
    private Double totalDnBalance;

    @SerializedName("list")
    private List<InvoiceListResDTO> list;

    public LedgerPendingBillResDTO() {
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

    public List<InvoiceListResDTO> getList() {
        return list;
    }

    public void setList(List<InvoiceListResDTO> list) {
        this.list = list;
    }

    public Double getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }

    public Double getTotalDnBalance() {
        return totalDnBalance;
    }

    public void setTotalDnBalance(Double totalDnBalance) {
        this.totalDnBalance = totalDnBalance;
    }
}
