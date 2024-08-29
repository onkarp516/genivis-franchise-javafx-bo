package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CSMultiPaymentDataDTO {

    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("bankId")
    @Expose
    private Long bankId;
    @SerializedName("payment_modes")
    @Expose
    private List<CSMultiPaymentModeDTO> paymentModes;

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

    public List<CSMultiPaymentModeDTO> getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(List<CSMultiPaymentModeDTO> paymentModes) {
        this.paymentModes = paymentModes;
    }
}
