package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PurchaseEditResponseDTO {

    @SerializedName("tcs_mode")
    @Expose
    private String tcsMode;
    @SerializedName("tcs_amt")
    @Expose
    private Float tcsAmt;
    @SerializedName("tcs_per")
    @Expose
    private Float tcsPer;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("discountLedgerId")
    @Expose
    private Integer discountLedgerId;
    @SerializedName("discountInAmt")
    @Expose
    private Float discountInAmt;
    @SerializedName("discountInPer")
    @Expose
    private Float discountInPer;
    @SerializedName("totalPurchaseDiscountAmt")
    @Expose
    private Float totalPurchaseDiscountAmt;
    @SerializedName("totalQty")
    @Expose
    private Integer totalQty;
    @SerializedName("totalFreeQty")
    @Expose
    private Float totalFreeQty;
    @SerializedName("grossTotal")
    @Expose
    private Float grossTotal;
    @SerializedName("totalTax")
    @Expose
    private Float totalTax;
    @SerializedName("additionLedger1")
    @Expose
    private Integer additionLedger1;
    @SerializedName("additionLedgerAmt1")
    @Expose
    private Float additionLedgerAmt1;
    @SerializedName("additionLedger2")
    @Expose
    private Integer additionLedger2;
    @SerializedName("additionLedgerAmt2")
    @Expose
    private Float additionLedgerAmt2;
    @SerializedName("additionLedger3")
    @Expose
    private Integer additionLedger3;
    @SerializedName("additionLedgerAmt3")
    @Expose
    private Float additionLedgerAmt3;
    @SerializedName("debitNoteReference")
    @Expose
    private Boolean debitNoteReference;
    @SerializedName("row")
    @Expose
    private List<RowListDTO> row;
    @SerializedName("invoice_data")
    @Expose
    private InvoiceDataResDTO invoiceData;
    @SerializedName("bills")
    @Expose
    private List<Object> bills;
    @SerializedName("barcode_list")
    @Expose
    private List<BarcodeListDTO> barcodeList;
    @SerializedName("additionalCharges")
    @Expose
    private List<AddChargesListDTO> additionalCharges;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public String getTcsMode() {
        return tcsMode;
    }

    public void setTcsMode(String tcsMode) {
        this.tcsMode = tcsMode;
    }

    public Float getTcsAmt() {
        return tcsAmt;
    }

    public void setTcsAmt(Float tcsAmt) {
        this.tcsAmt = tcsAmt;
    }

    public Float getTcsPer() {
        return tcsPer;
    }

    public void setTcsPer(Float tcsPer) {
        this.tcsPer = tcsPer;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getDiscountLedgerId() {
        return discountLedgerId;
    }

    public void setDiscountLedgerId(Integer discountLedgerId) {
        this.discountLedgerId = discountLedgerId;
    }

    public Float getDiscountInAmt() {
        return discountInAmt;
    }

    public void setDiscountInAmt(Float discountInAmt) {
        this.discountInAmt = discountInAmt;
    }

    public Float getDiscountInPer() {
        return discountInPer;
    }

    public void setDiscountInPer(Float discountInPer) {
        this.discountInPer = discountInPer;
    }

    public Float getTotalPurchaseDiscountAmt() {
        return totalPurchaseDiscountAmt;
    }

    public void setTotalPurchaseDiscountAmt(Float totalPurchaseDiscountAmt) {
        this.totalPurchaseDiscountAmt = totalPurchaseDiscountAmt;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Float getTotalFreeQty() {
        return totalFreeQty;
    }

    public void setTotalFreeQty(Float totalFreeQty) {
        this.totalFreeQty = totalFreeQty;
    }

    public Float getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(Float grossTotal) {
        this.grossTotal = grossTotal;
    }

    public Float getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Float totalTax) {
        this.totalTax = totalTax;
    }

    public Integer getAdditionLedger1() {
        return additionLedger1;
    }

    public void setAdditionLedger1(Integer additionLedger1) {
        this.additionLedger1 = additionLedger1;
    }

    public Float getAdditionLedgerAmt1() {
        return additionLedgerAmt1;
    }

    public void setAdditionLedgerAmt1(Float additionLedgerAmt1) {
        this.additionLedgerAmt1 = additionLedgerAmt1;
    }

    public Integer getAdditionLedger2() {
        return additionLedger2;
    }

    public void setAdditionLedger2(Integer additionLedger2) {
        this.additionLedger2 = additionLedger2;
    }

    public Float getAdditionLedgerAmt2() {
        return additionLedgerAmt2;
    }

    public void setAdditionLedgerAmt2(Float additionLedgerAmt2) {
        this.additionLedgerAmt2 = additionLedgerAmt2;
    }

    public Integer getAdditionLedger3() {
        return additionLedger3;
    }

    public void setAdditionLedger3(Integer additionLedger3) {
        this.additionLedger3 = additionLedger3;
    }

    public Float getAdditionLedgerAmt3() {
        return additionLedgerAmt3;
    }

    public void setAdditionLedgerAmt3(Float additionLedgerAmt3) {
        this.additionLedgerAmt3 = additionLedgerAmt3;
    }

    public Boolean getDebitNoteReference() {
        return debitNoteReference;
    }

    public void setDebitNoteReference(Boolean debitNoteReference) {
        this.debitNoteReference = debitNoteReference;
    }

    public List<RowListDTO> getRow() {
        return row;
    }

    public void setRow(List<RowListDTO> row) {
        this.row = row;
    }

    public InvoiceDataResDTO getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceDataResDTO invoiceData) {
        this.invoiceData = invoiceData;
    }

    public List<Object> getBills() {
        return bills;
    }

    public void setBills(List<Object> bills) {
        this.bills = bills;
    }

    public List<BarcodeListDTO> getBarcodeList() {
        return barcodeList;
    }

    public void setBarcodeList(List<BarcodeListDTO> barcodeList) {
        this.barcodeList = barcodeList;
    }

    public List<AddChargesListDTO> getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(List<AddChargesListDTO> additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }


    @Override
    public String toString() {
        return "PurchaseEditResponseDTO{" +
                "tcsMode='" + tcsMode + '\'' +
                ", tcsAmt=" + tcsAmt +
                ", tcsPer=" + tcsPer +
                ", narration='" + narration + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", discountLedgerId=" + discountLedgerId +
                ", discountInAmt=" + discountInAmt +
                ", discountInPer=" + discountInPer +
                ", totalPurchaseDiscountAmt=" + totalPurchaseDiscountAmt +
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
                ", debitNoteReference=" + debitNoteReference +
                ", row=" + row +
                ", invoiceData=" + invoiceData +
                ", bills=" + bills +
                ", barcodeList=" + barcodeList +
                ", additionalCharges=" + additionalCharges +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
