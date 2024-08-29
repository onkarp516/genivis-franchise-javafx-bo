package com.opethic.genivis.dto.reqres.debitNote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillId;

import java.io.Serializable;
import java.util.List;

public class DebitNoteByIdRowDTO implements Serializable {

    @SerializedName("details_id")
    @Expose
    private Long detailsId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ledger_type")
    @Expose
    private String ledgerType;
    @SerializedName("total_amt")
    @Expose
    private Double totalAmt;
    @SerializedName("balance")
    @Expose
    private Double balance;
    @SerializedName("paid_amt")
    @Expose
    private Double paidAmt;
    @SerializedName("adjusted_source")
    @Expose
    private String adjustedSource;
    @SerializedName("adjustment_status")
    @Expose
    private String adjustmentStatus;
    @SerializedName("operations")
    @Expose
    private String operations;
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
    @SerializedName("debitnoteTranxNo")
    @Expose
    private String debitnoteTranxNo;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
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
    private Boolean isAdvanceCheck;
    @SerializedName("bills")
    @Expose
    private List<BillByBillId> bills;

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

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

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getAdjustedSource() {
        return adjustedSource;
    }

    public void setAdjustedSource(String adjustedSource) {
        this.adjustedSource = adjustedSource;
    }

    public String getAdjustmentStatus() {
        return adjustmentStatus;
    }

    public void setAdjustmentStatus(String adjustmentStatus) {
        this.adjustmentStatus = adjustmentStatus;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
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

    public String getDebitnoteTranxNo() {
        return debitnoteTranxNo;
    }

    public void setDebitnoteTranxNo(String debitnoteTranxNo) {
        this.debitnoteTranxNo = debitnoteTranxNo;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
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

    public Boolean getAdvanceCheck() {
        return isAdvanceCheck;
    }

    public void setAdvanceCheck(Boolean advanceCheck) {
        isAdvanceCheck = advanceCheck;
    }

    public List<BillByBillId> getBills() {
        return bills;
    }

    public void setBills(List<BillByBillId> bills) {
        this.bills = bills;
    }
}
