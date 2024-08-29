package com.opethic.genivis.dto.cmp_t_row;

import javafx.beans.property.SimpleStringProperty;

public class BatchWindowTableDTO {
    private SimpleStringProperty currentIndex = new SimpleStringProperty("");
    private SimpleStringProperty productId = new SimpleStringProperty("");
    private SimpleStringProperty levelAId = new SimpleStringProperty("") ;
    private SimpleStringProperty levelBId = new SimpleStringProperty("");
    private SimpleStringProperty levelCId = new SimpleStringProperty("");
    private SimpleStringProperty unitId= new SimpleStringProperty("");
    private SimpleStringProperty batchNo= new SimpleStringProperty("");
    private SimpleStringProperty manufacturingDate= new SimpleStringProperty("");
    private SimpleStringProperty expiryDate= new SimpleStringProperty("");
    private SimpleStringProperty mrp= new SimpleStringProperty("");
    private SimpleStringProperty purchaseRate= new SimpleStringProperty("");
    private SimpleStringProperty quantity= new SimpleStringProperty("");
    private SimpleStringProperty free= new SimpleStringProperty("0");;
    private SimpleStringProperty discountPercentage= new SimpleStringProperty("");
    private SimpleStringProperty discountAmount= new SimpleStringProperty("");
    private SimpleStringProperty barcode= new SimpleStringProperty("");
    private SimpleStringProperty margin= new SimpleStringProperty("0");
    private SimpleStringProperty fsr_mh = new SimpleStringProperty("0.0");
    private SimpleStringProperty csr_mh= new SimpleStringProperty("0.0");

    private SimpleStringProperty fsr_ai= new SimpleStringProperty("0.0");
    private SimpleStringProperty csr_ai= new SimpleStringProperty("0.0");
    private SimpleStringProperty b_details_id = new SimpleStringProperty("0");

    private SimpleStringProperty supplier_id = new SimpleStringProperty("0");

    private SimpleStringProperty tax = new SimpleStringProperty("0");

    private SimpleStringProperty action= new SimpleStringProperty("");

    private SimpleStringProperty is_expired = new SimpleStringProperty("");

    private SimpleStringProperty cost_with_tax = new SimpleStringProperty("");

    private SimpleStringProperty cost_without_tax = new SimpleStringProperty("");

    public BatchWindowTableDTO(){

    }


    public BatchWindowTableDTO(String currentIndex, String productId, String levelAId, String levelBId, String levelCId, String unitId, String tax,String batchNo, String manufacturingDate, String expiryDate, String mrp, String purchaseRate, String quantity, String free, String discountPercentage, String discountAmount, String barcode, String margin, String fsr_mh, String csr_mh, String fsr_ai, String csr_ai, String b_details_id,String supplier_id,String action) {
        this.currentIndex = new SimpleStringProperty(currentIndex);
        this.productId = new SimpleStringProperty(productId);
        this.levelAId = new SimpleStringProperty(levelAId);
        this.levelBId = new SimpleStringProperty(levelBId);
        this.levelCId = new SimpleStringProperty(levelCId);
        this.unitId = new SimpleStringProperty(unitId);
        this.tax = new SimpleStringProperty(tax);
        this.batchNo = new SimpleStringProperty(batchNo);
        this.manufacturingDate = new SimpleStringProperty(manufacturingDate);
        this.expiryDate = new SimpleStringProperty(expiryDate);
        this.mrp = new SimpleStringProperty(mrp);
        this.purchaseRate = new SimpleStringProperty(purchaseRate);
        this.quantity = new SimpleStringProperty(quantity);
        this.free = new SimpleStringProperty(free);
        this.discountPercentage = new SimpleStringProperty(discountPercentage);
        this.discountAmount = new SimpleStringProperty(discountAmount);
        this.barcode = new SimpleStringProperty(barcode);
        this.margin = new SimpleStringProperty(margin);
        this.fsr_mh = new SimpleStringProperty(fsr_mh);
        this.csr_mh = new SimpleStringProperty(csr_mh);
        this.fsr_ai = new SimpleStringProperty(fsr_ai);
        this.csr_ai = new SimpleStringProperty(csr_ai);
        this.b_details_id = new SimpleStringProperty(b_details_id);
        this.supplier_id = new SimpleStringProperty(supplier_id);
        this.action = new SimpleStringProperty(action);
    }

    public String getCost_with_tax() {
        return cost_with_tax.get();
    }

    public SimpleStringProperty cost_with_taxProperty() {
        return cost_with_tax;
    }

    public void setCost_with_tax(String cost_with_tax) {
        this.cost_with_tax.set(cost_with_tax);
    }

    public String getCost_without_tax() {
        return cost_without_tax.get();
    }

    public SimpleStringProperty cost_without_taxProperty() {
        return cost_without_tax;
    }

    public void setCost_without_tax(String cost_without_tax) {
        this.cost_without_tax.set(cost_without_tax);
    }

    public String getIs_expired() {
        return is_expired.get();
    }

    public SimpleStringProperty is_expiredProperty() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired.set(is_expired);
    }

    public String getTax() {
        return tax.get();
    }

    public SimpleStringProperty taxProperty() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax.set(tax);
    }

    public String getSupplier_id() {
        return supplier_id.get();
    }

    public SimpleStringProperty supplier_idProperty() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id.set(supplier_id);
    }

    public String getFsr_mh() {
        return fsr_mh.get();
    }

    public SimpleStringProperty fsr_mhProperty() {
        return fsr_mh;
    }

    public void setFsr_mh(String fsr_mh) {
        this.fsr_mh.set(fsr_mh);
    }

    public String getCsr_mh() {
        return csr_mh.get();
    }

    public SimpleStringProperty csr_mhProperty() {
        return csr_mh;
    }

    public void setCsr_mh(String csr_mh) {
        this.csr_mh.set(csr_mh);
    }

    public String getFsr_ai() {
        return fsr_ai.get();
    }

    public SimpleStringProperty fsr_aiProperty() {
        return fsr_ai;
    }

    public void setFsr_ai(String fsr_ai) {
        this.fsr_ai.set(fsr_ai);
    }

    public String getCsr_ai() {
        return csr_ai.get();
    }

    public SimpleStringProperty csr_aiProperty() {
        return csr_ai;
    }

    public void setCsr_ai(String csr_ai) {
        this.csr_ai.set(csr_ai);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }

    public String getCurrentIndex() {
        return currentIndex.get();
    }

    public SimpleStringProperty currentIndexProperty() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex.set(currentIndex);
    }

    public String getProductId() {
        return productId.get();
    }

    public SimpleStringProperty productIdProperty() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId.set(productId);
    }

    public String getLevelAId() {
        return levelAId.get();
    }

    public SimpleStringProperty levelAIdProperty() {
        return levelAId;
    }

    public void setLevelAId(String levelAId) {
        this.levelAId.set(levelAId);
    }

    public String getLevelBId() {
        return levelBId.get();
    }

    public SimpleStringProperty levelBIdProperty() {
        return levelBId;
    }

    public void setLevelBId(String levelBId) {
        this.levelBId.set(levelBId);
    }

    public String getLevelCId() {
        return levelCId.get();
    }

    public SimpleStringProperty levelCIdProperty() {
        return levelCId;
    }

    public void setLevelCId(String levelCId) {
        this.levelCId.set(levelCId);
    }

    public String getUnitId() {
        return unitId.get();
    }

    public SimpleStringProperty unitIdProperty() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId.set(unitId);
    }

    public String getBatchNo() {
        return batchNo.get();
    }

    public SimpleStringProperty batchNoProperty() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo.set(batchNo);
    }

    public String getManufacturingDate() {
        return manufacturingDate.get();
    }

    public SimpleStringProperty manufacturingDateProperty() {
        return manufacturingDate;
    }

    public void setManufacturingDate(String manufacturingDate) {
        this.manufacturingDate.set(manufacturingDate);
    }

    public String getExpiryDate() {
        return expiryDate.get();
    }

    public SimpleStringProperty expiryDateProperty() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate.set(expiryDate);
    }

    public String getMrp() {
        return mrp.get();
    }

    public SimpleStringProperty mrpProperty() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp);
    }

    public String getPurchaseRate() {
        return purchaseRate.get();
    }

    public SimpleStringProperty purchaseRateProperty() {
        return purchaseRate;
    }

    public void setPurchaseRate(String purchaseRate) {
        this.purchaseRate.set(purchaseRate);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getFree() {
        return free.get();
    }

    public SimpleStringProperty freeProperty() {
        return free;
    }

    public void setFree(String free) {
        this.free.set(free);
    }

    public String getDiscountPercentage() {
        return discountPercentage.get();
    }

    public SimpleStringProperty discountPercentageProperty() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        this.discountPercentage.set(discountPercentage);
    }

    public String getDiscountAmount() {
        return discountAmount.get();
    }

    public SimpleStringProperty discountAmountProperty() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount.set(discountAmount);
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

    public String getMargin() {
        return margin.get();
    }

    public SimpleStringProperty marginProperty() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin.set(margin);
    }

    public String getB_details_id() {
        return b_details_id.get();
    }

    public SimpleStringProperty b_details_idProperty() {
        return b_details_id;
    }

    public void setB_details_id(String b_details_id) {
        this.b_details_id.set(b_details_id);
    }

//    @Override
//    public String toString() {
//        return "BatchWindowTableDTO{" +
//                "currentIndex=" + currentIndex +
//                ", productId=" + productId +
//                ", levelAId=" + levelAId +
//                ", levelBId=" + levelBId +
//                ", levelCId=" + levelCId +
//                ", unitId=" + unitId +
//                ", batchNo=" + batchNo +
//                ", manufacturingDate=" + manufacturingDate +
//                ", expiryDate=" + expiryDate +
//                ", mrp=" + mrp +
//                ", purchaseRate=" + purchaseRate +
//                ", quantity=" + quantity +
//                ", free=" + free +
//                ", discountPercentage=" + discountPercentage +
//                ", discountAmount=" + discountAmount +
//                ", barcode=" + barcode +
//                ", margin=" + margin +
//                ", fsr_mh=" + fsr_mh +
//                ", csr_mh=" + csr_mh +
//                ", fsr_ai=" + fsr_ai +
//                ", csr_ai=" + csr_ai +
//                ", b_details_id=" + b_details_id +
//                ", supplier_id=" + supplier_id +
//                ", tax=" + tax +
//                ", action=" + action +
//                '}';
//    }
    @Override
    public String toString() {
        return "BatchWindowTableDTO{" +
                "currentIndex=" + currentIndex +
                ", productId=" + productId +
                ", levelAId=" + levelAId +
                ", levelBId=" + levelBId +
                ", levelCId=" + levelCId +
                ", unitId=" + unitId +
                ", batchNo=" + batchNo +
                ", manufacturingDate=" + manufacturingDate +
                ", expiryDate=" + expiryDate +
                ", mrp=" + mrp +
                ", purchaseRate=" + purchaseRate +
                ", quantity=" + quantity +
                ", free=" + free +
                ", discountPercentage=" + discountPercentage +
                ", discountAmount=" + discountAmount +
                ", barcode=" + barcode +
                ", margin=" + margin +
                ", fsr_mh=" + fsr_mh +
                ", csr_mh=" + csr_mh +
                ", fsr_ai=" + fsr_ai +
                ", csr_ai=" + csr_ai +
                ", b_details_id=" + b_details_id +
                ", supplier_id=" + supplier_id +
                ", tax=" + tax +
                ", action=" + action +
                ", is_expired=" + is_expired +
                '}';
    }
}
