package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseOrderInvoiceData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("invoice_dt")
        @Expose
        private String invoiceDt;
        @SerializedName("invoice_no")
        @Expose
        private String invoiceNo;
        @SerializedName("tranx_unique_code")
        @Expose
        private String tranxUniqueCode;
        @SerializedName("po_sr_no")
        @Expose
        private Integer poSrNo;
        @SerializedName("purchase_account_ledger_id")
        @Expose
        private Integer purchaseAccountLedgerId;
        @SerializedName("supplierId")
        @Expose
        private Integer supplierId;
        @SerializedName("supplierName")
        @Expose
        private String supplierName;
        @SerializedName("transaction_dt")
        @Expose
        private String transactionDt;
        @SerializedName("gstNo")
        @Expose
        private String gstNo;

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

        public String getTranxUniqueCode() {
            return tranxUniqueCode;
        }

        public void setTranxUniqueCode(String tranxUniqueCode) {
            this.tranxUniqueCode = tranxUniqueCode;
        }

        public Integer getPoSrNo() {
            return poSrNo;
        }

        public void setPoSrNo(Integer poSrNo) {
            this.poSrNo = poSrNo;
        }

        public Integer getPurchaseAccountLedgerId() {
            return purchaseAccountLedgerId;
        }

        public void setPurchaseAccountLedgerId(Integer purchaseAccountLedgerId) {
            this.purchaseAccountLedgerId = purchaseAccountLedgerId;
        }

        public Integer getSupplierId() {
            return supplierId;
        }
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

        public void setSupplierId(Integer supplierId) {
            this.supplierId = supplierId;
        }

        public String getTransactionDt() {
            return transactionDt;
        }

        public void setTransactionDt(String transactionDt) {
            this.transactionDt = transactionDt;
        }

        public String getGstNo() {
            return gstNo;
        }

        public void setGstNo(String gstNo) {
            this.gstNo = gstNo;
        }

    }

