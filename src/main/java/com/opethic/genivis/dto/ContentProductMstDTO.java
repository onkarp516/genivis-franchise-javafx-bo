package com.opethic.genivis.dto;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ContentProductMstDTO {

    private SimpleIntegerProperty sgst;
    private SimpleIntegerProperty purchaserate;
    private SimpleStringProperty batch_expiry;
    private SimpleStringProperty hsn;
    private SimpleStringProperty code;
    private SimpleBooleanProperty is_serial;
    private SimpleIntegerProperty tax_per;
    private SimpleIntegerProperty mrp;
    private SimpleIntegerProperty cgst;
    private SimpleStringProperty packing;
    private SimpleStringProperty product_name;
    private SimpleBooleanProperty is_inventory;
    private SimpleIntegerProperty igst;
    private SimpleStringProperty unit;
    private SimpleBooleanProperty is_batch;
    private SimpleIntegerProperty current_stock;
    private SimpleStringProperty tax_type;
    private SimpleIntegerProperty id;
    private SimpleBooleanProperty is_negative;
    private SimpleStringProperty barcode;
    private SimpleStringProperty brand;
    private SimpleIntegerProperty sales_rate;
    private SimpleStringProperty productType;

    public ContentProductMstDTO(Integer sgst, Integer purchaserate, String batch_expiry, String hsn, String code, Boolean is_serial, Integer tax_per, Integer mrp, Integer cgst, String packing, String product_name, Boolean is_inventory, Integer igst, String unit, Boolean is_batch, Integer current_stock, String tax_type, Integer id, Boolean is_negative, String barcode, String brand, Integer sales_rate, String productType) {
        this.sgst = new SimpleIntegerProperty(sgst);
        this.purchaserate = new SimpleIntegerProperty(purchaserate);
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
        this.hsn = new SimpleStringProperty(hsn);
        this.code = new SimpleStringProperty(code);
        this.is_serial = new SimpleBooleanProperty(is_serial);
        this.tax_per = new SimpleIntegerProperty(tax_per);
        this.mrp = new SimpleIntegerProperty(mrp);
        this.cgst = new SimpleIntegerProperty(cgst);
        this.packing = new SimpleStringProperty(packing);
        this.product_name = new SimpleStringProperty(product_name);
        this.is_inventory = new SimpleBooleanProperty(is_inventory);
        this.igst = new SimpleIntegerProperty(igst);
        this.unit = new SimpleStringProperty(unit);
        this.is_batch = new SimpleBooleanProperty(is_batch);
        this.current_stock = new SimpleIntegerProperty(current_stock);
        this.tax_type = new SimpleStringProperty(tax_type);
        this.id = new SimpleIntegerProperty(id);
        this.is_negative = new SimpleBooleanProperty(is_negative);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.sales_rate = new SimpleIntegerProperty(sales_rate);
        this.productType = new SimpleStringProperty(productType);
    }


    public int getSgst() {
        return sgst.get();
    }

    public SimpleIntegerProperty sgstProperty() {
        return sgst;
    }

    public void setSgst(int sgst) {
        this.sgst.set(sgst);
    }

    public int getPurchaserate() {
        return purchaserate.get();
    }

    public SimpleIntegerProperty purchaserateProperty() {
        return purchaserate;
    }

    public void setPurchaserate(int purchaserate) {
        this.purchaserate.set(purchaserate);
    }

    public String getBatch_expiry() {
        return batch_expiry.get();
    }

    public SimpleStringProperty batch_expiryProperty() {
        return batch_expiry;
    }

    public void setBatch_expiry(String batch_expiry) {
        this.batch_expiry.set(batch_expiry);
    }

    public String getHsn() {
        return hsn.get();
    }

    public SimpleStringProperty hsnProperty() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn.set(hsn);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code.set(code);
    }

    public boolean isIs_serial() {
        return is_serial.get();
    }

    public SimpleBooleanProperty is_serialProperty() {
        return is_serial;
    }

    public void setIs_serial(boolean is_serial) {
        this.is_serial.set(is_serial);
    }

    public int getTax_per() {
        return tax_per.get();
    }

    public SimpleIntegerProperty tax_perProperty() {
        return tax_per;
    }

    public void setTax_per(int tax_per) {
        this.tax_per.set(tax_per);
    }

    public int getMrp() {
        return mrp.get();
    }

    public SimpleIntegerProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp.set(mrp);
    }

    public int getCgst() {
        return cgst.get();
    }

    public SimpleIntegerProperty cgstProperty() {
        return cgst;
    }

    public void setCgst(int cgst) {
        this.cgst.set(cgst);
    }

    public String getPacking() {
        return packing.get();
    }

    public SimpleStringProperty packingProperty() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing.set(packing);
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

    public boolean isIs_inventory() {
        return is_inventory.get();
    }

    public SimpleBooleanProperty is_inventoryProperty() {
        return is_inventory;
    }

    public void setIs_inventory(boolean is_inventory) {
        this.is_inventory.set(is_inventory);
    }

    public int getIgst() {
        return igst.get();
    }

    public SimpleIntegerProperty igstProperty() {
        return igst;
    }

    public void setIgst(int igst) {
        this.igst.set(igst);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public boolean isIs_batch() {
        return is_batch.get();
    }

    public SimpleBooleanProperty is_batchProperty() {
        return is_batch;
    }

    public void setIs_batch(boolean is_batch) {
        this.is_batch.set(is_batch);
    }

    public int getCurrent_stock() {
        return current_stock.get();
    }

    public SimpleIntegerProperty current_stockProperty() {
        return current_stock;
    }

    public void setCurrent_stock(int current_stock) {
        this.current_stock.set(current_stock);
    }

    public String getTax_type() {
        return tax_type.get();
    }

    public SimpleStringProperty tax_typeProperty() {
        return tax_type;
    }

    public void setTax_type(String tax_type) {
        this.tax_type.set(tax_type);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public boolean isIs_negative() {
        return is_negative.get();
    }

    public SimpleBooleanProperty is_negativeProperty() {
        return is_negative;
    }

    public void setIs_negative(boolean is_negative) {
        this.is_negative.set(is_negative);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public int getSales_rate() {
        return sales_rate.get();
    }

    public SimpleIntegerProperty sales_rateProperty() {
        return sales_rate;
    }

    public void setSales_rate(int sales_rate) {
        this.sales_rate.set(sales_rate);
    }

    public String getProductType() {
        return productType.get();
    }

    public SimpleStringProperty productTypeProperty() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType.set(productType);
    }
}
