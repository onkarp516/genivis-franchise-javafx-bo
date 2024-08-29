package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;
import java.io.Serializable;
import java.util.List;

public class SaleOrderToChallanResponse implements Serializable {//main DTO for Sale order to challan conversion
    @SerializedName("discountLedgerId")
    @Expose
    private Long discountLedgerId;
    @SerializedName("discountInAmt")
    @Expose
    private Double discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Double discountInPer;
    @SerializedName("totalSalesDiscountAmt")
    @Expose
    private Double totalSalesDiscountAmt;
    @SerializedName("additional_charges")
    @Expose
    private List<Object> additionalCharges;
    @SerializedName("invoice_data")
    @Expose
    private SaleOrderToChallanInvoice invoiceData;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Long responseStatus;
    @SerializedName("row")
    @Expose
    private List<SaleOrderToChallanRow> row;

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

    public Double getTotalSalesDiscountAmt() {
        return totalSalesDiscountAmt;
    }

    public void setTotalSalesDiscountAmt(Double totalSalesDiscountAmt) {
        this.totalSalesDiscountAmt = totalSalesDiscountAmt;
    }

    public List<Object> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<Object> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public SaleOrderToChallanInvoice getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(SaleOrderToChallanInvoice invoiceData) {
        this.invoiceData = invoiceData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Long responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<SaleOrderToChallanRow> getRow() {
        return row;
    }

    public void setRow(List<SaleOrderToChallanRow> row) {
        this.row = row;
    }
}
