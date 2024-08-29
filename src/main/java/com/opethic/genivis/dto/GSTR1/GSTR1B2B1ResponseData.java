package com.opethic.genivis.dto.GSTR1;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GSTR1B2B1ResponseData {

    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;
    @SerializedName("total_tax")
    @Expose
    private Double totalTax;
    @SerializedName("total_igst")
    @Expose
    private Double totalIgst;
    @SerializedName("total_cgst")
    @Expose
    private Double totalCgst;
    @SerializedName("total_sgst")
    @Expose
    private Double totalSgst;
    @SerializedName("ledger_name")
    @Expose
    private String ledgerName;
    @SerializedName("ledger_id")
    @Expose
    private Integer ledgerId;
    @SerializedName("gst_number")
    @Expose
    private String gstNumber;
    @SerializedName("total_invoices")
    @Expose
    private Integer totalInvoices;

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(Double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(Double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public Double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public Integer getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Integer ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public Integer getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Integer totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
}
