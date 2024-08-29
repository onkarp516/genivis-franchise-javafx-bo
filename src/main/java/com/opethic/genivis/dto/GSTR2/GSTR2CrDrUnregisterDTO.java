package com.opethic.genivis.dto.GSTR2;

import javafx.beans.property.SimpleStringProperty;

public class GSTR2CrDrUnregisterDTO {

    private SimpleStringProperty gst_number;
    private SimpleStringProperty id;              //id of the tranx list
    private SimpleStringProperty start_date;
    private SimpleStringProperty end_date;
    private SimpleStringProperty invoice_no;
    private SimpleStringProperty ledger_id;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty total_amt;
    private SimpleStringProperty total_cgst;
    private SimpleStringProperty total_igst;
    private SimpleStringProperty total_sgst;
    private SimpleStringProperty total_tax;
    private SimpleStringProperty transaction_date;
    private SimpleStringProperty voucher_type;

    public GSTR2CrDrUnregisterDTO(String tranxId,String startDate,String endDate,String invoiceNo,String ledgerId,String ledgerName, String taxableAmt, String totalAmt,
                                  String totalCgst,String totalIgst,String totalSgst,String totalTax,String tranxDate, String voucherType){

        this.id = new SimpleStringProperty(tranxId);
        this.start_date = new SimpleStringProperty(startDate);
        this.end_date = new SimpleStringProperty(endDate);
        this.invoice_no = new SimpleStringProperty(invoiceNo);
        this.ledger_id = new SimpleStringProperty(ledgerId);
        this.ledger_name = new SimpleStringProperty(ledgerName);
        this.taxable_amt = new SimpleStringProperty(taxableAmt);
        this.total_amt = new SimpleStringProperty(totalAmt);
        this.total_cgst = new SimpleStringProperty(totalCgst);
        this.total_igst = new SimpleStringProperty(totalIgst);
        this.total_sgst = new SimpleStringProperty(totalSgst);
        this.total_tax = new SimpleStringProperty(totalTax);
        this.transaction_date = new SimpleStringProperty(tranxDate);
        this.voucher_type = new SimpleStringProperty(voucherType);
    }
    public GSTR2CrDrUnregisterDTO(String gstNo,String tranxId,String startDate,String endDate,String invoiceNo,String ledgerId,String ledgerName, String taxableAmt, String totalAmt,
                                  String totalCgst,String totalIgst,String totalSgst,String totalTax,String tranxDate, String voucherType){
    //constructor for GSTR2 Ammendment to Credit/Debit note to initialize the value
        this.gst_number = new SimpleStringProperty(gstNo);
        this.id = new SimpleStringProperty(tranxId);
        this.start_date = new SimpleStringProperty(startDate);
        this.end_date = new SimpleStringProperty(endDate);
        this.invoice_no = new SimpleStringProperty(invoiceNo);
        this.ledger_id = new SimpleStringProperty(ledgerId);
        this.ledger_name = new SimpleStringProperty(ledgerName);
        this.taxable_amt = new SimpleStringProperty(taxableAmt);
        this.total_amt = new SimpleStringProperty(totalAmt);
        this.total_cgst = new SimpleStringProperty(totalCgst);
        this.total_igst = new SimpleStringProperty(totalIgst);
        this.total_sgst = new SimpleStringProperty(totalSgst);
        this.total_tax = new SimpleStringProperty(totalTax);
        this.transaction_date = new SimpleStringProperty(tranxDate);
        this.voucher_type = new SimpleStringProperty(voucherType);
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

    public String getStart_date() {
        return start_date.get();
    }

    public SimpleStringProperty start_dateProperty() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date.set(start_date);
    }

    public String getEnd_date() {
        return end_date.get();
    }

    public SimpleStringProperty end_dateProperty() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date.set(end_date);
    }

    public String getInvoice_no() {
        return invoice_no.get();
    }

    public SimpleStringProperty invoice_noProperty() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no.set(invoice_no);
    }

    public String getLedger_id() {
        return ledger_id.get();
    }

    public SimpleStringProperty ledger_idProperty() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id.set(ledger_id);
    }

    public String getLedger_name() {
        return ledger_name.get();
    }

    public SimpleStringProperty ledger_nameProperty() {
        return ledger_name;
    }

    public void setLedger_name(String ledger_name) {
        this.ledger_name.set(ledger_name);
    }

    public String getTaxable_amt() {
        return taxable_amt.get();
    }

    public SimpleStringProperty taxable_amtProperty() {
        return taxable_amt;
    }

    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt.set(taxable_amt);
    }

    public String getTotal_amt() {
        return total_amt.get();
    }

    public SimpleStringProperty total_amtProperty() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt.set(total_amt);
    }

    public String getTotal_cgst() {
        return total_cgst.get();
    }

    public SimpleStringProperty total_cgstProperty() {
        return total_cgst;
    }

    public void setTotal_cgst(String total_cgst) {
        this.total_cgst.set(total_cgst);
    }

    public String getTotal_igst() {
        return total_igst.get();
    }

    public SimpleStringProperty total_igstProperty() {
        return total_igst;
    }

    public void setTotal_igst(String total_igst) {
        this.total_igst.set(total_igst);
    }

    public String getTotal_sgst() {
        return total_sgst.get();
    }

    public SimpleStringProperty total_sgstProperty() {
        return total_sgst;
    }

    public void setTotal_sgst(String total_sgst) {
        this.total_sgst.set(total_sgst);
    }

    public String getTotal_tax() {
        return total_tax.get();
    }

    public SimpleStringProperty total_taxProperty() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax.set(total_tax);
    }

    public String getTransaction_date() {
        return transaction_date.get();
    }

    public SimpleStringProperty transaction_dateProperty() {
        return transaction_date;
    }

    public void setTransaction_date(String transaction_date) {
        this.transaction_date.set(transaction_date);
    }

    public String getVoucher_type() {
        return voucher_type.get();
    }

    public SimpleStringProperty voucher_typeProperty() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type.set(voucher_type);
    }

    public String getGst_number() {
        return gst_number.get();
    }

    public SimpleStringProperty gst_numberProperty() {
        return gst_number;
    }

    public void setGst_number(String gst_number) {
        this.gst_number.set(gst_number);
    }
}
