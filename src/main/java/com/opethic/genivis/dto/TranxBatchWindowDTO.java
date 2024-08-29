package com.opethic.genivis.dto;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

public class TranxBatchWindowDTO  {
    private SimpleStringProperty b_no;
    private SimpleObjectProperty pur_date;
    private SimpleObjectProperty manufacturing_date;
    private SimpleObjectProperty b_expiry;


    private SimpleStringProperty pur_date2;
    private SimpleStringProperty manufacturing_date2;
    private SimpleStringProperty b_expiry2;
    private SimpleStringProperty mrp;
    private SimpleStringProperty PurRate;
    private SimpleStringProperty b_qty;
    private SimpleStringProperty b_freeQty;
    private SimpleStringProperty b_dis_per;
    private SimpleStringProperty b_dis_amt;
    private SimpleStringProperty barcode;
    private SimpleStringProperty margin;
    private SimpleStringProperty b_rate_a;
    private SimpleStringProperty b_rate_b;
    private SimpleStringProperty b_rate_c;
    private SimpleStringProperty costing;
    private SimpleStringProperty costingWithTax;
    private SimpleStringProperty b_details_id;
    private SimpleStringProperty unit_id;
    private SimpleStringProperty level_a_id;
    private SimpleStringProperty level_b_id;
    private SimpleStringProperty level_c_id;
    private SimpleStringProperty product_id;
    public TranxBatchWindowDTO(String b_no, LocalDate pur_date, LocalDate manufacturing_date, LocalDate b_expiry, String mrp, String PurRate, String b_qty,
                               String b_freeQty, String b_dis_per, String b_dis_amt, String barcode, String margin, String b_rate_a, String b_rate_b,
                               String b_rate_c, String costing, String costingWithTax, String b_details_id,
                               String unit_id, String level_a_id, String level_b_id, String level_c_id, String product_id) {
        this.b_no = new SimpleStringProperty(b_no);
        this.pur_date = new SimpleObjectProperty(pur_date);
        this.manufacturing_date =  new SimpleObjectProperty(manufacturing_date);
        this.b_expiry =  new SimpleObjectProperty(b_expiry);
        this.mrp =  new SimpleStringProperty(mrp);
        this.PurRate =  new SimpleStringProperty(PurRate);
        this.b_qty =  new SimpleStringProperty(b_qty);
        this.b_freeQty =  new SimpleStringProperty(b_freeQty);
        this.b_dis_per =  new SimpleStringProperty(b_dis_per);
        this.b_dis_amt =  new SimpleStringProperty(b_dis_amt);
        this.barcode =  new SimpleStringProperty(barcode);
        this.margin =  new SimpleStringProperty(margin);
        this.b_rate_a =  new SimpleStringProperty(b_rate_a);
        this.b_rate_b =  new SimpleStringProperty(b_rate_b);
        this.b_rate_c =  new SimpleStringProperty(b_rate_c);
        this.costing =  new SimpleStringProperty(costing);
        this.costingWithTax =  new SimpleStringProperty(costingWithTax);
        this.b_details_id =  new SimpleStringProperty(b_details_id);
        this.unit_id =  new SimpleStringProperty(unit_id);
        this.level_a_id =  new SimpleStringProperty(level_a_id);
        this.level_b_id =  new SimpleStringProperty(level_b_id);
        this.level_c_id =  new SimpleStringProperty(level_c_id);
        this.product_id =  new SimpleStringProperty(product_id);
    }

    public TranxBatchWindowDTO(String b_no, String pur_date, String manufacturing_date, String b_expiry, String mrp, String purRate, String b_qty, String b_freeQty,
                               String b_dis_per, String b_dis_amt, String barcode, String margin, String b_rate_a, String b_rate_b, String b_rate_c, String costing,
                               String costingWithTax, String b_details_id, String unit_id, String level_a_id, String level_b_id, String product_id) {
        this.b_no = new SimpleStringProperty(b_no) ;
        this.pur_date2 = new SimpleStringProperty(pur_date);
        this.manufacturing_date2 =new SimpleStringProperty(manufacturing_date) ;
        this.b_expiry2 = new SimpleStringProperty(b_expiry) ;
        this.mrp = new SimpleStringProperty(mrp) ;
        PurRate = new SimpleStringProperty(purRate) ;
        this.b_qty =  new SimpleStringProperty(b_qty) ;
        this.b_freeQty = new SimpleStringProperty(b_freeQty) ;
        this.b_dis_per = new SimpleStringProperty(b_dis_per) ;
        this.b_dis_amt = new SimpleStringProperty(b_dis_amt) ;
        this.barcode = new SimpleStringProperty(barcode) ;
        this.margin = new SimpleStringProperty(margin) ;
        this.b_rate_a = new SimpleStringProperty(b_rate_a) ;
        this.b_rate_b = new SimpleStringProperty(b_rate_b) ;
        this.b_rate_c = new SimpleStringProperty(b_rate_c) ;
        this.costing = new SimpleStringProperty(costing) ;
        this.costingWithTax = new SimpleStringProperty(costingWithTax) ;
        this.b_details_id = new SimpleStringProperty(b_details_id) ;
        this.unit_id = new SimpleStringProperty(unit_id) ;
        this.level_a_id = new SimpleStringProperty(level_a_id);
        this.level_b_id = new SimpleStringProperty(level_b_id)  ;
        this.product_id = new SimpleStringProperty(product_id) ;
    }

    public void setPur_date(Object pur_date) {
        this.pur_date.set(pur_date);
    }

    public void setManufacturing_date(Object manufacturing_date) {
        this.manufacturing_date.set(manufacturing_date);
    }

    public void setB_expiry(Object b_expiry) {
        this.b_expiry.set(b_expiry);
    }

    public String getPur_date2() {
        return pur_date2.get();
    }

    public SimpleStringProperty pur_date2Property() {
        return pur_date2;
    }

    public void setPur_date2(String pur_date2) {
        this.pur_date2.set(pur_date2);
    }

    public String getManufacturing_date2() {
        return manufacturing_date2.get();
    }

    public SimpleStringProperty manufacturing_date2Property() {
        return manufacturing_date2;
    }

    public void setManufacturing_date2(String manufacturing_date2) {
        this.manufacturing_date2.set(manufacturing_date2);
    }

    public String getB_expiry2() {
        return b_expiry2.get();
    }

    public SimpleStringProperty b_expiry2Property() {
        return b_expiry2;
    }

    public void setB_expiry2(String b_expiry2) {
        this.b_expiry2.set(b_expiry2);
    }

    public String getB_no() {
        return b_no.get();
    }

    public SimpleStringProperty b_noProperty() {
        return b_no;
    }

    public void setB_no(String b_no) {
        this.b_no = new SimpleStringProperty(b_no);
    }

    public LocalDate getPur_date() {
        return (LocalDate) pur_date.get();
    }

    public  SimpleObjectProperty<LocalDate> pur_dateProperty() {
        return pur_date;
    }

    public void setPur_date(LocalDate pur_date) {
        this.pur_date= new SimpleObjectProperty(pur_date);
    }

    public LocalDate getManufacturing_date() {
        return (LocalDate) manufacturing_date.get();
    }

    public SimpleObjectProperty<LocalDate> manufacturing_dateProperty() {
        return manufacturing_date;
    }

    public void setManufacturing_date(String manufacturing_date) {
        this.manufacturing_date= new SimpleObjectProperty(manufacturing_date);
    }

    public LocalDate getB_expiry() {
        return (LocalDate) b_expiry.get();
    }

    public SimpleObjectProperty<LocalDate> b_expiryProperty() {
        return b_expiry;
    }

    public void setB_expiry(LocalDate b_expiry) {
        this.b_expiry=new SimpleObjectProperty(b_expiry);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp=new SimpleStringProperty(mrp);
    }

    public String getPurRate() {
        return PurRate.get();
    }

    public SimpleStringProperty purRateProperty() {
        return PurRate;
    }

    public void setPurRate(String purRate) {
        this.PurRate =new SimpleStringProperty(purRate);
    }

    public String getB_qty() {
        return b_qty.get();
    }

    public SimpleStringProperty b_qtyProperty() {
        return b_qty;
    }

    public void setB_qty(String b_qty) {
        this.b_qty =new SimpleStringProperty(b_qty);
    }

    public String getB_freeQty() {
        return b_freeQty.get();
    }

    public SimpleStringProperty b_freeQtyProperty() {
        return b_freeQty;
    }

    public void setB_freeQty(String b_freeQty) {
        this.b_freeQty =new SimpleStringProperty(b_freeQty);
    }

    public String getB_dis_per() {
        return b_dis_per.get();
    }

    public SimpleStringProperty b_dis_perProperty() {
        return b_dis_per;
    }

    public void setB_dis_per(String b_dis_per) {
        this.b_dis_per=new SimpleStringProperty(b_dis_per);
    }

    public String getB_dis_amt() {
        return b_dis_amt.get();
    }

    public SimpleStringProperty b_dis_amtProperty() {
        return b_dis_amt;
    }

    public void setB_dis_amt(String b_dis_amt) {
        this.b_dis_amt=new SimpleStringProperty(b_dis_amt);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode=new SimpleStringProperty(barcode);
    }

    public String getMargin() {
        return margin.get();
    }

    public SimpleStringProperty marginProperty() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = new SimpleStringProperty(margin);
    }

    public String getB_rate_a() {
        return b_rate_a.get();
    }

    public SimpleStringProperty b_rate_aProperty() {
        return b_rate_a;
    }

    public void setB_rate_a(String b_rate_a) {
        this.b_rate_a=new SimpleStringProperty(b_rate_a);
    }

    public String getB_rate_b() {
        return b_rate_b.get();
    }

    public SimpleStringProperty b_rate_bProperty() {
        return b_rate_b;
    }

    public void setB_rate_b(String b_rate_b) {
        this.b_rate_b=new SimpleStringProperty(b_rate_b);
    }

    public String getB_rate_c() {
        return b_rate_c.get();
    }

    public SimpleStringProperty b_rate_cProperty() {
        return b_rate_c;
    }

    public void setB_rate_c(String b_rate_c) {
        this.b_rate_c =new SimpleStringProperty(b_rate_c);
    }

    public String getCosting() {
        return costing.get();
    }

    public SimpleStringProperty costingProperty() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing=new SimpleStringProperty(costing);
    }

    public String getCostingWithTax() {
        return costingWithTax.get();
    }

    public SimpleStringProperty costingWithTaxProperty() {
        return costingWithTax;
    }

    public void setCostingWithTax(String costingWithTax) {
        this.costingWithTax=new SimpleStringProperty(costingWithTax);
    }

    public String getB_details_id() {
        return b_details_id.get();
    }

    public SimpleStringProperty b_details_idProperty() {
        return b_details_id;
    }

    public void setB_details_id(String b_details_id) {
        this.b_details_id=new SimpleStringProperty(b_details_id);
    }

    public String getUnit_id() {
        return unit_id.get();
    }

    public SimpleStringProperty unit_idProperty() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id=new SimpleStringProperty(unit_id);
    }

    public String getLevel_a_id() {
        return level_a_id.get();
    }

    public SimpleStringProperty level_a_idProperty() {
        return level_a_id;
    }

    public void setLevel_a_id(String level_a_id) {
        this.level_a_id=new SimpleStringProperty(level_a_id);
    }

    public String getLevel_b_id() {
        return level_b_id.get();
    }

    public SimpleStringProperty level_b_idProperty() {
        return level_b_id;
    }

    public void setLevel_b_id(String level_b_id) {
        this.level_b_id=new SimpleStringProperty(level_b_id);
    }

    public String getLevel_c_id() {
        return level_c_id.get();
    }

    public SimpleStringProperty level_c_idProperty() {
        return level_c_id;
    }

    public void setLevel_c_id(String level_c_id) {
        this.level_c_id=new SimpleStringProperty(level_c_id);
    }

    public String getProduct_id() {
        return product_id.get();
    }

    public SimpleStringProperty product_idProperty() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id =new SimpleStringProperty(product_id);
    }


}
