package com.opethic.genivis.dto.bill_format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillFormatInvoiceDataDTO {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("invoice_dt")
        @Expose
        private String invoiceDt;
        @SerializedName("invoice_no")
        @Expose
        private String invoiceNo;
        @SerializedName("state_code")
        @Expose
        private String stateCode;
        @SerializedName("state_name")
        @Expose
        private String stateName;
        @SerializedName("taxable_amt")
        @Expose
        private Double taxableAmt;
        @SerializedName("tax_amount")
        @Expose
        private Double taxAmount;
        @SerializedName("total_cgst")
        @Expose
        private Double totalCgst;
        @SerializedName("total_sgst")
        @Expose
        private Double totalSgst;
        @SerializedName("net_amount")
        @Expose
        private Double netAmount;
        @SerializedName("total_amount")
        @Expose
        private Double totalAmount;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getInvoiceDt() {
            return invoiceDt;
        }

        public void setInvoiceDt(String invoiceDt) {
            this.invoiceDt = invoiceDt;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public Double getTaxableAmt() {
            return taxableAmt;
        }

        public void setTaxableAmt(Double taxableAmt) {
            this.taxableAmt = taxableAmt;
        }

        public Double getTaxAmount() {
            return taxAmount;
        }

        public void setTaxAmount(Double taxAmount) {
            this.taxAmount = taxAmount;
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

        public Double getNetAmount() {
            return netAmount;
        }

        public void setNetAmount(Double netAmount) {
            this.netAmount = netAmount;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

    }
