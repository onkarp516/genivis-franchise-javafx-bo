package com.opethic.genivis.models.tranx.sales;


public class TranxSelectedBatch {
    public int id;
    public boolean isExpired;
    public String purDate;
    public String manufactureDate;
    public String expiryDate;
    public double mrp;
    public double purchaseRate;

    public String productName;
    public String batchNo;
    private String unit1Name;
    private Integer unit1Id;
    private Double unit1FSRMH;
    private Double unit1FSRAI;
    private Double unit1CSRMH;
    private Double unit1CSRAI;
    private Double unit1Conv;
    private String unit1ClsStock;
    private Double unit1ActualStock;
    private String unit2Name;
    private Integer unit2Id;
    private Double unit2FSRMH;
    private Double unit2FSRAI;
    private Double unit2CSRMH;
    private Double unit2CSRAI;
    private Double unit2Conv;
    private String unit2ClsStock;
    private Double unit2ActualStock;

    private String unit3Name;
    private Integer unit3Id;
    private Double unit3FSRMH;
    private Double unit3FSRAI;
    private Double unit3CSRMH;
    private Double unit3CSRAI;
    private Double unit3Conv;
    private String unit3ClsStock;
    private Double unit3ActualStock;

    public TranxSelectedBatch(int id, boolean isExpired, String purDate, String manufactureDate, String expiryDate, double mrp, double purchaseRate, String productName, String batchNo, String unit1Name, Integer unit1Id, Double unit1FSRMH, Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI, Double unit1Conv, String unit1ClsStock, Double unit1ActualStock, String unit2Name, Integer unit2Id, Double unit2FSRMH, Double unit2FSRAI, Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, String unit2ClsStock, Double unit2ActualStock, String unit3Name, Integer unit3Id, Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI, Double unit3Conv, String unit3ClsStock, Double unit3ActualStock) {
        this.id = id;
        this.isExpired = isExpired;
        this.purDate = purDate;
        this.manufactureDate = manufactureDate;
        this.expiryDate = expiryDate;
        this.mrp = mrp;
        this.purchaseRate = purchaseRate;
        this.productName = productName;
        this.batchNo = batchNo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getPurDate() {
        return purDate;
    }

    public void setPurDate(String purDate) {
        this.purDate = purDate;
    }

    public String getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(String manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getMrp() {
        return mrp;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public double getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(double purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
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

    public Double getUnit1ActualStock() {
        return unit1ActualStock;
    }

    public void setUnit1ActualStock(Double unit1ActualStock) {
        this.unit1ActualStock = unit1ActualStock;
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


    public Double getUnit2ActualStock() {
        return unit2ActualStock;
    }

    public void setUnit2ActualStock(Double unit2ActualStock) {
        this.unit2ActualStock = unit2ActualStock;
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


    public Double getUnit3ActualStock() {
        return unit3ActualStock;
    }

    public void setUnit3ActualStock(Double unit3ActualStock) {
        this.unit3ActualStock = unit3ActualStock;
    }

    public String getUnit1ClsStock() {
        return unit1ClsStock;
    }

    public void setUnit1ClsStock(String unit1ClsStock) {
        this.unit1ClsStock = unit1ClsStock;
    }

    public String getUnit2ClsStock() {
        return unit2ClsStock;
    }

    public void setUnit2ClsStock(String unit2ClsStock) {
        this.unit2ClsStock = unit2ClsStock;
    }

    public String getUnit3ClsStock() {
        return unit3ClsStock;
    }

    public void setUnit3ClsStock(String unit3ClsStock) {
        this.unit3ClsStock = unit3ClsStock;
    }

    @Override
    public String toString() {
        return "TranxSelectedBatch{" +
                "id=" + id +
                ", isExpired=" + isExpired +
                ", purDate='" + purDate + '\'' +
                ", manufactureDate='" + manufactureDate + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", mrp=" + mrp +
                ", purchaseRate=" + purchaseRate +
                ", productName='" + productName + '\'' +
                ", batchNo='" + batchNo + '\'' +
                ", unit1Name='" + unit1Name + '\'' +
                ", unit1Id=" + unit1Id +
                ", unit1FSRMH=" + unit1FSRMH +
                ", unit1FSRAI=" + unit1FSRAI +
                ", unit1CSRMH=" + unit1CSRMH +
                ", unit1CSRAI=" + unit1CSRAI +
                ", unit1Conv=" + unit1Conv +
                ", unit1ClsStock=" + unit1ClsStock +
                ", unit1ActualStock=" + unit1ActualStock +
                ", unit2Name='" + unit2Name + '\'' +
                ", unit2Id=" + unit2Id +
                ", unit2FSRMH=" + unit2FSRMH +
                ", unit2FSRAI=" + unit2FSRAI +
                ", unit2CSRMH=" + unit2CSRMH +
                ", unit2CSRAI=" + unit2CSRAI +
                ", unit2Conv=" + unit2Conv +
                ", unit2ClsStock=" + unit2ClsStock +
                ", unit2ActualStock=" + unit2ActualStock +
                ", unit3Name='" + unit3Name + '\'' +
                ", unit3Id=" + unit3Id +
                ", unit3FSRMH=" + unit3FSRMH +
                ", unit3FSRAI=" + unit3FSRAI +
                ", unit3CSRMH=" + unit3CSRMH +
                ", unit3CSRAI=" + unit3CSRAI +
                ", unit3Conv=" + unit3Conv +
                ", unit3ClsStock=" + unit3ClsStock +
                ", unit3ActualStock=" + unit3ActualStock +
                '}';
    }
}
