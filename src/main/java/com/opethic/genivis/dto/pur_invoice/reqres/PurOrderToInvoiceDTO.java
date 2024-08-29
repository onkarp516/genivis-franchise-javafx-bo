package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.pur_invoice.PurchaseOrderToInvoiceRowListDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.Row;
import com.opethic.genivis.dto.reqres.sales_tranx.Row1;

import java.io.Serializable;
import java.util.List;

public class PurOrderToInvoiceDTO implements Serializable {
    @SerializedName("discountLedgerId")
    @Expose
    private Integer discountLedgerId;
    @SerializedName("discountInAmt")
    @Expose
    private Integer discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Integer discountInPer;
    @SerializedName("totalPurchaseDiscountAmt")
    @Expose
    private Integer totalPurchaseDiscountAmt;
    @SerializedName("additional_charges")
    @Expose
    private List<Object> additionalCharges;
    @SerializedName("reference_type")
    @Expose
    private String referenceType;
    @SerializedName("reference_id")
    @Expose
    private String referenceId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("invoice_data")
    @Expose
    private InvoiceDataPurToInvDTO invoiceData;
    @SerializedName("row")
    @Expose
    private List<PurchaseOrderToInvoiceRowListDTO> row;

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

    public Integer getTotalPurchaseDiscountAmt() {
        return totalPurchaseDiscountAmt;
    }

    public void setTotalPurchaseDiscountAmt(Integer totalPurchaseDiscountAmt) {
        this.totalPurchaseDiscountAmt = totalPurchaseDiscountAmt;
    }

    public List<Object> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<Object> additionalCharges) {
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

    public InvoiceDataPurToInvDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceDataPurToInvDTO invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<PurchaseOrderToInvoiceRowListDTO> getRow() {
        return row;
    }

    public void setRow(List<PurchaseOrderToInvoiceRowListDTO> row) {
        this.row = row;
    }


    @Override
    public String toString() {
        return "PurOrderToInvoiceDTO{" +
                "discountLedgerId=" + discountLedgerId +
                ", discountInAmt=" + discountInAmt +
                ", discountInPer=" + discountInPer +
                ", totalPurchaseDiscountAmt=" + totalPurchaseDiscountAmt +
                ", additionalCharges=" + additionalCharges +
                ", referenceType='" + referenceType + '\'' +
                ", referenceId='" + referenceId + '\'' +
                ", message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", invoiceData=" + invoiceData +
                ", row=" + row +
                '}';
    }
}

