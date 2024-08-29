package com.opethic.genivis.dto.reqres.payment;

import com.opethic.genivis.dto.reqres.creditNote.BillByBillRowReqDTO;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class PaymentRowDTO {
    private SimpleStringProperty id = new SimpleStringProperty("0");
    private SimpleStringProperty current_index;

    private SimpleStringProperty type = new SimpleStringProperty("Dr");
    private SimpleStringProperty particulars = new SimpleStringProperty("");
    private SimpleStringProperty debit;
    private SimpleStringProperty credit;

    private Long detailsId;
    private Long ledgerId;
    private String ledgerType;
    private String ledgerName;
    private String balancingMethod;
    private Double payableAmt;
    private Double remainingAmt;
    private Double selectedAmt;
    private Boolean isAdvanceCheck;
    private Long invoiceId;
    private String invoiceUniqueId;
    private Double amount;
    private Double totalAmt;
    private String invoiceDate;
    private String invoiceNo;
    private Long billLedgerId;
    private String source;
    private Integer dueDays;
    private String balancingType;
    private Double paidAmt;
    private Long billDetailsId;
    private Double billRemainingAmt;
    private String paymentMode;
    private String bankAccount;
    private String tranxNum;

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getTranxNum() {
        return tranxNum;
    }

    public void setTranxNum(String tranxNum) {
        this.tranxNum = tranxNum;
    }

    private List<BillByBillRowReqDTO> billRowReqDTOS = new ArrayList<>();

    public List<BillByBillRowReqDTO> getBillRowReqDTOS() {
        return billRowReqDTOS;
    }

    public void setBillRowReqDTOS(List<BillByBillRowReqDTO> billRowReqDTOS) {
        this.billRowReqDTOS = billRowReqDTOS;
    }

    public PaymentRowDTO() {
    }

    public PaymentRowDTO(String current_index, String type,
                         String particulars, String debit, String credit) {
        this.current_index = new SimpleStringProperty(current_index);
        this.type = new SimpleStringProperty(type);
        this.particulars = new SimpleStringProperty(particulars);
        this.debit = new SimpleStringProperty(debit);
        this.credit = new SimpleStringProperty(credit);
    }

    public String getCurrent_index() {
        return current_indexProperty().get();
    }

    public SimpleStringProperty current_indexProperty() {
        return current_index;
    }

    public void setCurrent_index(String current_index) {
        current_indexProperty().set(current_index);
    }

    public String getId() {
        return idProperty().get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        idProperty().set(id);
    }

    public String getType() {
        return typeProperty().get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        typeProperty().set(type);
    }

    public String getParticulars() {
        return particularsProperty().get();
    }

    public SimpleStringProperty particularsProperty() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        particularsProperty().set(particulars);
    }

    public String getDebit() {
        return debitProperty().get();
    }

    public SimpleStringProperty debitProperty() {
        return debit;
    }

    public void setDebit(String debit) {
        if (debitProperty() != null)
            debitProperty().set(debit);

        else {
            this.debit = new SimpleStringProperty(debit);
        }

    }

    public String getCredit() {
        return creditProperty().get();
    }

    public SimpleStringProperty creditProperty() {
        return this.credit;
    }

    public void setCredit(String credit) {
        if (creditProperty() != null)
            creditProperty().set(credit);
        else {
            this.credit = new SimpleStringProperty(credit);
        }
    }

    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long detailsId) {
        this.detailsId = detailsId;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
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

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public Double getSelectedAmt() {
        return selectedAmt;
    }

    public void setSelectedAmt(Double selectedAmt) {
        this.selectedAmt = selectedAmt;
    }

    public Boolean getAdvanceCheck() {
        return isAdvanceCheck;
    }

    public void setAdvanceCheck(Boolean advanceCheck) {
        isAdvanceCheck = advanceCheck;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceUniqueId() {
        return invoiceUniqueId;
    }

    public void setInvoiceUniqueId(String invoiceUniqueId) {
        this.invoiceUniqueId = invoiceUniqueId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
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

    public Long getBillLedgerId() {
        return billLedgerId;
    }

    public void setBillLedgerId(Long billLedgerId) {
        this.billLedgerId = billLedgerId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getDueDays() {
        return dueDays;
    }

    public void setDueDays(Integer dueDays) {
        this.dueDays = dueDays;
    }

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public Long getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Long billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Double getBillRemainingAmt() {
        return billRemainingAmt;
    }

    public void setBillRemainingAmt(Double billRemainingAmt) {
        this.billRemainingAmt = billRemainingAmt;
    }

    @Override
    public String toString() {
        return "PaymentRowDTO{" +
                "id=" + id +
                ", current_index=" + current_index +
                ", type=" + type +
                ", particulars=" + particulars +
                ", debit=" + debit +
                ", credit=" + credit +
                ", ledgerId=" + ledgerId +
                ", ledgerType='" + ledgerType + '\'' +
                ", ledgerName='" + ledgerName + '\'' +
                ", balancingMethod='" + balancingMethod + '\'' +
                ", payableAmt=" + payableAmt +
                ", remainingAmt=" + remainingAmt +
                ", selectedAmt=" + selectedAmt +
                ", isAdvanceCheck=" + isAdvanceCheck +
                ", invoiceId=" + invoiceId +
                ", invoiceUniqueId='" + invoiceUniqueId + '\'' +
                ", amount=" + amount +
                ", totalAmt=" + totalAmt +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", billLedgerId=" + billLedgerId +
                ", source='" + source + '\'' +
                ", dueDays=" + dueDays +
                ", balancingType='" + balancingType + '\'' +
                ", paidAmt=" + paidAmt +
                ", billDetailsId=" + billDetailsId +
                ", billRemainingAmt=" + billRemainingAmt +
                ", billRowReqDTOS=" + billRowReqDTOS +
                '}';
    }
}
