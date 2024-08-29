package com.opethic.genivis.dto.utilities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BarcodePrintDTO {
    private SimpleStringProperty sr_no;
    private SimpleStringProperty product_id;
    private SimpleStringProperty product_name;
    private SimpleStringProperty barcode_no;
    private SimpleStringProperty batch_no;
    private SimpleStringProperty mrp;
    private SimpleStringProperty packing_name;
    private SimpleStringProperty units_name;
    private SimpleStringProperty product_qty;
    private SimpleStringProperty print_qty;

    public BarcodePrintDTO(String product_id, String product_name, String barcode_no, String batch_no, String mrp, String packing_name, String units_name, String product_qty, String print_qty, String sr_no) {
        this.product_id = new SimpleStringProperty(product_id);
        this.product_name=new SimpleStringProperty(product_name);
        this.barcode_no = new SimpleStringProperty( barcode_no);
        this.batch_no = new SimpleStringProperty( batch_no);
        this.mrp = new SimpleStringProperty( mrp);
        this.packing_name = new SimpleStringProperty( packing_name);
        this.units_name = new SimpleStringProperty( units_name);
        this.product_qty = new SimpleStringProperty( product_qty);
        this.print_qty = new SimpleStringProperty( print_qty);
        this.sr_no = new SimpleStringProperty( sr_no);
    }

    public BarcodePrintDTO() {
    }

    public String getSr_no() {
        return sr_no.get();
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no.set(sr_no);
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public SimpleStringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name.set(product_name);
    }

    public String getProduct_id() {
        return product_id.get();
    }

    public StringProperty product_idProperty() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id.set(product_id);
    }

    public String getBarcode_no() {
        return barcode_no.get();
    }

    public StringProperty barcode_noProperty() {
        return barcode_no;
    }

    public void setBarcode_no(String barcode_no) {
        this.barcode_no.set(barcode_no);
    }

    public String getBatch_no() {
        return batch_no.get();
    }

    public StringProperty batch_noProperty() {
        return batch_no;
    }

    public void setBatch_no(String batch_no) {
        this.batch_no.set(batch_no);
    }

    public String getMrp() {
        return mrp.get();
    }

    public StringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp);
    }

    public String getPacking_name() {
        return packing_name.get();
    }

    public StringProperty packing_nameProperty() {
        return packing_name;
    }

    public void setPacking_name(String packing_name) {
        this.packing_name.set(packing_name);
    }

    public String getUnits_name() {
        return units_name.get();
    }

    public StringProperty units_nameProperty() {
        return units_name;
    }

    public void setUnits_name(String units_name) {
        this.units_name.set(units_name);
    }

    public String getProduct_qty() {
        return product_qty.get();
    }

    public StringProperty product_qtyProperty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty.set(product_qty);
    }

    public String getPrint_qty() {
        return print_qty.get();
    }

    public StringProperty print_qtyProperty() {
        return print_qty;
    }

    public void setPrint_qty(String print_qty) {
        this.print_qty.set(print_qty);
    }
}
