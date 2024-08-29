package com.opethic.genivis.models.AccountEntry;


public class AccTRowDTO {
   private String type;
    private String  typeobj;
    private String perticulars;
    private Double  paid_amt;
    private String  bank_payment_type;
    private String bank_payment_no;
    private String   bank_name;
    private String   payment_date;
    private Double   debit;
    private Double   credit;
    private String  receiptNarration;


 public AccTRowDTO(String type, String typeobj, String perticulars, Double paid_amt, String bank_payment_type, String bank_payment_no, String bank_name, String payment_date, Double debit, Double credit, String receiptNarration) {
  this.type = type;
  this.typeobj = typeobj;
  this.perticulars = perticulars;
  this.paid_amt = paid_amt;
  this.bank_payment_type = bank_payment_type;
  this.bank_payment_no = bank_payment_no;
  this.bank_name = bank_name;
  this.payment_date = payment_date;
  this.debit = debit;
  this.credit = credit;
  this.receiptNarration = receiptNarration;
 }

 public String getType() {
  return type;
 }

 public void setType(String type) {
  this.type = type;
 }

 public String getTypeobj() {
  return typeobj;
 }

 public void setTypeobj(String typeobj) {
  this.typeobj = typeobj;
 }

 public String getPerticulars() {
  return perticulars;
 }

 public void setPerticulars(String perticulars) {
  this.perticulars = perticulars;
 }

 public Double getPaid_amt() {
  return paid_amt;
 }

 public void setPaid_amt(Double paid_amt) {
  this.paid_amt = paid_amt;
 }

 public String getBank_payment_type() {
  return bank_payment_type;
 }

 public void setBank_payment_type(String bank_payment_type) {
  this.bank_payment_type = bank_payment_type;
 }

 public String getBank_payment_no() {
  return bank_payment_no;
 }

 public void setBank_payment_no(String bank_payment_no) {
  this.bank_payment_no = bank_payment_no;
 }

 public String getBank_name() {
  return bank_name;
 }

 public void setBank_name(String bank_name) {
  this.bank_name = bank_name;
 }

 public String getPayment_date() {
  return payment_date;
 }

 public void setPayment_date(String payment_date) {
  this.payment_date = payment_date;
 }

 public Double getDebit() {
  return debit;
 }

 public void setDebit(Double debit) {
  this.debit = debit;
 }

 public Double getCredit() {
  return credit;
 }

 public void setCredit(Double credit) {
  this.credit = credit;
 }

 public String getReceiptNarration() {
  return receiptNarration;
 }

 public void setReceiptNarration(String receiptNarration) {
  this.receiptNarration = receiptNarration;
 }
}
