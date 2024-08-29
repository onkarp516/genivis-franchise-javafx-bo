package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;

import java.util.List;

public class SalesOrderResponse {

    @SerializedName("tcs")
    @Expose
    private Double tcs;
    @SerializedName("narration")
    @Expose
    private String narration;
    @SerializedName("gstDetails")
    @Expose
    private List<GstDetailsDTO> gstDetails;
    @SerializedName("row")
    @Expose
    private List<SalesOrderRow> row;
    @SerializedName("invoice_data")
    @Expose
    private SalesOrderInvoiceData invoiceData;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

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

    public List<GstDetailsDTO> getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(List<GstDetailsDTO> gstDetails) {
        this.gstDetails = gstDetails;
    }

    public List<SalesOrderRow> getRow() {
        return row;
    }

    public void setRow(List<SalesOrderRow> row) {
        this.row = row;
    }

    public SalesOrderInvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(SalesOrderInvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    }
