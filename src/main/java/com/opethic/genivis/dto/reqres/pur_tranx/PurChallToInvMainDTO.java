package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurChallToInvMainDTO {

    @SerializedName("discountLedgerId")
    @Expose
    private Long discountLedgerId;
    @SerializedName("discountInAmt")
    @Expose
    private Double discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Double discountInPer;
    @SerializedName("totalPurchaseDiscountAmt")
    @Expose
    private Double totalPurchaseDiscountAmt;
    @SerializedName("additional_charges")
    @Expose
    private List<AdditionalCharge> additionalCharges;
    @SerializedName("reference_type")
    @Expose
    private String referenceType;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("invoice_data")
    @Expose
    private PurChallToInvInvoiceDataDTO invoiceData;
    @SerializedName("row")
    @Expose
    private List<TranxPurRowResEditDTO> row;

    public PurChallToInvMainDTO() {
    }

    public Long getDiscountLedgerId() {
        return discountLedgerId;
    }

    public void setDiscountLedgerId(Long discountLedgerId) {
        this.discountLedgerId = discountLedgerId;
    }

    public Double getDiscountInAmt() {
        return discountInAmt;
    }

    public void setDiscountInAmt(Double discountInAmt) {
        this.discountInAmt = discountInAmt;
    }

    public Double getDiscountInPer() {
        return discountInPer;
    }

    public void setDiscountInPer(Double discountInPer) {
        this.discountInPer = discountInPer;
    }

    public Double getTotalPurchaseDiscountAmt() {
        return totalPurchaseDiscountAmt;
    }

    public void setTotalPurchaseDiscountAmt(Double totalPurchaseDiscountAmt) {
        this.totalPurchaseDiscountAmt = totalPurchaseDiscountAmt;
    }

    public List<AdditionalCharge> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<AdditionalCharge> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
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

    public PurChallToInvInvoiceDataDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(PurChallToInvInvoiceDataDTO invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<TranxPurRowResEditDTO> getRow() {
        return row;
    }

    public void setRow(List<TranxPurRowResEditDTO> row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return "PurChallToInvMainDTO{" +
                "discountLedgerId=" + discountLedgerId +
                ", discountInAmt=" + discountInAmt +
                ", discountInPer=" + discountInPer +
                ", totalPurchaseDiscountAmt=" + totalPurchaseDiscountAmt +
                ", additionalCharges=" + additionalCharges +
                ", referenceType='" + referenceType + '\'' +
                ", message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", invoiceData=" + invoiceData +
                ", row=" + row +
                '}';
    }
}
