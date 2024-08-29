package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CSPaymentTypeResDTO {
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bank_Id")
    @Expose
    private Long bankId;
    @SerializedName("payment_modes")
    @Expose
    private List<CSPaymentModeDTO> paymentModes;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public List<CSPaymentModeDTO> getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(List<CSPaymentModeDTO> paymentModes) {
        this.paymentModes = paymentModes;
    }

    @Override
    public String toString() {
        return "CSPaymentTypeResDTO{" +
                "bankName='" + bankName + '\'' +
                ", bankId=" + bankId +
                ", paymentModes=" + paymentModes +
                '}';
    }
}
