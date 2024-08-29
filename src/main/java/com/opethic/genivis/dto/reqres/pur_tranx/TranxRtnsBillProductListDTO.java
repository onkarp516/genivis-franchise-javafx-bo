package com.opethic.genivis.dto.reqres.pur_tranx;

import javafx.scene.control.CheckBox;

public class TranxRtnsBillProductListDTO {
    private CheckBox isSelect;
    private String id;
    private String invoiceNo;
    private String productName;
    private String packageName;
    private String unitName;
    private String batchNo;
    private String expDate;
    private String rtnQty;
    private String qty;
    private String freeQty;
    private String rate;
    private Long prId;


    public Long getPrId() {
        return prId;
    }

    public void setPrId(Long prId) {
        this.prId = prId;
    }

    public TranxRtnsBillProductListDTO(CheckBox isSelect, String id, String invoiceNo, String productName,
                                       String packageName, String unitName, String batchNo, String expDate,
                                       String rtnQty, String qty, String freeQty, String rate, Long prId) {
        this.isSelect = isSelect;
        this.id = id;
        this.invoiceNo = invoiceNo;
        this.productName = productName;
        this.packageName = packageName;
        this.unitName = unitName;
        this.batchNo = batchNo;
        this.expDate = expDate;
        this.rtnQty = rtnQty;
        this.qty = qty;
        this.freeQty = freeQty;
        this.rate = rate;
        this.prId=prId;
    }

    public CheckBox getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(CheckBox isSelect) {
        this.isSelect = isSelect;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public TranxRtnsBillProductListDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getRtnQty() {
        return rtnQty;
    }

    public void setRtnQty(String rtnQty) {
        this.rtnQty = rtnQty;
    }

    public String getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(String freeQty) {
        this.freeQty = freeQty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
