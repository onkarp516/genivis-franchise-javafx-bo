package com.opethic.genivis.dto.reqres.journal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class JournalByIdMainDTO implements Serializable {

        @SerializedName("journal_id")
        @Expose
        private Long journalId;
        @SerializedName("journal_no")
        @Expose
        private String journalNo;
        @SerializedName("journal_sr_no")
        @Expose
        private String journalSrNo;
        @SerializedName("tranx_date")
        @Expose
        private String tranxDate;
        @SerializedName("total_amt")
        @Expose
        private Double totalAmt;
        @SerializedName("narrations")
        @Expose
        private String narrations;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("responseStatus")
        @Expose
        private Long responseStatus;
        @SerializedName("perticulars")
        @Expose
        private List<JournalByIdRowDTO> perticulars;

        public Long getJournalId() {
            return journalId;
        }

        public void setJournalId(Long journalId) {
            this.journalId = journalId;
        }

        public String getJournalNo() {
            return journalNo;
        }

        public void setJournalNo(String journalNo) {
            this.journalNo = journalNo;
        }

        public String getJournalSrNo() {
            return journalSrNo;
        }

        public void setJournalSrNo(String journalSrNo) {
            this.journalSrNo = journalSrNo;
        }

        public String getTranxDate() {
            return tranxDate;
        }

        public void setTranxDate(String tranxDate) {
            this.tranxDate = tranxDate;
        }

        public Double getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(Double totalAmt) {
            this.totalAmt = totalAmt;
        }

        public String getNarrations() {
            return narrations;
        }

        public void setNarrations(String narrations) {
            this.narrations = narrations;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(Long responseStatus) {
            this.responseStatus = responseStatus;
        }

        public List<JournalByIdRowDTO> getPerticulars() {
            return perticulars;
        }

        public void setPerticulars(List<JournalByIdRowDTO> perticulars) {
            this.perticulars = perticulars;
        }

    }
