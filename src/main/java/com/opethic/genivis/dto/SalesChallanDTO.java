package com.opethic.genivis.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class SalesChallanDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty bill_no;
    private SimpleStringProperty referenceNo;
    private SimpleStringProperty bill_date;
    private SimpleStringProperty sundry_debtors_name;
    private SimpleStringProperty narration;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty tax_amt;
    private SimpleStringProperty total_amount;
    private SimpleStringProperty sales_challan_status;
    private SimpleBooleanProperty is_row_selected;
    private SimpleStringProperty salesAccountId;
    private SimpleStringProperty salesAccountName;
    private SimpleStringProperty ledgerId;

    //    id,SC_bill_no,SC_referenceNo,SC_bill_date,SC_sundry_debtors_name,SC_narration,SC_taxable_amt,SC_tax_amt,SC_total_amount,SC_sales_challan_status
    public SalesChallanDTO(String id, String bill_no, String referenceNo, String bill_date, String sundry_debtors_name, String narration,
                           String taxable_amt, String tax_amt, String total_amount, String sales_challan_status,String ledgerId) {
        this.id = new SimpleStringProperty(id);
        this.bill_no = new SimpleStringProperty(bill_no);
        this.referenceNo = new SimpleStringProperty(referenceNo);
        this.bill_date = new SimpleStringProperty(bill_date);
        this.sundry_debtors_name = new SimpleStringProperty(sundry_debtors_name);
        this.narration = new SimpleStringProperty(narration);
        this.sales_challan_status = new SimpleStringProperty(sales_challan_status);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.tax_amt = new SimpleStringProperty(tax_amt);
        this.total_amount = new SimpleStringProperty(total_amount);
        this.is_row_selected = new SimpleBooleanProperty(false);
        this.ledgerId = new SimpleStringProperty(ledgerId);
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


    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getBill_no() {
        return bill_no.get();
    }

    public SimpleStringProperty bill_noProperty() {
        return bill_no;
    }

    public void setBill_no(String bill_no) {
        this.bill_no.set(bill_no);
    }

    public String getReferenceNo() {
        return referenceNo.get();
    }

    public SimpleStringProperty referenceNoProperty() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo.set(referenceNo);
    }

    public String getBill_date() {
        return bill_date.get();
    }

    public SimpleStringProperty bill_dateProperty() {
        return bill_date;
    }

    public void setBill_date(String bill_date) {
        this.bill_date.set(bill_date);
    }

    public String getSundry_debtors_name() {
        return sundry_debtors_name.get();
    }

    public SimpleStringProperty sundry_debtors_nameProperty() {
        return sundry_debtors_name;
    }

    public void setSundry_debtors_name(String sundry_debtors_name) {
        this.sundry_debtors_name.set(sundry_debtors_name);
    }

    public String getNarration() {
        return narration.get();
    }

    public SimpleStringProperty narrationProperty() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration.set(narration);
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

    public String getTax_amt() {
        return tax_amt.get();
    }

    public SimpleStringProperty tax_amtProperty() {
        return tax_amt;
    }

    public void setTax_amt(String tax_amt) {
        this.tax_amt.set(tax_amt);
    }

    public String getTotal_amount() {
        return total_amount.get();
    }

    public SimpleStringProperty total_amountProperty() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount.set(total_amount);
    }

    public String getSales_challan_status() {
        return sales_challan_status.get();
    }

    public SimpleStringProperty sales_challan_statusProperty() {
        return sales_challan_status;
    }

    public void setSales_challan_status(String sales_challan_status) {
        this.sales_challan_status.set(sales_challan_status);
    }

    public String getSalesAccountId() {
        return salesAccountId.get();
    }

    public SimpleStringProperty salesAccountIdProperty() {
        return salesAccountId;
    }

    public void setSalesAccountId(String salesAccountId) {
        this.salesAccountId.set(salesAccountId);
    }

    public String getSalesAccountName() {
        return salesAccountName.get();
    }

    public SimpleStringProperty salesAccountNameProperty() {
        return salesAccountName;
    }

    public void setSalesAccountName(String salesAccountName) {
        this.salesAccountName.set(salesAccountName);
    }

    public String getLedgerId() {
        return ledgerId.get();
    }

    public SimpleStringProperty ledgerIdProperty() {
        return ledgerId;
    }

    public void setLedgerId(String ledgerId) {
        this.ledgerId.set(ledgerId);
    }
}
