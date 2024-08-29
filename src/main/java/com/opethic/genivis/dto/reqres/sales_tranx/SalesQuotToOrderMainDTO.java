package com.opethic.genivis.dto.reqres.sales_tranx;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;

public class SalesQuotToOrderMainDTO {


    @SerializedName("gstDetails")
    @Expose
    private List<GstDetailsDTO> gstDetails;
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
    private InvoiceData invoiceData;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("row")
    @Expose
    private List<Row> row;

    public List<GstDetailsDTO> getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(List<GstDetailsDTO> gstDetails) {
        this.gstDetails = gstDetails;
    }

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

    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceData invoiceData) {
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

    public List<Row> getRow() {
        return row;
    }

    public void setRow(List<Row> row) {
        this.row = row;
    }
}
