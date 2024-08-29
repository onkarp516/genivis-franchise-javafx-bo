package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//import com.opethic.genivis.dto.GstDetailsDTO;

import java.util.List;


public class SalesQuotToChallanResponseDTO {
    @SerializedName("discountLedgerId")
    @Expose
    private Integer discountLedgerId;
    @SerializedName("discountInAmt")
    @Expose
    private Integer discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Integer discountInPer;
    @SerializedName("totalSalesDiscountAmt")
    @Expose
    private Integer totalSalesDiscountAmt;
    @SerializedName("additional_charges")
    @Expose
    private List<Object> additionalCharges;
    @SerializedName("invoice_data")
    @Expose
    private SalesQuotToChallanInvoiceMainDTO invoiceData;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("row")
    @Expose
    private List<SalesQuotToChallanRowMainDTO> row;

    public Integer getDiscountLedgerId() {
        return discountLedgerId;
    }

    public void setDiscountLedgerId(Integer discountLedgerId) {
        this.discountLedgerId = discountLedgerId;
    }

    public Integer getDiscountInAmt() {
        return discountInAmt;
    }

    public void setDiscountInAmt(Integer discountInAmt) {
        this.discountInAmt = discountInAmt;
    }

    public Integer getDiscountInPer() {
        return discountInPer;
    }

    public void setDiscountInPer(Integer discountInPer) {
        this.discountInPer = discountInPer;
    }

    public Integer getTotalSalesDiscountAmt() {
        return totalSalesDiscountAmt;
    }

    public void setTotalSalesDiscountAmt(Integer totalSalesDiscountAmt) {
        this.totalSalesDiscountAmt = totalSalesDiscountAmt;
    }

    public List<Object> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<Object> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public SalesQuotToChallanInvoiceMainDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(SalesQuotToChallanInvoiceMainDTO invoiceData) {
        this.invoiceData = invoiceData;
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

    public List<SalesQuotToChallanRowMainDTO> getRow() {
        return row;
    }

    public void setRow(List<SalesQuotToChallanRowMainDTO> row) {
        this.row = row;
    }



}

