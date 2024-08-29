package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class PurOrdToChallGetByIdDTO implements Serializable {

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
    private PurOrdToChallGetByIdInvDataDTO invoiceData;
    @SerializedName("row")
    @Expose
    private List<PurConvGetByIdRowDTO> row;

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

    public PurOrdToChallGetByIdInvDataDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(PurOrdToChallGetByIdInvDataDTO invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<PurConvGetByIdRowDTO> getRow() {
        return row;
    }

    public void setRow(List<PurConvGetByIdRowDTO> row) {
        this.row = row;
    }
}
