package com.opethic.genivis.dto.reqres.journal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JournalByIdRowDTO implements Serializable {

        @SerializedName("details_id")
        @Expose
        private Long detailsId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("ledger_type")
        @Expose
        private String ledgerType;
        @SerializedName("paid_amt")
        @Expose
        private Double paidAmt;
        @SerializedName("dr")
        @Expose
        private Double dr;
        @SerializedName("cr")
        @Expose
        private Double cr;
        @SerializedName("ledger_id")
        @Expose
        private Long ledgerId;
        @SerializedName("ledgerName")
        @Expose
        private String ledgerName;
        @SerializedName("balancingMethod")
        @Expose
        private String balancingMethod;
        @SerializedName("payableAmt")
        @Expose
        private Double payableAmt;
        @SerializedName("selectedAmt")
        @Expose
        private Double selectedAmt;
        @SerializedName("remainingAmt")
        @Expose
        private Double remainingAmt;
        @SerializedName("isAdvanceCheck")
        @Expose
        private Double isAdvanceCheck;
        @SerializedName("bills")
        @Expose
        private List<JournalByIdBillsDTO> bills;

        public Long getDetailsId() {
            return detailsId;
        }

        public void setDetailsId(Long detailsId) {
            this.detailsId = detailsId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLedgerType() {
            return ledgerType;
        }

        public void setLedgerType(String ledgerType) {
            this.ledgerType = ledgerType;
        }

        public Double getPaidAmt() {
            return paidAmt;
        }

        public void setPaidAmt(Double paidAmt) {
            this.paidAmt = paidAmt;
        }

        public Double getDr() {
            return dr;
        }

        public void setDr(Double dr) {
            this.dr = dr;
        }

        public Double getCr() {
            return cr;
        }

        public void setCr(Double cr) {
            this.cr = cr;
        }

        public Long getLedgerId() {
            return ledgerId;
        }

        public void setLedgerId(Long ledgerId) {
            this.ledgerId = ledgerId;
        }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getBalancingMethod() {
            return balancingMethod;
        }

        public void setBalancingMethod(String balancingMethod) {
            this.balancingMethod = balancingMethod;
        }

        public Double getPayableAmt() {
            return payableAmt;
        }

        public void setPayableAmt(Double payableAmt) {
            this.payableAmt = payableAmt;
        }

        public Double getSelectedAmt() {
            return selectedAmt;
        }

        public void setSelectedAmt(Double selectedAmt) {
            this.selectedAmt = selectedAmt;
        }

        public Double getRemainingAmt() {
            return remainingAmt;
        }

        public void setRemainingAmt(Double remainingAmt) {
            this.remainingAmt = remainingAmt;
        }

        public Double getIsAdvanceCheck() {
            return isAdvanceCheck;
        }

        public void setIsAdvanceCheck(Double isAdvanceCheck) {
            this.isAdvanceCheck = isAdvanceCheck;
        }

        public List<JournalByIdBillsDTO> getBills() {
            return bills;
        }

        public void setBills(List<JournalByIdBillsDTO> bills) {
            this.bills = bills;
        }

    }
