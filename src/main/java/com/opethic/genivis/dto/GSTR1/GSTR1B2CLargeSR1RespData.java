package com.opethic.genivis.dto.GSTR1;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GSTR1B2CLargeSR1RespData implements Serializable {

    @SerializedName("sr_no")
    @Expose
    private Integer srNo;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("state_code")
    @Expose
    private String stateCode;
    @SerializedName("rate_of_tax")
    @Expose
    private String rateOfTax;
    @SerializedName("taxable_amt")
    @Expose
    private Double taxableAmt;
    @SerializedName("igst_amt")
    @Expose
    private Double igstAmt;
    @SerializedName("sgst_amt")
    @Expose
    private Double sgstAmt;
    @SerializedName("cgst_amt")
    @Expose
    private Double cgstAmt;
    @SerializedName("tax_amt")
    @Expose
    private Double taxAmt;
    @SerializedName("invoice_amt")
    @Expose
    private Double invoiceAmt;

    public Integer getSrNo() {
        return srNo;
    }

    public void setSrNo(Integer srNo) {
        this.srNo = srNo;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateCode = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getRateOfTax() {
        return rateOfTax;
    }

    public void setRateOfTax(String rateOfTax) {
        this.rateOfTax = rateOfTax;
    }

    public Double getTaxableAmt() {
        return taxableAmt;
    }

    public void setTaxableAmt(Double taxableAmt) {
        this.taxableAmt = taxableAmt;
    }

    public Double getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(Double igstAmt) {
        this.igstAmt = igstAmt;
    }

    public Double getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(Double sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public Double getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(Double cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public Double getTaxAmt() {
        return taxAmt;
    }

    public void setTaxAmt(Double taxAmt) {
        this.taxAmt = taxAmt;
    }

    public Double getInvoiceAmt() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(Double invoiceAmt) {
        this.invoiceAmt = invoiceAmt;
    }
}
