package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class StockReport1DTO {
    private SimpleStringProperty id;
    private SimpleStringProperty code;
    private SimpleStringProperty product_name;
    private SimpleStringProperty drug_type;
    private SimpleStringProperty packing;
    private SimpleStringProperty brand;
    private SimpleStringProperty drug_content;
    private SimpleStringProperty rate;
    private SimpleStringProperty company;
    private SimpleStringProperty group;
    private SimpleStringProperty sub_group;
    private SimpleStringProperty batch_no;
    private SimpleStringProperty expiry_date;
    private SimpleStringProperty closing_stock;
    private SimpleStringProperty unit_name;
    private SimpleStringProperty unit_qty;

    public StockReport1DTO(
            String id,
            String code,
            String product_name,
            String drug_type,
            String packing,
            String brand,
            String drug_content,
            String rate,
            String company,
            String group,
            String sub_group,
            String batch_no,
            String expiry_date,
            String closing_stock,
            String unit_name,
            String unit_qty
            ){
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.product_name = new SimpleStringProperty(product_name);
        this.drug_type = new SimpleStringProperty(drug_type);
        this.packing = new SimpleStringProperty(packing);
        this.brand = new SimpleStringProperty(brand);
        this.rate = new SimpleStringProperty(rate);
        this.company = new SimpleStringProperty(company);
        this.group = new SimpleStringProperty(group);
        this.sub_group = new SimpleStringProperty(sub_group);
        this.drug_content = new SimpleStringProperty(drug_content);
        this.batch_no = new SimpleStringProperty(batch_no);
        this.expiry_date = new SimpleStringProperty(expiry_date);
        this.closing_stock = new SimpleStringProperty(closing_stock);
        this.unit_name = new SimpleStringProperty(unit_name);
        this.unit_qty = new SimpleStringProperty(unit_qty);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getCode() {return code.get();}
    public SimpleStringProperty codeProperty() {return code;}
    public void setCode(String code) {this.code = new SimpleStringProperty(code);}

    public String getProduct_name() {return product_name.get();}
    public SimpleStringProperty product_nameProperty() {return product_name;}
    public void setProduct_name(String product_name) {this.product_name = new SimpleStringProperty(product_name);}

    public String getDrug_type() {return drug_type.get();}
    public SimpleStringProperty drug_typeProperty() {return drug_type;}
    public void setDrug_type(String drug_type) {this.drug_type = new SimpleStringProperty(drug_type);}

    public String getPacking() {return packing.get();}
    public SimpleStringProperty packingProperty() {return packing;}
    public void setPacking(String packing) {this.packing = new SimpleStringProperty(packing);}

    public String getBrand() {return brand.get();}
    public SimpleStringProperty brandProperty() {return brand;}
    public void setBrand(String brand) {this.brand = new SimpleStringProperty(brand);}

    public String getDrug_content() {return drug_content.get();}
    public SimpleStringProperty drug_contentProperty() {return drug_content;}
    public void setDrug_content(String drug_content) {this.drug_content = new SimpleStringProperty(drug_content);}

    public String getRate() {return rate.get();}
    public SimpleStringProperty rateProperty() {return rate;}
    public void setRate(String rate) {this.rate = new SimpleStringProperty(rate);}

    public String getCompany() {return company.get();}
    public SimpleStringProperty companyProperty() {return company;}
    public void setCompany(String company) {this.company = new SimpleStringProperty(company);}

    public String getGroup() {return group.get();}
    public SimpleStringProperty groupProperty() {return group;}
    public void setGroup(String group) {this.group = new SimpleStringProperty(group);}

    public String getSub_group() {return sub_group.get();}
    public SimpleStringProperty sub_groupProperty() {return sub_group;}
    public void setSub_group(String sub_group) {this.sub_group = new SimpleStringProperty(sub_group);}

    public String getBatch_no() {return batch_no.get();}
    public SimpleStringProperty batch_noProperty() {return batch_no;}
    public void setBatch_no(String batch_no) {this.batch_no = new SimpleStringProperty(batch_no);}

    public String getExpiry_date() {return expiry_date.get();}
    public SimpleStringProperty expiry_dateProperty() {return expiry_date;}
    public void setExpiry_date(String expiry_date) {this.expiry_date = new SimpleStringProperty(expiry_date);}

    public String getClosing_stock() {return closing_stock.get();}
    public SimpleStringProperty closing_stockProperty() {return closing_stock;}
    public void setClosing_stock(String closing_stock) {this.closing_stock = new SimpleStringProperty(closing_stock);}

    public String getUnit_name() {return unit_name.get();}
    public SimpleStringProperty unit_nameProperty() {return unit_name;}
    public void setUnit_name(String unit_name) {this.unit_name = new SimpleStringProperty(unit_name);}

    public String getUnit_qty() {return unit_qty.get();}
    public SimpleStringProperty unit_qtyProperty() {return unit_qty;}
    public void setUnit_qty(String unit_qty) {this.unit_qty = new SimpleStringProperty(unit_qty);}

}
