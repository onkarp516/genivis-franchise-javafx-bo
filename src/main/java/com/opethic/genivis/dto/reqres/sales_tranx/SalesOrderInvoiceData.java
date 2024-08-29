package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
    public class SalesOrderInvoiceData {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sales_sr_no")
        @Expose
        private Integer salesSrNo;
        @SerializedName("sales_account_id")
        @Expose
        private Integer salesAccountId;
        @SerializedName("sales_account")
        @Expose
        private String salesAccount;
        @SerializedName("supplierGST")
        @Expose
        private Object supplierGST;
        @SerializedName("transaction_dt")
        @Expose
        private String transactionDt;
        @SerializedName("bill_date")
        @Expose
        private String billDate;
        @SerializedName("so_bill_no")
        @Expose
        private String soBillNo;
        @SerializedName("tranx_unique_code")
        @Expose
        private String tranxUniqueCode;
        @SerializedName("round_off")
        @Expose
        private Double roundOff;
        @SerializedName("total_base_amount")
        @Expose
        private Double totalBaseAmount;
        @SerializedName("total_amount")
        @Expose
        private Double totalAmount;
        @SerializedName("total_cgst")
        @Expose
        private Double totalCgst;
        @SerializedName("total_sgst")
        @Expose
        private Double totalSgst;
        @SerializedName("total_igst")
        @Expose
        private Double totalIgst;
        @SerializedName("total_qty")
        @Expose
        private Integer totalQty;
        @SerializedName("taxable_amount")
        @Expose
        private Double taxableAmount;
        @SerializedName("tcs")
        @Expose
        private Double tcs;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("financial_year")
        @Expose
        private String financialYear;
        @SerializedName("debtor_id")
        @Expose
        private Integer debtorId;
        @SerializedName("debtor_name")
        @Expose
        private String debtorName;
        @SerializedName("narration")
        @Expose
        private String narration;
        @SerializedName("gstNo")
        @Expose
        private String gstNo;

        //new
        @SerializedName("ledgerStateCode")
        @Expose
        private String ledgerStateCode;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getSalesSrNo() {
            return salesSrNo;
        }

        public void setSalesSrNo(Integer salesSrNo) {
            this.salesSrNo = salesSrNo;
        }

        public Integer getSalesAccountId() {
            return salesAccountId;
        }

        public void setSalesAccountId(Integer salesAccountId) {
            this.salesAccountId = salesAccountId;
        }

        public String getSalesAccount() {
            return salesAccount;
        }

        public void setSalesAccount(String salesAccount) {
            this.salesAccount = salesAccount;
        }

        public Object getSupplierGST() {
            return supplierGST;
        }

        public void setSupplierGST(Object supplierGST) {
            this.supplierGST = supplierGST;
        }

        public String getTransactionDt() {
            return transactionDt;
        }

        public void setTransactionDt(String transactionDt) {
            this.transactionDt = transactionDt;
        }

        public String getBillDate() {
            return billDate;
        }

        public void setBillDate(String billDate) {
            this.billDate = billDate;
        }

        public String getSoBillNo() {
            return soBillNo;
        }

        public void setSoBillNo(String soBillNo) {
            this.soBillNo = soBillNo;
        }

        public String getTranxUniqueCode() {
            return tranxUniqueCode;
        }

        public void setTranxUniqueCode(String tranxUniqueCode) {
            this.tranxUniqueCode = tranxUniqueCode;
        }

        public Double getRoundOff() {
            return roundOff;
        }

        public void setRoundOff(Double roundOff) {
            this.roundOff = roundOff;
        }

        public Double getTotalBaseAmount() {
            return totalBaseAmount;
        }

        public void setTotalBaseAmount(Double totalBaseAmount) {
            this.totalBaseAmount = totalBaseAmount;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
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

        public Double getTotalIgst() {
            return totalIgst;
        }

        public void setTotalIgst(Double totalIgst) {
            this.totalIgst = totalIgst;
        }

        public Integer getTotalQty() {
            return totalQty;
        }

        public void setTotalQty(Integer totalQty) {
            this.totalQty = totalQty;
        }

        public Double getTaxableAmount() {
            return taxableAmount;
        }

        public void setTaxableAmount(Double taxableAmount) {
            this.taxableAmount = taxableAmount;
        }

        public Double getTcs() {
            return tcs;
        }

        public void setTcs(Double tcs) {
            this.tcs = tcs;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public String getFinancialYear() {
            return financialYear;
        }

        public void setFinancialYear(String financialYear) {
            this.financialYear = financialYear;
        }

        public Integer getDebtorId() {
            return debtorId;
        }

        public void setDebtorId(Integer debtorId) {
            this.debtorId = debtorId;
        }

        public String getDebtorName() {
            return debtorName;
        }

        public void setDebtorName(String debtorName) {
            this.debtorName = debtorName;
        }

        public String getNarration() {
            return narration;
        }

        public void setNarration(String narration) {
            this.narration = narration;
        }

        public String getGstNo() {
            return gstNo;
        }

        public void setGstNo(String gstNo) {
            this.gstNo = gstNo;
        }
        //new
        public String getLedgerStateCode() {
            return ledgerStateCode;
        }

        public void setLedgerStateCode(String ledgerStateCode) {
            this.ledgerStateCode = ledgerStateCode;
        }
    }
