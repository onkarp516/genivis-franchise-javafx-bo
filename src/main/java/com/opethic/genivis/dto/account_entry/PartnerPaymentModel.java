package com.opethic.genivis.dto.account_entry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.*;

public class PartnerPaymentModel {
    private BooleanProperty select=new SimpleBooleanProperty(false);
    private StringProperty franchise_name;
    private StringProperty sales_date;
    private StringProperty sales_invoice_no;
    private DoubleProperty sales_invoice_amt;
    private DoubleProperty sales_tax_amt;
    private StringProperty partner_name;
    private StringProperty designation;
    private DoubleProperty taxable_amt;
    private DoubleProperty incentive_amt;
    private DoubleProperty tds;
    private DoubleProperty payment;

    public boolean isSelect() {
        return selectProperty().get();
    }

    public BooleanProperty selectProperty() {
        if(select==null)
            select=new SimpleBooleanProperty(this,"select");
        return select;
    }

    public void setSelect(boolean select) {
        selectProperty().set(select);
    }

    public String getFranchise_name() {
        return franchise_nameProperty().get();
    }

    public StringProperty franchise_nameProperty() {
        if(franchise_name==null)
            franchise_name=new SimpleStringProperty(this,"franchise_name");
        return franchise_name;
    }

    public void setFranchise_name(String franchise_name) {
        franchise_nameProperty().set(franchise_name);
    }

    public String getSales_date() {
        return sales_dateProperty().get();
    }

    public StringProperty sales_dateProperty() {
        if(sales_date==null)
            sales_date=new SimpleStringProperty(this,"sales_date");
        return sales_date;
    }

    public void setSales_date(String sales_date) {
        sales_dateProperty().set(sales_date);
    }

    public String getSales_invoice_no() {
        return sales_invoice_noProperty().get();
    }

    public StringProperty sales_invoice_noProperty() {
        if(sales_invoice_no==null)
            sales_invoice_no=new SimpleStringProperty(this,"sales_invoice_no");
        return sales_invoice_no;
    }

    public void setSales_invoice_no(String sales_invoice_no) {
        sales_invoice_noProperty().set(sales_invoice_no);
    }

    public double getSales_invoice_amt() {
        return sales_invoice_amtProperty().get();
    }

    public DoubleProperty sales_invoice_amtProperty() {
        if(sales_invoice_amt==null)
            sales_invoice_amt=new SimpleDoubleProperty(this,"sales_invoice_amt");
        return sales_invoice_amt;
    }

    public void setSales_invoice_amt(double sales_invoice_amt) {
        sales_invoice_amtProperty().set(sales_invoice_amt);
    }

    public double getSales_tax_amt() {
        return sales_tax_amtProperty().get();
    }

    public DoubleProperty sales_tax_amtProperty() {
        if(sales_tax_amt==null)
            sales_tax_amt=new SimpleDoubleProperty(this,"sales_tax_amt");
        return sales_tax_amt;
    }

    public void setSales_tax_amt(double sales_tax_amt) {
        sales_tax_amtProperty().set(sales_tax_amt);
    }

    public String getPartner_name() {
        return partner_nameProperty().get();
    }

    public StringProperty partner_nameProperty() {
        if(partner_name==null)
            partner_name=new SimpleStringProperty(this,"partner_name");
        return partner_name;
    }

    public void setPartner_name(String partner_name) {
        partner_nameProperty().set(partner_name);
    }

    public String getDesignation() {
        return designationProperty().get();
    }

    public StringProperty designationProperty() {
        if(designation==null)
            designation=new SimpleStringProperty(this,"designation");
        return designation;
    }

    public void setDesignation(String designation) {
        designationProperty().set(designation);
    }

    public double getTaxable_amt() {
        return taxable_amtProperty().get();
    }

    public DoubleProperty taxable_amtProperty() {
        if(taxable_amt==null)
            taxable_amt=new SimpleDoubleProperty(this,"taxable_amt");
        return taxable_amt;
    }

    public void setTaxable_amt(double taxable_amt) {
        taxable_amtProperty().set(taxable_amt);
    }

    public double getIncentive_amt() {
        return incentive_amtProperty().get();
    }

    public DoubleProperty incentive_amtProperty() {
        if(incentive_amt==null)
            incentive_amt=new SimpleDoubleProperty(this,"incentive_amt");
        return incentive_amt;
    }

    public void setIncentive_amt(double incentive_amt) {
        incentive_amtProperty().set(incentive_amt);
    }

    public double getTds() {
        return tdsProperty().get();
    }

    public DoubleProperty tdsProperty() {
        if(tds==null)
            tds=new SimpleDoubleProperty(this,"tds");
        return tds;
    }

    public void setTds(double tds) {
        tdsProperty().set(tds);
    }

    public double getPayment() {
        return paymentProperty().get();
    }

    public DoubleProperty paymentProperty() {
        if(payment==null)
            payment=new SimpleDoubleProperty(this,"payment");
        return payment;
    }

    public void setPayment(double payment) {
        paymentProperty().set(payment);
    }
}
