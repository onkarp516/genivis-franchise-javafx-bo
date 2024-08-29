package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurOrdToChallMainDTO {

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
    private PurOrdToChallIvoiceData invoiceData;
    @SerializedName("row")
    @Expose
    private List<PurOrdToChallRowDTO> row;

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

    public PurOrdToChallIvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(PurOrdToChallIvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<PurOrdToChallRowDTO> getRow() {
        return row;
    }

    public void setRow(List<PurOrdToChallRowDTO> row) {
        this.row = row;
    }
}
