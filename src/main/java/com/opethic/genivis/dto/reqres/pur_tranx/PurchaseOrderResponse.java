package com.opethic.genivis.dto.reqres.pur_tranx;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.GstDetailsDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.Row;


public class PurchaseOrderResponse {

        @SerializedName("tcs")
        @Expose
        private Double tcs;
        @SerializedName("narration")
        @Expose
        private String narration;
        @SerializedName("discountInAmt")
        @Expose
        private Double discountInAmt;
        @SerializedName("discountInPer")
        @Expose
        private Double discountInPer;
        @SerializedName("totalPurchaseDiscountAmt")
        @Expose
        private Double totalPurchaseDiscountAmt;
        @SerializedName("totalQty")
        @Expose
        private Integer totalQty;
        @SerializedName("totalFreeQty")
        @Expose
        private Double totalFreeQty;
        @SerializedName("grossTotal")
        @Expose
        private Double grossTotal;
        @SerializedName("totalTax")
        @Expose
        private Double totalTax;
        @SerializedName("gstDetails")
        @Expose
        private List<GstDetailsDTO> gstDetails;
        @SerializedName("row")
        @Expose
        private List<PurchaseOrderRow> row;
        @SerializedName("invoice_data")
        @Expose
        private PurchaseOrderInvoiceData invoiceData;
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

        public Double getTotalPurchaseDiscountAmt() {
            return totalPurchaseDiscountAmt;
        }

        public void setTotalPurchaseDiscountAmt(Double totalPurchaseDiscountAmt) {
            this.totalPurchaseDiscountAmt = totalPurchaseDiscountAmt;
        }

        public Integer getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(Integer totalQty) {
            this.totalQty = totalQty;
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

        public List<GstDetailsDTO> getGstDetails() {
            return gstDetails;
        }

        public void setGstDetails(List<GstDetailsDTO> gstDetails) {
            this.gstDetails = gstDetails;
        }

        public List<PurchaseOrderRow> getRow() {
            return row;
        }

        public void setRow(List<PurchaseOrderRow> row) {
            this.row = row;
        }

        public PurchaseOrderInvoiceData getInvoiceData() {
            return invoiceData;
        }

        public void setInvoiceData(PurchaseOrderInvoiceData invoiceData) {
            this.invoiceData = invoiceData;
        }

        public Integer getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(Integer responseStatus) {
            this.responseStatus = responseStatus;
        }

    }

