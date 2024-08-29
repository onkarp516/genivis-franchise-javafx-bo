package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TranxPurInvoiceResEditDTO implements Serializable {
        @SerializedName("id")
        @Expose
        private Long id;
        @SerializedName("discountLedgerId")
        @Expose
        private Long discountLedgerId;
        @SerializedName("discountInAmt")
        @Expose
        private Double discountInAmt;
        @SerializedName("discountInPer")
        @Expose
        private Double discountInPer;
        @SerializedName("grossTotal")
        @Expose
        private Double grossTotal;
        @SerializedName("narration")
        @Expose
        private String narration;
        @SerializedName("paymentMode")
        @Expose
        private String paymentMode;
        @SerializedName("tcs_amt")
        @Expose
        private Double tcs_amt;
        @SerializedName("tcs_mode")
        @Expose
        private String tcs_mode;
        @SerializedName("tcs_per")
        @Expose
        private Double tcs_per;
        @SerializedName("totalAmount")
        @Expose
        private Double totalAmount;
        @SerializedName("totalFreeQty")
        @Expose
        private Double totalFreeQty;
        @SerializedName("totalPurchaseDiscountAmt")
        @Expose
        private Double totalPurchaseDiscountAmt;
        @SerializedName("totalQty")
        @Expose
        private Double totalQty;
        @SerializedName("totalTax")
        @Expose
        private Double totalTax;
        @SerializedName("additional_charges_total")
        @Expose
        private Double additionalTotalCharges;
        @SerializedName("invoice_dt")
        @Expose
        private String invoiceDate;
        @SerializedName("invoice_no")
        @Expose
        private String invoiceNo;
        @SerializedName("isRoundOffCheck")
        @Expose
        private Boolean isroundOff;

        @SerializedName("responseStatus")
        @Expose
        private Integer responseStatus;
        @SerializedName("purchase_account_ledger_id")
        @Expose
        private Long purchaseAcctId;

        public Integer getResponseStatus() {
                return responseStatus;
        }

        public void setResponseStatus(Integer responseStatus) {
                this.responseStatus = responseStatus;
        }

        @SerializedName("purchase_sr_no")
        @Expose
        private Long purchaseSrNo;
        @SerializedName("roundoff")
        @Expose
        private Double roundoffAmt;
        @SerializedName("supplierId")
        @Expose
        private Long supplierId;
        @SerializedName("transaction_dt")
        @Expose
        private String transactionDate;
        @SerializedName("tranx_unique_code")
        @Expose
        private String tranxUniqueCode;
        @SerializedName("total_row_gross_amt")
        @Expose
        private Double totalBaseAmt;

        public TranxPurInvoiceResEditDTO() {
        }

        public Double getTotalBaseAmt() {
                return totalBaseAmt;
        }

        public void setTotalBaseAmt(Double totalBaseAmt) {
                this.totalBaseAmt = totalBaseAmt;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getDiscountLedgerId() {
                return discountLedgerId;
        }

        public void setDiscountLedgerId(Long discountLedgerId) {
                this.discountLedgerId = discountLedgerId;
        }

        public Double getGrossTotal() {
                return grossTotal;
        }

        public void setGrossTotal(Double grossTotal) {
                this.grossTotal = grossTotal;
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

        public Double getTcs_amt() {
                return tcs_amt;
        }

        public void setTcs_amt(Double tcs_amt) {
                this.tcs_amt = tcs_amt;
        }

        public String getTcs_mode() {
                return tcs_mode;
        }

        public void setTcs_mode(String tcs_mode) {
                this.tcs_mode = tcs_mode;
        }

        public Double getTcs_per() {
                return tcs_per;
        }

        public void setTcs_per(Double tcs_per) {
                this.tcs_per = tcs_per;
        }

        public Double getTotalAmount() {
                return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
                this.totalAmount = totalAmount;
        }

        public Double getTotalFreeQty() {
                return totalFreeQty;
        }

        public void setTotalFreeQty(Double totalFreeQty) {
                this.totalFreeQty = totalFreeQty;
        }

        public Double getTotalPurchaseDiscountAmt() {
                return totalPurchaseDiscountAmt;
        }

        public void setTotalPurchaseDiscountAmt(Double totalPurchaseDiscountAmt) {
                this.totalPurchaseDiscountAmt = totalPurchaseDiscountAmt;
        }

        public Double getTotalQty() {
                return totalQty;
        }

        public void setTotalQty(Double totalQty) {
                this.totalQty = totalQty;
        }

        public Double getTotalTax() {
                return totalTax;
        }

        public void setTotalTax(Double totalTax) {
                this.totalTax = totalTax;
        }

        public Double getAdditionalTotalCharges() {
                return additionalTotalCharges;
        }

        public void setAdditionalTotalCharges(Double additionalTotalCharges) {
                this.additionalTotalCharges = additionalTotalCharges;
        }

        public String getInvoiceDate() {
                return invoiceDate;
        }

        public void setInvoiceDate(String invoiceDate) {
                this.invoiceDate = invoiceDate;
        }

        public String getInvoiceNo() {
                return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
                this.invoiceNo = invoiceNo;
        }

        public Long getSupplierId() {
                return supplierId;
        }

        public void setSupplierId(Long supplierId) {
                this.supplierId = supplierId;
        }

        public String getTransactionDate() {
                return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
                this.transactionDate = transactionDate;
        }

        public String getTranxUniqueCode() {
                return tranxUniqueCode;
        }

        public void setTranxUniqueCode(String tranxUniqueCode) {
                this.tranxUniqueCode = tranxUniqueCode;
        }


        public Long getPurchaseAcctId() {
                return purchaseAcctId;
        }

        public void setPurchaseAcctId(Long purchaseAcctId) {
                this.purchaseAcctId = purchaseAcctId;
        }

        public Long getPurchaseSrNo() {
                return purchaseSrNo;
        }

        public void setPurchaseSrNo(Long purchaseSrNo) {
                this.purchaseSrNo = purchaseSrNo;
        }

        public Boolean getIsroundOff() {
                return isroundOff;
        }

        public void setIsroundOff(Boolean isroundOff) {
                this.isroundOff = isroundOff;
        }

        public Double getRoundoffAmt() {
                return roundoffAmt;
        }

        public void setRoundoffAmt(Double roundoffAmt) {
                this.roundoffAmt = roundoffAmt;
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
}