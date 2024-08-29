package com.opethic.genivis.dto;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductRowDTO {

    private SimpleStringProperty id = new SimpleStringProperty("0");
    private SimpleStringProperty current_index;
    private SimpleStringProperty selectedLevelA;
    private SimpleStringProperty selectedLevelB;
    private SimpleStringProperty selectedLevelC;
    private SimpleStringProperty selectedUnit;
    private SimpleStringProperty conv;
    private SimpleStringProperty mrp;
    private SimpleStringProperty pur_rate;
    private SimpleStringProperty rate_1;
    private SimpleStringProperty rate_2;
    private SimpleStringProperty rate_3;
    private SimpleStringProperty rate_4;
    private SimpleStringProperty opn_stock;
    private SimpleStringProperty is_negetive;

    private SimpleStringProperty is_rate;

    private List<ProductRowBatchItem> batchList;

    public ProductRowDTO() {
    }

    public ProductRowDTO(String current_index, String selectedLevelA, String selectedLevelB, String selectedLevelC, String selectedUnit, String conv, String mrp, String pur_rate, String rate_1, String rate_2, String rate_3, String rate_4, String opn_stock, String is_negetive,String is_rate) {
        this.current_index =new SimpleStringProperty(current_index);
        this.selectedLevelA = new SimpleStringProperty(selectedLevelA);
        this.selectedLevelB = new SimpleStringProperty(selectedLevelB) ;
        this.selectedLevelC = new SimpleStringProperty(selectedLevelC) ;
        this.selectedUnit =new SimpleStringProperty(selectedUnit) ;
        this.conv =new SimpleStringProperty(conv) ;
        this.mrp =new SimpleStringProperty(mrp) ;
        this.pur_rate =new SimpleStringProperty(pur_rate) ;
        this.rate_1 =new SimpleStringProperty(rate_1) ;
        this.rate_2 =new SimpleStringProperty(rate_2) ;
        this.rate_3 =new SimpleStringProperty(rate_3) ;
        this.rate_4 =new SimpleStringProperty(rate_4) ;
        this.opn_stock =new SimpleStringProperty(opn_stock) ;
        this.is_negetive =new SimpleStringProperty(is_negetive) ;
        this.is_rate =new SimpleStringProperty(is_rate) ;

    }

    public String getIs_rate() {
        return is_rate.get();
    }

    public SimpleStringProperty is_rateProperty() {
        return is_rate;
    }

    public void setIs_rate(String is_rate) {
        this.is_rate.set(is_rate);
    }

    public String getRate_4() {
        return rate_4.get();
    }

    public SimpleStringProperty rate_4Property() {
        return rate_4;
    }

    public void setRate_4(String rate_4) {
        this.rate_4.set(rate_4);
    }

    public String getCurrent_index() {
        return current_index.get();
    }

    public String getId(){
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id) ;
    }
    public String getSelectedLevelA() {
        return selectedLevelA.get();
    }

    public StringProperty levelAProperty() {
        return selectedLevelA;
    }

    public String getSelectedLevelB() {
        return selectedLevelB.get();
    }

    public StringProperty levelBProperty() {
        return selectedLevelB;
    }

    public String getSelectedLevelC() {
        return selectedLevelC.get();
    }

    public StringProperty levelCProperty() {
        return selectedLevelC;
    }

    public String getSelectedUnit() {
        return selectedUnit.get();
    }

    public StringProperty unitProperty() {
        return selectedUnit;
    }

    public String getConv() {
        return conv.get();
    }

    public StringProperty convProperty() {
        return conv;
    }

    public String getMrp() {
        return mrp.get();
    }

    public StringProperty mrpProperty() {
        return mrp;
    }

    public String getPurRate() {
        return pur_rate.get();
    }

    public StringProperty purRateProperty() {
        return pur_rate;
    }

    public String getRate_1() {
        return rate_1.get();
    }

    public StringProperty rate_1Property() {
        return rate_1;
    }

    public String getRate_2() {
        return rate_2.get();
    }

    public StringProperty rate_2Property() {
        return rate_2;
    }

    public String getRate_3() {
        return rate_3.get();
    }

    public StringProperty rate_3Property() {
        return rate_3;
    }

    public String getOpn_stock() {
        return opn_stock.get();
    }

    public StringProperty opnStockProperty() {
        return opn_stock;
    }

    public String getIs_negetive() {
        return is_negetive.get();
    }

    public StringProperty is_negetiveProperty() {
        return is_negetive;
    }

    public void setCurrent_index(String current_index) {
        this.current_index.set(current_index);
    }

    public void setSelectedLevelA(String selectedLevelA) {
        this.selectedLevelA.set(selectedLevelA) ;
    }

    public void setSelectedLevelB(String selectedLevelB) {
        this.selectedLevelB.set(selectedLevelB);
    }

    public void setSelectedLevelC(String selectedLevelC) {
        this.selectedLevelC.set(selectedLevelC);
    }

    public void setSelectedUnit(String selectedUnit) {
        this.selectedUnit.set(selectedUnit) ;
    }

    public void setConv(String conv) {
        this.conv.set(conv);
    }

    public void setMrp(String mrp) {
        this.mrp.set(mrp) ;
    }

    public void setPur_rate(String pur_rate) {
        this.pur_rate.set(pur_rate) ;
    }

    public void setRate_1(String rate_1) {
        this.rate_1.set(rate_1) ;
    }

    public void setRate_2(String rate_2) {
        this.rate_2.set(rate_2);
    }

    public void setRate_3(String rate_3) {
        this.rate_3.set(rate_3);
    }

    public void setOpn_stock(String opn_stock) {
        this.opn_stock.set(opn_stock) ;
    }

    public void setIs_negetive(String is_negetive) {
        this.is_negetive.set(is_negetive);
    }

    public List<ProductRowBatchItem> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<ProductRowBatchItem> batchList) {
        this.batchList = batchList;
    }

    public void addBatchItem(ProductRowBatchItem batchItem) {
        batchList.add(batchItem);
    }

//    @Override
//    public String toString() {
//        return "ProductRowDTO{" +
//                "current_index=" + current_index +
//                ", selectedLevelA=" + selectedLevelA +
//                ", selectedLevelB=" + selectedLevelB +
//                ", selectedLevelC=" + selectedLevelC +
//                ", selectedUnit=" + selectedUnit +
//                ", conv=" + conv +
//                ", mrp=" + mrp +
//                ", pur_rate=" + pur_rate +
//                ", rate_1=" + rate_1 +
//                ", rate_2=" + rate_2 +
//                ", rate_3=" + rate_3 +
//                ", opn_stock=" + opn_stock +
//                ", is_negetive=" + is_negetive +
//                '}';
//    }

    @Override
    public String toString() {
        return String.format("{\"id\": \"%s\", \"current_index\": \"%s\", \"selectedLevelA\": \"%s\", \"selectedLevelB\": \"%s\", \"selectedLevelC\": \"%s\", \"selectedUnit\": \"%s\", \"conv\": \"%s\", \"mrp\": \"%s\", \"pur_rate\": \"%s\", \"rate_1\": \"%s\", \"rate_2\": \"%s\", \"rate_3\": \"%s\",\"rate_4\": \"%s\", \"opn_stock\": \"%s\", \"is_negetive\": \"%s\", \"is_rate\": \"%s\", \"batchList\":%s}",
                id.getValue(),
                current_index.getValue(),
                selectedLevelA.getValue(),
                selectedLevelB.getValue(),
                selectedLevelC.getValue(),
                selectedUnit.getValue(),
                conv.getValue(),
                mrp.getValue(),
                pur_rate.getValue(),
                rate_1.getValue(),
                rate_2.getValue(),
                rate_3.getValue(),
                rate_4.getValue(),
                opn_stock.getValue(),
                is_negetive.getValue(),
                is_rate.getValue(),
                batchList);

    }
}
