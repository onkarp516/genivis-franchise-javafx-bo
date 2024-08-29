package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;

import java.util.List;

public class SalesChallanResponse {
    @SerializedName("tcs")
    @Expose
    private Double tcs;
    @SerializedName("narration")
    @Expose
    private String narration;
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
    @SerializedName("totalQty")
    @Expose
    private Double totalQty;
    @SerializedName("totalFreeQty")
    @Expose
    private Double totalFreeQty;
    @SerializedName("grossTotal")
    @Expose
    private Double grossTotal;
    @SerializedName("totalTax")
    @Expose
    private Double totalTax;
    @SerializedName("additionLedger1")
    @Expose
    private Long additionLedger1;
    @SerializedName("additionLedgerAmt1")
    @Expose
    private Double additionLedgerAmt1;
    @SerializedName("additionLedger2")
    @Expose
    private Long additionLedger2;
    @SerializedName("additionLedgerAmt2")
    @Expose
    private Double additionLedgerAmt2;
    @SerializedName("additionLedger3")
    @Expose
    private Long additionLedger3;
    @SerializedName("additionLedgerAmt3")
    @Expose
    private Double additionLedgerAmt3;
    @SerializedName("additional_charges_total")
    @Expose
    private Double additionalChargesTotal;
    @SerializedName("additionalCharges")
    @Expose
    private List<Object> additionalCharges;
    @SerializedName("gstDetails")
    @Expose
    private List<GstDetailsDTO> gstDetails;
    @SerializedName("row")
    @Expose
    private List<SalesChallanRow> row;
    @SerializedName("invoice_data")
    @Expose
    private SalesChallanInvoice invoiceData;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public Long getDiscountLedgerId() {
        return discountLedgerId;
    }

    public void setDiscountLedgerId(Long discountLedgerId) {
        this.discountLedgerId = discountLedgerId;
    }

    public Double getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Double totalQty) {
        this.totalQty = totalQty;
    }

    public Long getAdditionLedger1() {
        return additionLedger1;
    }

    public void setAdditionLedger1(Long additionLedger1) {
        this.additionLedger1 = additionLedger1;
    }

    public Long getAdditionLedger2() {
        return additionLedger2;
    }

    public void setAdditionLedger2(Long additionLedger2) {
        this.additionLedger2 = additionLedger2;
    }

    public Long getAdditionLedger3() {
        return additionLedger3;
    }

    public void setAdditionLedger3(Long additionLedger3) {
        this.additionLedger3 = additionLedger3;
    }

    public Double getTcs() {
        return tcs;
    }

    public void setTcs(Double tcs) {
        this.tcs = tcs;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
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



    public Double getTotalFreeQty() {
        return totalFreeQty;
    }

    public void setTotalFreeQty(Double totalFreeQty) {
        this.totalFreeQty = totalFreeQty;
    }

    public Double getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(Double grossTotal) {
        this.grossTotal = grossTotal;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }


    public Double getAdditionLedgerAmt1() {
        return additionLedgerAmt1;
    }

    public void setAdditionLedgerAmt1(Double additionLedgerAmt1) {
        this.additionLedgerAmt1 = additionLedgerAmt1;
    }



    public Double getAdditionLedgerAmt2() {
        return additionLedgerAmt2;
    }

    public void setAdditionLedgerAmt2(Double additionLedgerAmt2) {
        this.additionLedgerAmt2 = additionLedgerAmt2;
    }


    public Double getAdditionLedgerAmt3() {
        return additionLedgerAmt3;
    }

    public void setAdditionLedgerAmt3(Double additionLedgerAmt3) {
        this.additionLedgerAmt3 = additionLedgerAmt3;
    }

    public Double getAdditionalChargesTotal() {
        return additionalChargesTotal;
    }

    public void setAdditionalChargesTotal(Double additionalChargesTotal) {
        this.additionalChargesTotal = additionalChargesTotal;
    }

    public List<Object> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<Object> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public List<GstDetailsDTO> getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(List<GstDetailsDTO> gstDetails) {
        this.gstDetails = gstDetails;
    }

    public List<SalesChallanRow> getRow() {
        return row;
    }

    public void setRow(List<SalesChallanRow> row) {
        this.row = row;
    }

    public SalesChallanInvoice getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(SalesChallanInvoice invoiceData) {
        this.invoiceData = invoiceData;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString() {
        return "SalesChallanResponse{" +
                "tcs=" + tcs +
                ", narration='" + narration + '\'' +
                ", discountLedgerId=" + discountLedgerId +
                ", discountInAmt=" + discountInAmt +
                ", discountInPer=" + discountInPer +
                ", totalSalesDiscountAmt=" + totalSalesDiscountAmt +
                ", totalQty=" + totalQty +
                ", totalFreeQty=" + totalFreeQty +
                ", grossTotal=" + grossTotal +
                ", totalTax=" + totalTax +
                ", additionLedger1=" + additionLedger1 +
                ", additionLedgerAmt1=" + additionLedgerAmt1 +
                ", additionLedger2=" + additionLedger2 +
                ", additionLedgerAmt2=" + additionLedgerAmt2 +
                ", additionLedger3=" + additionLedger3 +
                ", additionLedgerAmt3=" + additionLedgerAmt3 +
                ", additionalChargesTotal=" + additionalChargesTotal +
                ", additionalCharges=" + additionalCharges +
                ", gstDetails=" + gstDetails +
                ", row=" + row +
                ", invoiceData=" + invoiceData +
                ", responseStatus=" + responseStatus +
                '}';
    }
}

