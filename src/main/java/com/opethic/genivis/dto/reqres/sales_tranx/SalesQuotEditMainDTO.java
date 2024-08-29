
package com.opethic.genivis.dto.reqres.sales_tranx;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;

public class SalesQuotEditMainDTO implements Serializable {

    @SerializedName("tcs")
    @Expose
    private Double tcs;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("row")
    @Expose
    private List<Row1> row;
    @SerializedName("invoice_data")
    @Expose
    private InvoiceData invoiceData;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    @SerializedName("gstDetails")
    @Expose
    private List<GstDetailsDTO> gstDetails;

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

    public List<Row1> getRow1() {
        return row;
    }

    public void setRow1(List<Row1> row) {
        this.row = row;
    }

    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<GstDetailsDTO> getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(List<GstDetailsDTO> gstDetails) {
        this.gstDetails = gstDetails;
    }

}