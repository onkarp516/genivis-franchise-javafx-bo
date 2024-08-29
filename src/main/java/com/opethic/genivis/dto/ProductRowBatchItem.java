package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class ProductRowBatchItem {
    private SimpleStringProperty id = new SimpleStringProperty("0");

    private SimpleStringProperty current_index = new SimpleStringProperty("");
    private SimpleStringProperty b_no;
    private SimpleStringProperty opening_qty;
    private SimpleStringProperty b_free_qty;
    private SimpleStringProperty b_mrp;
    private SimpleStringProperty b_sale_rate;
    private SimpleStringProperty b_purchase_rate;
    private SimpleStringProperty b_costing;
    private SimpleStringProperty b_expiry;
    private SimpleStringProperty b_manufacturing_date;
    private SimpleStringProperty isOpeningbatch=new SimpleStringProperty("");
    private SimpleStringProperty isUpdate=new SimpleStringProperty("");
    private SimpleStringProperty batch_id;

    public ProductRowBatchItem(String id, String b_no, String opening_qty, String b_free_qty, String b_mrp, String b_sale_rate,
                     String b_purchase_rate, String b_costing, String b_expiry, String b_manufacturing_date,
                     String isOpeningbatch, String isUpdate, String batch_id) {
        this.id = new SimpleStringProperty(id);
        this.b_no = new SimpleStringProperty(b_no);
        this.opening_qty = new SimpleStringProperty(opening_qty);
        this.b_free_qty = new SimpleStringProperty(b_free_qty);
        this.b_mrp = new SimpleStringProperty(b_mrp);
        this.b_sale_rate = new SimpleStringProperty(b_sale_rate);
        this.b_purchase_rate = new SimpleStringProperty(b_purchase_rate);
        this.b_costing = new SimpleStringProperty(b_costing);
        this.b_expiry = new SimpleStringProperty(b_expiry);
        this.b_manufacturing_date = new SimpleStringProperty(b_manufacturing_date);
        this.isOpeningbatch = new SimpleStringProperty(isOpeningbatch);
        this.isUpdate = new SimpleStringProperty(isUpdate);
        this.batch_id = new SimpleStringProperty(batch_id);
    }

    public ProductRowBatchItem(String id, String b_no,String batch_id,  String opening_qty, String b_free_qty, String b_mrp, String b_sale_rate,
                               String b_purchase_rate, String b_costing, String b_expiry, String b_manufacturing_date) {
        this.id = new SimpleStringProperty(id);
        this.b_no = new SimpleStringProperty(b_no);
        this.opening_qty = new SimpleStringProperty(opening_qty);
        this.b_free_qty = new SimpleStringProperty(b_free_qty);
        this.b_mrp = new SimpleStringProperty(b_mrp);
        this.b_sale_rate = new SimpleStringProperty(b_sale_rate);
        this.b_purchase_rate = new SimpleStringProperty(b_purchase_rate);
        this.b_costing = new SimpleStringProperty(b_costing);
        this.b_expiry = new SimpleStringProperty(b_expiry);
        this.b_manufacturing_date = new SimpleStringProperty(b_manufacturing_date);
        this.batch_id = new SimpleStringProperty(batch_id);
    }

    public ProductRowBatchItem(Integer current_index, String b_no, String opening_qty, String b_free_qty, String b_mrp, String b_sale_rate,
                               String b_purchase_rate, String b_costing, String b_expiry, String b_manufacturing_date,
                               String isOpeningbatch, String isUpdate, String batch_id) {
        this.current_index = new SimpleStringProperty(String.valueOf(current_index));
        this.b_no = new SimpleStringProperty(b_no);
        this.opening_qty = new SimpleStringProperty(opening_qty);
        this.b_free_qty = new SimpleStringProperty(b_free_qty);
        this.b_mrp = new SimpleStringProperty(b_mrp);
        this.b_sale_rate = new SimpleStringProperty(b_sale_rate);
        this.b_purchase_rate = new SimpleStringProperty(b_purchase_rate);
        this.b_costing = new SimpleStringProperty(b_costing);
        this.b_expiry = new SimpleStringProperty(b_expiry);
        this.b_manufacturing_date = new SimpleStringProperty(b_manufacturing_date);
        this.isOpeningbatch = new SimpleStringProperty(isOpeningbatch);
        this.isUpdate = new SimpleStringProperty(isUpdate);
        this.batch_id = new SimpleStringProperty(batch_id);
    }

    // Getters and setters
    public String getId() {
        return id.get();
    }

    public String getCurrentIndex() {
        return current_index.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getB_no() {
        return b_no.get();
    }

    public SimpleStringProperty b_noProperty() {
        return b_no;
    }

    public void setB_no(String b_no) {
        this.b_no.set(b_no);
    }

    public String getOpening_qty() {
        return opening_qty.get();
    }

    public SimpleStringProperty opening_qtyProperty() {
        return opening_qty;
    }

    public void setOpening_qty(String opening_qty) {
        this.opening_qty.set(opening_qty);
    }

    public String getB_free_qty() {
        return b_free_qty.get();
    }

    public SimpleStringProperty b_free_qtyProperty() {
        return b_free_qty;
    }

    public void setB_free_qty(String b_free_qty) {
        this.b_free_qty.set(b_free_qty);
    }

    public String getB_mrp() {
        return b_mrp.get();
    }

    public SimpleStringProperty b_mrpProperty() {
        return b_mrp;
    }

    public void setB_mrp(String b_mrp) {
        this.b_mrp.set(b_mrp);
    }

    public String getB_sale_rate() {
        return b_sale_rate.get();
    }

    public SimpleStringProperty b_sale_rateProperty() {
        return b_sale_rate;
    }

    public void setB_sale_rate(String b_sale_rate) {
        this.b_sale_rate.set(b_sale_rate);
    }

    public String getB_purchase_rate() {
        return b_purchase_rate.get();
    }

    public SimpleStringProperty b_purchase_rateProperty() {
        return b_purchase_rate;
    }

    public void setB_purchase_rate(String b_purchase_rate) {
        this.b_purchase_rate.set(b_purchase_rate);
    }

    public String getB_costing() {
        return b_costing.get();
    }

    public SimpleStringProperty b_costingProperty() {
        return b_costing;
    }

    public void setB_costing(String b_costing) {
        this.b_costing.set(b_costing);
    }

    public String getB_expiry() {
        return b_expiry.get();
    }

    public SimpleStringProperty b_expiryProperty() {
        return b_expiry;
    }

    public void setB_expiry(String b_expiry) {
        this.b_expiry.set(b_expiry);
    }

    public String getB_manufacturing_date() {
        return b_manufacturing_date.get();
    }

    public SimpleStringProperty b_manufacturing_dateProperty() {
        return b_manufacturing_date;
    }

    public void setB_manufacturing_date(String b_manufacturing_date) {
        this.b_manufacturing_date.set(b_manufacturing_date);
    }

    public String getIsOpeningbatch() {
        return isOpeningbatch.get();
    }

    public SimpleStringProperty isOpeningbatchProperty() {
        return isOpeningbatch;
    }

    public void setIsOpeningbatch(String isOpeningbatch) {
        this.isOpeningbatch.set(isOpeningbatch);
    }

    public String getIsUpdate() {
        return isUpdate.get();
    }

    public SimpleStringProperty isUpdateProperty() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate.set(isUpdate);
    }

    public String getBatch_id() {
        return batch_id.get();
    }

    public SimpleStringProperty batch_idProperty() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id.set(batch_id);
    }



//    @Override
//    public String toString() {
//        return String.format("{ ID: %s, Batch: %s, Opening Qty: %s, Free Qty: %s, MRP: %s, Purchase Rate: %s, Costing: %s, Sale Rate: %s, Manufacturing Date: %s, Expiry Date: %s, Opening batch: %s, Update: %s, batch_id: %s}",
//                id.getValue(),
//                b_no.getValue(),
//                opening_qty.getValue(),
//                b_free_qty.getValue(),
//                b_mrp.getValue(),
//                b_purchase_rate.getValue(),
//                b_costing.getValue(),
//                b_sale_rate.getValue(),
//                b_manufacturing_date.getValue(),
//                b_expiry.getValue(),isOpeningbatch.getValue(),isUpdate.getValue(),batch_id.getValue());
//    }

    @Override
    public String toString() {
        return String.format("{ \"current_index\": \"%s\",\"id\": \"%s\", \"b_no\": \"%s\", \"opening_qty\": \"%s\", \"b_free_qty\": \"%s\", \"b_mrp\": \"%s\", \"b_sale_rate\": \"%s\", \"b_purchase_rate\": \"%s\", \"b_costing\": \"%s\", \"b_expiry\": \"%s\", \"b_manufacturing_date\": \"%s\", \"isOpeningbatch\": \"%s\", \"isUpdate\": \"%s\", \"batch_id\": \"%s\"}",
                current_index.getValue(),
                id.getValue(),
                b_no.getValue(),
                opening_qty.getValue(),
                b_free_qty.getValue(),
                b_mrp.getValue(),
                b_purchase_rate.getValue(),
                b_costing.getValue(),
                b_sale_rate.getValue(),
                b_manufacturing_date.getValue(),
                b_expiry.getValue(),
                isOpeningbatch.getValue(),
                isUpdate.getValue(),
                batch_id.getValue());
    }


}
