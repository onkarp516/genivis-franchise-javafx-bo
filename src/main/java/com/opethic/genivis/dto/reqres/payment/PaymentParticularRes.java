package com.opethic.genivis.dto.reqres.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillId;

import java.io.Serializable;
import java.util.List;

public class PaymentParticularRes implements Serializable {
    @SerializedName("details_id")
    @Expose
    private Long detailsId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("ledger_type")
    @Expose
    private String ledgerType;
    @SerializedName("ledger_name")
    @Expose
    private String ledgerName;
    @SerializedName("dr")
    @Expose
    private Double dr;
    @SerializedName("cr")
    @Expose
    private Double cr;
    @SerializedName("bank_payment_type")
    @Expose
    private BankPaymentType bankPaymentType;
    @SerializedName("bank_acc_name")
    @Expose
    private String bankAccName;
    @SerializedName("paymentTranxNo")
    @Expose
    private String paymentTranxNo;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
    @SerializedName("ledger_id")
    @Expose
    private Long ledgerId;
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

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
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

    public BankPaymentType getBankPaymentType() {
        return bankPaymentType;
    }

    public void setBankPaymentType(BankPaymentType bankPaymentType) {
        this.bankPaymentType = bankPaymentType;
    }

    public String getBankAccName() {
        return bankAccName;
    }

    public void setBankAccName(String bankAccName) {
        this.bankAccName = bankAccName;
    }

    public String getPaymentTranxNo() {
        return paymentTranxNo;
    }

    public void setPaymentTranxNo(String paymentTranxNo) {
        this.paymentTranxNo = paymentTranxNo;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
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

    @Override
    public String toString() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("details_id", detailsId);
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("ledger_type", ledgerType);
        jsonObject.addProperty("ledger_name", ledgerName);
        jsonObject.addProperty("dr", dr);
        jsonObject.addProperty("cr", cr);
        jsonObject.addProperty("bank_payment_type", bankPaymentType.toString());
        jsonObject.addProperty("bank_acc_name", bankAccName);
        jsonObject.addProperty("paymentTranxNo", paymentTranxNo);
        jsonObject.addProperty("payment_date", paymentDate);
        jsonObject.addProperty("ledger_id", ledgerId);
        jsonObject.addProperty("balancingMethod", balancingMethod);
        jsonObject.addProperty("payableAmt", payableAmt);
        jsonObject.addProperty("selectedAmt", selectedAmt);
        jsonObject.addProperty("remainingAmt", remainingAmt);
        jsonObject.addProperty("isAdvanceCheck", isAdvanceCheck);
        jsonObject.addProperty("bills", bills != null ? bills.toString() : null);

        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
