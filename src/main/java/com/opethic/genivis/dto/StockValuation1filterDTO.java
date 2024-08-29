package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class StockValuation1filterDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty product_name;
    private SimpleStringProperty brand;
    private SimpleStringProperty packing;
    private SimpleStringProperty group;
    private SimpleStringProperty category;
    private SimpleStringProperty batch;
    private SimpleStringProperty unit;
    private SimpleStringProperty qty;
    private SimpleStringProperty purchase_rate;
    private SimpleStringProperty valuation_pr;
    private SimpleStringProperty cost_With_tax;
    private SimpleStringProperty valuation_cwt;
    private SimpleStringProperty cost_Without_tax;
    private SimpleStringProperty valuation_ct;
    private SimpleStringProperty mrp;
    private SimpleStringProperty valuation_mrp;
    private SimpleStringProperty sales_rate;
    private SimpleStringProperty valuation_sr;

    public StockValuation1filterDTO(
            String id,
            String product_name,
            String brand,
            String packing,
            String group,
            String category,
            String batch,
            String unit,
            String qty,
            String purchase_rate,
            String valuation_pr,
            String cost_With_tax,
            String valuation_cwt,
            String cost_Without_tax,
            String valuation_ct,
            String mrp,
            String valuation_mrp,
            String sales_rate,
            String valuation_sr
    ){
        this.id = new SimpleStringProperty(id);
        this.product_name = new SimpleStringProperty(product_name);
        this.brand = new SimpleStringProperty(brand);
        this.packing = new SimpleStringProperty(packing);
        this.group = new SimpleStringProperty(group);
        this.category = new SimpleStringProperty(category);
        this.batch = new SimpleStringProperty(batch);
        this.unit = new SimpleStringProperty(unit);
        this.qty = new SimpleStringProperty(qty);
        this.purchase_rate = new SimpleStringProperty(purchase_rate);
        this.valuation_pr = new SimpleStringProperty(valuation_pr);
        this.cost_With_tax = new SimpleStringProperty(cost_With_tax);
        this.valuation_cwt = new SimpleStringProperty(valuation_cwt);
        this.cost_Without_tax = new SimpleStringProperty(cost_Without_tax);
        this.valuation_ct = new SimpleStringProperty(valuation_ct);
        this.mrp = new SimpleStringProperty(mrp);
        this.valuation_mrp = new SimpleStringProperty(valuation_mrp);
        this.sales_rate = new SimpleStringProperty(sales_rate);
        this.valuation_sr = new SimpleStringProperty(valuation_sr);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getProduct_name() {return product_name.get();}
    public SimpleStringProperty product_nameProperty() {return product_name;}
    public void setProduct_name(String product_name) {this.product_name = new SimpleStringProperty(product_name);}

    public String getBrand() {return brand.get();}
    public SimpleStringProperty brandProperty() {return brand;}
    public void setBrand(String brand) {this.brand = new SimpleStringProperty(brand);}

    public String getPacking() {return packing.get();}
    public SimpleStringProperty packingProperty() {return packing;}
    public void setPacking(String packing) {this.packing = new SimpleStringProperty(packing);}

    public String getGroup() {return group.get();}
    public SimpleStringProperty groupProperty() {return group;}
    public void setGroup(String group) {this.group = new SimpleStringProperty(group);}

    public String getCategory() {return category.get();}
    public SimpleStringProperty categoryProperty() {return category;}
    public void setCategory(String category) {this.category = new SimpleStringProperty(category);}

    public String getBatch() {return batch.get();}
    public SimpleStringProperty batchProperty() {return batch;}
    public void setBatch(String batch) {this.batch = new SimpleStringProperty(batch);}

    public String getUnit() {return unit.get();}
    public SimpleStringProperty unitProperty() {return unit;}
    public void setUnit(String unit) {this.unit = new SimpleStringProperty(unit);}

    public String getQty() {return qty.get();}
    public SimpleStringProperty qtyProperty() {return qty;}
    public void setQty(String qty) {this.qty = new SimpleStringProperty(qty);}

    public String getPurchase_rate() {return purchase_rate.get();}
    public SimpleStringProperty purchase_rateProperty() {return purchase_rate;}
    public void setPurchase_rate(String purchase_rate) {this.purchase_rate = new SimpleStringProperty(purchase_rate);}

    public String getValuation_pr() {return valuation_pr.get();}
    public SimpleStringProperty valuation_prProperty() {return valuation_pr;}
    public void setValuation_pr(String valuation_pr) {this.valuation_pr = new SimpleStringProperty(valuation_pr);}

    public String getCost_With_tax() {return cost_With_tax.get();}
    public SimpleStringProperty cost_With_taxProperty() {return cost_With_tax;}
    public void setCost_With_tax(String cost_With_tax) {this.cost_With_tax = new SimpleStringProperty(cost_With_tax);}

    public String getValuation_cwt() {return valuation_cwt.get();}
    public SimpleStringProperty valuation_cwtProperty() {return valuation_cwt;}
    public void setValuation_cwt(String valuation_cwt) {this.valuation_cwt = new SimpleStringProperty(valuation_cwt);}

    public String getCost_Without_tax() {return cost_Without_tax.get();}
    public SimpleStringProperty cost_Without_taxProperty() {return cost_Without_tax;}
    public void setCost_Without_tax(String cost_Without_tax) {this.cost_Without_tax = new SimpleStringProperty(cost_Without_tax);}

    public String getValuation_ct() {return valuation_ct.get();}
    public SimpleStringProperty valuation_ctProperty() {return valuation_ct;}
    public void setValuation_ct(String valuation_ct) {this.valuation_ct = new SimpleStringProperty(valuation_ct);}

    public String getMrp() {return mrp.get();}
    public SimpleStringProperty mrpProperty() {return mrp;}
    public void setMrp(String mrp) {this.mrp = new SimpleStringProperty(mrp);}

    public String getValuation_mrp() {return valuation_mrp.get();}
    public SimpleStringProperty valuation_mrpProperty() {return valuation_mrp;}
    public void setValuation_mrp(String valuation_mrp) {this.valuation_mrp = new SimpleStringProperty(valuation_mrp);}

    public String getSales_rate() {return sales_rate.get();}
    public SimpleStringProperty sales_rateProperty() {return sales_rate;}
    public void setSales_rate(String sales_rate) {this.sales_rate = new SimpleStringProperty(sales_rate);}

    public String getValuation_sr() {return valuation_sr.get();}
    public SimpleStringProperty valuation_srProperty() {return valuation_sr;}
    public void setValuation_sr(String valuation_sr) {this.valuation_sr = new SimpleStringProperty(valuation_sr);}
}

