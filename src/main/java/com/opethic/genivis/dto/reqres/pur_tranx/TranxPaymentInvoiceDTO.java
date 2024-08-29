package com.opethic.genivis.dto.reqres.pur_tranx;

import com.opethic.genivis.dto.reqres.creditNote.BillByBillRowReqDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

/*** Bill by Bill DTO for Table ****/
public class TranxPaymentInvoiceDTO {
    /*  private String id;
      private String invoiceNo;
      private String invoiceAmt;
      private String type;
      private String BillDate;
      private String balance;
      private String dueDate;
      private String days;
      private String paidAmt;
      private String balanceAmt;*/
    private SimpleBooleanProperty is_row_selected=new SimpleBooleanProperty(false);
    private SimpleStringProperty id;
    private SimpleStringProperty invoiceNo;
    private SimpleStringProperty invoiceAmt;
    private SimpleStringProperty type;
    private SimpleStringProperty billdate;
    private SimpleStringProperty balance;
    private SimpleStringProperty dueDate;
    private SimpleStringProperty days;
    private SimpleStringProperty paidAmt;
    private SimpleStringProperty balanceAmt;
    private String source="";
    private String invoiceUniqueNumber="";
    private Double totalAmt=0.0;
    private Long ledgerId;
    private String balancingType;
    private Long billDetailsId=0L;
    private Double remainingAmt;
    private String transactionTrackCode;

    public String getTransactionTrackCode() {
        return transactionTrackCode;
    }

    public void setTransactionTrackCode(String transactionTrackCode) {
        this.transactionTrackCode = transactionTrackCode;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInvoiceUniqueNumber() {
        return invoiceUniqueNumber;
    }

    public void setInvoiceUniqueNumber(String invoiceUniqueNumber) {
        this.invoiceUniqueNumber = invoiceUniqueNumber;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getBalancingType() {
        return balancingType;
    }

    public void setBalancingType(String balancingType) {
        this.balancingType = balancingType;
    }

    public Long getBillDetailsId() {
        return billDetailsId;
    }

    public void setBillDetailsId(Long billDetailsId) {
        this.billDetailsId = billDetailsId;
    }

    public Double getRemainingAmt() {
        return remainingAmt;
    }

    public void setRemainingAmt(Double remainingAmt) {
        this.remainingAmt = remainingAmt;
    }

    public List<BillByBillRowReqDTO> getBillRowReqDTOS() {
        return billRowReqDTOS;
    }

    public void setCredit(String credit) {
        this.credit.set(credit);
    }

    public TranxPaymentInvoiceDTO() {
    }

    public TranxPaymentInvoiceDTO(String id, String invoiceNo,
                                  String invoiceAmt, String type,
                                  String billdate, String balance,
                                  String dueDate, String days,
                                  String paidAmt, String balanceAmt,String source) {
        this.id = new SimpleStringProperty(id);
        this.invoiceNo = new SimpleStringProperty(invoiceNo);
        this.invoiceAmt = new SimpleStringProperty(invoiceAmt);
        this.type = new SimpleStringProperty(type);
        this.billdate = new SimpleStringProperty(billdate);
        this.balance = new SimpleStringProperty(balance);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.days = new SimpleStringProperty(days);
        this.paidAmt = new SimpleStringProperty(paidAmt);
        this.balanceAmt = new SimpleStringProperty(balanceAmt);
        this.is_row_selected = new SimpleBooleanProperty(false);
        this.source = source;
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getInvoiceNo() {
        return invoiceNo.get();
    }

    public SimpleStringProperty invoiceNoProperty() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo.set(invoiceNo);
    }

    public String getInvoiceAmt() {
        return invoiceAmt.get();
    }

    public SimpleStringProperty invoiceAmtProperty() {
        return invoiceAmt;
    }

    public void setInvoiceAmt(String invoiceAmt) {
        this.invoiceAmt.set(invoiceAmt);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getBilldate() {
        return billdate.get();
    }

    public SimpleStringProperty billdateProperty() {
        return billdate;
    }

    public void setBilldate(String billdate) {
        this.billdate.set(billdate);
    }

    public String getBalance() {
        return balance.get();
    }

    public SimpleStringProperty balanceProperty() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance.set(balance);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public SimpleStringProperty dueDateProperty() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public String getDays() {
        return days.get();
    }

    public SimpleStringProperty daysProperty() {
        return days;
    }

    public void setDays(String days) {
        this.days.set(days);
    }

    public String getPaidAmt() {
        return paidAmt.get();
    }

    public SimpleStringProperty paidAmtProperty() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt.set(paidAmt);
    }

    public String getBalanceAmt() {
        return balanceAmt.get();
    }

    public SimpleStringProperty balanceAmtProperty() {
        return balanceAmt;
    }

    public void setBalanceAmt(String balanceAmt) {
        this.balanceAmt.set(balanceAmt);
    }

    public boolean isIs_row_selected() {
        return is_row_selected.get();
    }

    public SimpleBooleanProperty is_row_selectedProperty() {
        return is_row_selected;
    }

    public void setIs_row_selected(boolean is_row_selected) {
        this.is_row_selected.set(is_row_selected);
    }

    public String getSource() {
        return source;
    }
//    ------------------------
private List<BillByBillRowReqDTO> billRowReqDTOS = new ArrayList<>();
private SimpleStringProperty debit;
    private SimpleStringProperty credit;
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
    public void setBillRowReqDTOS(List<BillByBillRowReqDTO> billRowReqDTOS) {
        this.billRowReqDTOS = billRowReqDTOS;
    }
    public SimpleStringProperty creditProperty() {
        return this.credit;
    }
    public String getCredit() {
        return creditProperty().get();
    }

}
