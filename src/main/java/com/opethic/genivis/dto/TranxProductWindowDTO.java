package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class TranxProductWindowDTO {
    private SimpleStringProperty index = new SimpleStringProperty("");
    private SimpleStringProperty id;
    private SimpleStringProperty code;
    private SimpleStringProperty product_name;
    private SimpleStringProperty packing;
    private SimpleStringProperty barcode;
    private SimpleStringProperty brand;
    private SimpleStringProperty mrp;
    private SimpleStringProperty current_stock;
    private SimpleStringProperty unit;
    private SimpleStringProperty sales_rate;
    private SimpleStringProperty is_negative;
    private SimpleStringProperty batch_expiry;
    private SimpleStringProperty tax_per;
    private SimpleStringProperty is_batch;
    private SimpleStringProperty purchaserate;
    private List<UnitRateList> unitRateList = new ArrayList<>();
    private String unit1Name;
    private Integer unit1Id;
    private Double unit1FSRMH;
    private Double unit1FSRAI;
    private Double unit1CSRMH;
    private Double unit1CSRAI;
    private Double unit1Conv;
    private Double unit1ClsStock;
    private Double unit1ActualStock;
    private Boolean unit1IsNegetive;
    private String unit2Name;
    private Integer unit2Id;
    private Double unit2FSRMH;
    private Double unit2FSRAI;
    private Double unit2CSRMH;
    private Double unit2CSRAI;
    private Double unit2Conv;
    private Double unit2ClsStock;
    private Double unit2ActualStock;
    private Boolean unit2IsNegetive;

    private String unit3Name;
    private Integer unit3Id;
    private Double unit3FSRMH;
    private Double unit3FSRAI;
    private Double unit3CSRMH;
    private Double unit3CSRAI;
    private Double unit3Conv;
    private Double unit3ClsStock;
    private Double unit3ActualStock;
    private Boolean unit3IsNegetive;

    public TranxProductWindowDTO(String index, String id, String code,
                                 String product_name, String packing,
                                 String barcode, String brand, String mrp,
                                 String current_stock, String unit,
                                 String sales_rate, String is_negative,
                                 String batch_expiry, String tax_per,
                                 String is_batch, String purchaserate,
                                 List<UnitRateList> unitRateList) {
        this.index = new SimpleStringProperty(index);
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.product_name = new SimpleStringProperty(product_name);
        this.packing = new SimpleStringProperty(packing);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.mrp = new SimpleStringProperty(mrp);
        this.current_stock = new SimpleStringProperty(current_stock);
        this.unit = new SimpleStringProperty(unit);
        this.sales_rate = new SimpleStringProperty(sales_rate);
        this.is_negative = new SimpleStringProperty(is_negative);
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.is_batch = new SimpleStringProperty(is_batch);
        this.purchaserate = new SimpleStringProperty(purchaserate);
        this.unitRateList = unitRateList;
    }

    public TranxProductWindowDTO(String index, String id, String code, String product_name, String packing, String barcode, String brand, String mrp, String current_stock,
                                 String unit, String sales_rate, String is_negative, String batch_expiry, String tax_per, String is_batch, String purchaserate) {
        this.index = new SimpleStringProperty(index);
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.product_name = new SimpleStringProperty(product_name);
        this.packing = new SimpleStringProperty(packing);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.mrp = new SimpleStringProperty(mrp);
        this.current_stock = new SimpleStringProperty(current_stock);
        this.unit = new SimpleStringProperty(unit);
        this.sales_rate = new SimpleStringProperty(sales_rate);
        this.is_negative = new SimpleStringProperty(is_negative);
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.is_batch = new SimpleStringProperty(is_batch);
        this.purchaserate = new SimpleStringProperty(purchaserate);

    }

    public TranxProductWindowDTO(String id, String code, String product_name, String packing, String barcode, String brand, String mrp, String current_stock,
                                 String unit, String sales_rate, String is_negative, String batch_expiry, String tax_per, String is_batch, String purchaserate) {
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.product_name = new SimpleStringProperty(product_name);
        this.packing = new SimpleStringProperty(packing);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.mrp = new SimpleStringProperty(mrp);
        this.current_stock = new SimpleStringProperty(current_stock);
        this.unit = new SimpleStringProperty(unit);
        this.sales_rate = new SimpleStringProperty(sales_rate);
        this.is_negative = new SimpleStringProperty(is_negative);
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.is_batch = new SimpleStringProperty(is_batch);
        this.purchaserate = new SimpleStringProperty(purchaserate);

    }

    public TranxProductWindowDTO(String id, String code, String product_name,
                                 String packing, String barcode, String brand,
                                 String mrp, String current_stock, String unit,
                                 String sales_rate, String is_negative,
                                 String batch_expiry, String tax_per,
                                 String is_batch, String purchaserate,
                                 List<UnitRateList> unitRateList, String unit1Name, Integer unit1Id, Double unit1FSRMH,
                                 Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI, Double unit1Conv,
                                 Double unit1ClsStock, Double unit1ActualStock,
                                 String unit2Name, Integer unit2Id, Double unit2FSRMH, Double unit2FSRAI,
                                 Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, Double unit2ClsStock,
                                 Double unit2ActualStock, String unit3Name, Integer unit3Id,
                                 Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI,
                                 Double unit3Conv, Double unit3ClsStock, Double unit3ActualStock) {
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.product_name = new SimpleStringProperty(product_name);
        this.packing = new SimpleStringProperty(packing);
        this.barcode = new SimpleStringProperty(barcode);
        this.brand = new SimpleStringProperty(brand);
        this.mrp = new SimpleStringProperty(mrp);
        this.current_stock = new SimpleStringProperty(current_stock);
        this.unit = new SimpleStringProperty(unit);
        this.sales_rate = new SimpleStringProperty(sales_rate);
        this.is_negative = new SimpleStringProperty(is_negative);
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.is_batch = new SimpleStringProperty(is_batch);
        this.purchaserate = new SimpleStringProperty(purchaserate);
        this.unitRateList = unitRateList;
        this.unit1Name = unit1Name;
        this.unit1Id = unit1Id;
        this.unit1FSRMH = unit1FSRMH;
        this.unit1FSRAI = unit1FSRAI;
        this.unit1CSRMH = unit1CSRMH;
        this.unit1CSRAI = unit1CSRAI;
        this.unit1Conv = unit1Conv;
        this.unit1ClsStock = unit1ClsStock;
        this.unit1ActualStock = unit1ActualStock;
        this.unit2Name = unit2Name;
        this.unit2Id = unit2Id;
        this.unit2FSRMH = unit2FSRMH;
        this.unit2FSRAI = unit2FSRAI;
        this.unit2CSRMH = unit2CSRMH;
        this.unit2CSRAI = unit2CSRAI;
        this.unit2Conv = unit2Conv;
        this.unit2ClsStock = unit2ClsStock;
        this.unit2ActualStock = unit2ActualStock;
        this.unit3Name = unit3Name;
        this.unit3Id = unit3Id;
        this.unit3FSRMH = unit3FSRMH;
        this.unit3FSRAI = unit3FSRAI;
        this.unit3CSRMH = unit3CSRMH;
        this.unit3CSRAI = unit3CSRAI;
        this.unit3Conv = unit3Conv;
        this.unit3ClsStock = unit3ClsStock;
        this.unit3ActualStock = unit3ActualStock;
    }

    public List<UnitRateList> getUnitRateList() {
        return unitRateList;
    }

    public void setUnitRateList(List<UnitRateList> unitRateList) {
        this.unitRateList = unitRateList;
    }

    public String getIndex() {
        return index.get();
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public void setIndex(String index) {
        this.index.set(index);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code = new SimpleStringProperty(code);
    }

    public String getProduct_name() {
        return product_name.get();
    }

    public SimpleStringProperty product_nameProperty() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = new SimpleStringProperty(product_name);
    }

    public String getPacking() {
        return packing.get();
    }

    public SimpleStringProperty packingProperty() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = new SimpleStringProperty(packing);
    }

    public String getBarcode() {
        return barcode.get();
    }

    public SimpleStringProperty barcodeProperty() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = new SimpleStringProperty(barcode);
    }

    public String getBrand() {
        return brand.get();
    }

    public SimpleStringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = new SimpleStringProperty(brand);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = new SimpleStringProperty(mrp);
    }

    public String getCurrent_stock() {
        return current_stock.get();
    }

    public SimpleStringProperty current_stockProperty() {
        return current_stock;
    }

    public void setCurrent_stock(String current_stock) {
        this.current_stock = new SimpleStringProperty(current_stock);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = new SimpleStringProperty(unit);
    }

    public String getSales_rate() {
        return sales_rate.get();
    }

    public SimpleStringProperty sales_rateProperty() {
        return sales_rate;
    }

    public void setSales_rate(String sales_rate) {
        this.sales_rate = new SimpleStringProperty(sales_rate);
    }

    public String getIs_negative() {
        return is_negative.get();
    }

    public SimpleStringProperty is_negativeProperty() {
        return is_negative;
    }

    public void setIs_negative(String is_negative) {
        this.is_negative = new SimpleStringProperty(is_negative);
    }

    public String getBatch_expiry() {
        return batch_expiry.get();
    }

    public SimpleStringProperty batch_expiryProperty() {
        return batch_expiry;
    }

    public void setBatch_expiry(String batch_expiry) {
        this.batch_expiry = new SimpleStringProperty(batch_expiry);
    }

    public String getTax_per() {
        return tax_per.get();
    }

    public SimpleStringProperty tax_perProperty() {
        return tax_per;
    }

    public void setTax_per(String tax_per) {
        this.tax_per = new SimpleStringProperty(tax_per);
    }

    public String getIs_batch() {
        return is_batch.get();
    }

    public SimpleStringProperty is_batchProperty() {
        return is_batch;
    }

    public void setIs_batch(String is_batch) {
        this.is_batch = new SimpleStringProperty(is_batch);
    }

    public String getPurchaserate() {
        return purchaserate.get();
    }

    public SimpleStringProperty purchaserateProperty() {
        return purchaserate;
    }

    public void setPurchaserate(String purchaserate) {
        this.purchaserate = new SimpleStringProperty(purchaserate);
    }

    public String getUnit1Name() {
        return unit1Name;
    }

    public void setUnit1Name(String unit1Name) {
        this.unit1Name = unit1Name;
    }

    public Integer getUnit1Id() {
        return unit1Id;
    }

    public void setUnit1Id(Integer unit1Id) {
        this.unit1Id = unit1Id;
    }

    public Double getUnit1FSRMH() {
        return unit1FSRMH;
    }

    public void setUnit1FSRMH(Double unit1FSRMH) {
        this.unit1FSRMH = unit1FSRMH;
    }

    public Double getUnit1FSRAI() {
        return unit1FSRAI;
    }

    public void setUnit1FSRAI(Double unit1FSRAI) {
        this.unit1FSRAI = unit1FSRAI;
    }

    public Double getUnit1CSRMH() {
        return unit1CSRMH;
    }

    public void setUnit1CSRMH(Double unit1CSRMH) {
        this.unit1CSRMH = unit1CSRMH;
    }

    public Double getUnit1CSRAI() {
        return unit1CSRAI;
    }

    public void setUnit1CSRAI(Double unit1CSRAI) {
        this.unit1CSRAI = unit1CSRAI;
    }

    public Double getUnit1Conv() {
        return unit1Conv;
    }

    public void setUnit1Conv(Double unit1Conv) {
        this.unit1Conv = unit1Conv;
    }

    public Double getUnit1ClsStock() {
        return unit1ClsStock;
    }

    public void setUnit1ClsStock(Double unit1ClsStock) {
        this.unit1ClsStock = unit1ClsStock;
    }

    public Double getUnit1ActualStock() {
        return unit1ActualStock;
    }

    public void setUnit1ActualStock(Double unit1ActualStock) {
        this.unit1ActualStock = unit1ActualStock;
    }

    public Boolean getUnit1IsNegetive() {
        return unit1IsNegetive;
    }

    public void setUnit1IsNegetive(Boolean unit1IsNegetive) {
        this.unit1IsNegetive = unit1IsNegetive;
    }

    public String getUnit2Name() {
        return unit2Name;
    }

    public void setUnit2Name(String unit2Name) {
        this.unit2Name = unit2Name;
    }

    public Integer getUnit2Id() {
        return unit2Id;
    }

    public void setUnit2Id(Integer unit2Id) {
        this.unit2Id = unit2Id;
    }

    public Double getUnit2FSRMH() {
        return unit2FSRMH;
    }

    public void setUnit2FSRMH(Double unit2FSRMH) {
        this.unit2FSRMH = unit2FSRMH;
    }

    public Double getUnit2FSRAI() {
        return unit2FSRAI;
    }

    public void setUnit2FSRAI(Double unit2FSRAI) {
        this.unit2FSRAI = unit2FSRAI;
    }

    public Double getUnit2CSRMH() {
        return unit2CSRMH;
    }

    public void setUnit2CSRMH(Double unit2CSRMH) {
        this.unit2CSRMH = unit2CSRMH;
    }

    public Double getUnit2CSRAI() {
        return unit2CSRAI;
    }

    public void setUnit2CSRAI(Double unit2CSRAI) {
        this.unit2CSRAI = unit2CSRAI;
    }

    public Double getUnit2Conv() {
        return unit2Conv;
    }

    public void setUnit2Conv(Double unit2Conv) {
        this.unit2Conv = unit2Conv;
    }

    public Double getUnit2ClsStock() {
        return unit2ClsStock;
    }

    public void setUnit2ClsStock(Double unit2ClsStock) {
        this.unit2ClsStock = unit2ClsStock;
    }

    public Double getUnit2ActualStock() {
        return unit2ActualStock;
    }

    public void setUnit2ActualStock(Double unit2ActualStock) {
        this.unit2ActualStock = unit2ActualStock;
    }

    public Boolean getUnit2IsNegetive() {
        return unit2IsNegetive;
    }

    public void setUnit2IsNegetive(Boolean unit2IsNegetive) {
        this.unit2IsNegetive = unit2IsNegetive;
    }

    public String getUnit3Name() {
        return unit3Name;
    }

    public void setUnit3Name(String unit3Name) {
        this.unit3Name = unit3Name;
    }

    public Integer getUnit3Id() {
        return unit3Id;
    }

    public void setUnit3Id(Integer unit3Id) {
        this.unit3Id = unit3Id;
    }

    public Double getUnit3FSRMH() {
        return unit3FSRMH;
    }

    public void setUnit3FSRMH(Double unit3FSRMH) {
        this.unit3FSRMH = unit3FSRMH;
    }

    public Double getUnit3FSRAI() {
        return unit3FSRAI;
    }

    public void setUnit3FSRAI(Double unit3FSRAI) {
        this.unit3FSRAI = unit3FSRAI;
    }

    public Double getUnit3CSRMH() {
        return unit3CSRMH;
    }

    public void setUnit3CSRMH(Double unit3CSRMH) {
        this.unit3CSRMH = unit3CSRMH;
    }

    public Double getUnit3CSRAI() {
        return unit3CSRAI;
    }

    public void setUnit3CSRAI(Double unit3CSRAI) {
        this.unit3CSRAI = unit3CSRAI;
    }

    public Double getUnit3Conv() {
        return unit3Conv;
    }

    public void setUnit3Conv(Double unit3Conv) {
        this.unit3Conv = unit3Conv;
    }

    public Double getUnit3ClsStock() {
        return unit3ClsStock;
    }

    public void setUnit3ClsStock(Double unit3ClsStock) {
        this.unit3ClsStock = unit3ClsStock;
    }

    public Double getUnit3ActualStock() {
        return unit3ActualStock;
    }

    public void setUnit3ActualStock(Double unit3ActualStock) {
        this.unit3ActualStock = unit3ActualStock;
    }

    public Boolean getUnit3IsNegetive() {
        return unit3IsNegetive;
    }

    public void setUnit3IsNegetive(Boolean unit3IsNegetive) {
        this.unit3IsNegetive = unit3IsNegetive;
    }

    @Override
    public String toString() {
        return "TranxProductWindowDTO{" +
                "index=" + index +
                ", id=" + id +
                ", code=" + code +
                ", product_name=" + product_name +
                ", packing=" + packing +
                ", barcode=" + barcode +
                ", brand=" + brand +
                ", mrp=" + mrp +
                ", current_stock=" + current_stock +
                ", unit=" + unit +
                ", sales_rate=" + sales_rate +
                ", is_negative=" + is_negative +
                ", batch_expiry=" + batch_expiry +
                ", tax_per=" + tax_per +
                ", is_batch=" + is_batch +
                ", purchaserate=" + purchaserate +
                '}';
    }
}
