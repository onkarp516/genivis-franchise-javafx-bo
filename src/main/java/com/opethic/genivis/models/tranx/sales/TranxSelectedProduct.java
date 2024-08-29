package com.opethic.genivis.models.tranx.sales;

public class TranxSelectedProduct {
    private Integer productId;
    private String productName;
    private Double igst;
    private Double cgst;
    private Double sgst;
    private Integer packageId;
    private String packageName;
    private String code;
    private String barCode;
    private Boolean isNegetive;
    private Boolean isBatch;
    private Boolean isSerial;
    private Double rateA;
    private Double rateB;
    private Double rateC;
    private Double currStock;
    private Boolean isLevelA;
    private Boolean isLevelB;
    private Boolean isLevelC;

    private Integer levelAId;
    private String dispLevelA;
    private Integer levelBId;
    private String dispLevelB;
    private Integer levelCId;
    private String dispLevelC;

    private String brandName;
    private String unitName;
    private Double salesRate;
    private String drugType;

    private Double mrp;

    private Integer unit1Id;
    private String unit1Name;
    private Double unit1Mrp;
    private Double unit1PurchaseRate;
    private Double unit1FSRMH;
    private Double unit1FSRAI;
    private Double unit1CSRMH;
    private Double unit1CSRAI;
    private Double unit1Conv;
    private Double unit1ClsStock;
    private Double unit1ActualStock;
    private Boolean unit1IsNegetive;


    private Integer unit2Id;
    private String unit2Name;
    private Double unit2Mrp;
    private Double unit2PurchaseRate;
    private Double unit2FSRMH;
    private Double unit2FSRAI;
    private Double unit2CSRMH;
    private Double unit2CSRAI;
    private Double unit2Conv;
    private Double unit2ClsStock;
    private Double unit2ActualStock;
    private Boolean unit2IsNegetive;

    private Integer unit3Id;
    private String unit3Name;
    private Double unit3Mrp;
    private Double unit3PurchaseRate;
    private Double unit3FSRMH;
    private Double unit3FSRAI;
    private Double unit3CSRMH;
    private Double unit3CSRAI;
    private Double unit3Conv;
    private Double unit3ClsStock;
    private Double unit3ActualStock;
    private Boolean unit3IsNegetive;


    public TranxSelectedProduct(Integer productId, String productName, Double igst, Double cgst, Double sgst, Integer packageId, String packageName,
                                String code, String barCode, Boolean isNegetive, Boolean isBatch, Boolean isSerial, Double rateA, Double rateB,
                                Double rateC, Double currStock, Boolean isLevelA, Boolean isLevelB, Boolean isLevelC, Integer levelAId, String dispLevelA,
                                Integer levelBId, String dispLevelB, Integer levelCId, String dispLevelC, String brandName, String unitName, Double salesRate,
                                Double mrp, String unit1Name, Integer unit1Id, Double unit1FSRMH, Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI,
                                Double unit1Conv, Double unit1ClsStock, Double unit1ActualStock, String unit2Name, Integer unit2Id, Double unit2FSRMH,
                                Double unit2FSRAI, Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, Double unit2ClsStock, Double unit2ActualStock,
                                String unit3Name, Integer unit3Id, Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI, Double unit3Conv,
                                Double unit3ClsStock, Double unit3ActualStock) {
        this.productId = productId;
        this.productName = productName;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.packageId = packageId;
        this.packageName = packageName;
        this.code = code;
        this.barCode = barCode;
        this.isNegetive = isNegetive;
        this.isBatch = isBatch;
        this.isSerial = isSerial;
        this.rateA = rateA;
        this.rateB = rateB;
        this.rateC = rateC;
        this.currStock = currStock;
        this.isLevelA = isLevelA;
        this.isLevelB = isLevelB;
        this.isLevelC = isLevelC;
        this.levelAId = levelAId;
        this.dispLevelA = dispLevelA;
        this.levelBId = levelBId;
        this.dispLevelB = dispLevelB;
        this.levelCId = levelCId;
        this.dispLevelC = dispLevelC;
        this.brandName = brandName;
        this.unitName = unitName;
        this.salesRate = salesRate;
        this.mrp = mrp;
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

    public TranxSelectedProduct(Integer productId, String productName, Double igst, Double cgst, Double sgst, Integer packageId, String packageName,
                                String code, String barCode, Boolean isNegetive, Boolean isBatch, Boolean isSerial, Double rateA, Double rateB,
                                Double rateC, Double currStock, Boolean isLevelA, Boolean isLevelB, Boolean isLevelC, Integer levelAId, String dispLevelA,
                                Integer levelBId, String dispLevelB, Integer levelCId, String dispLevelC, String brandName, String unitName, Double salesRate,
                                Double mrp, String unit1Name, Integer unit1Id, Double unit1FSRMH, Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI,
                                Double unit1Conv, Double unit1ClsStock, Double unit1ActualStock, String unit2Name, Integer unit2Id, Double unit2FSRMH,
                                Double unit2FSRAI, Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, Double unit2ClsStock, Double unit2ActualStock,
                                String unit3Name, Integer unit3Id, Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI, Double unit3Conv,
                                Double unit3ClsStock, Double unit3ActualStock, String drugType) {
        this.productId = productId;
        this.productName = productName;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.packageId = packageId;
        this.packageName = packageName;
        this.code = code;
        this.barCode = barCode;
        this.isNegetive = isNegetive;
        this.isBatch = isBatch;
        this.isSerial = isSerial;
        this.rateA = rateA;
        this.rateB = rateB;
        this.rateC = rateC;
        this.currStock = currStock;
        this.isLevelA = isLevelA;
        this.isLevelB = isLevelB;
        this.isLevelC = isLevelC;
        this.levelAId = levelAId;
        this.dispLevelA = dispLevelA;
        this.levelBId = levelBId;
        this.dispLevelB = dispLevelB;
        this.levelCId = levelCId;
        this.dispLevelC = dispLevelC;
        this.brandName = brandName;
        this.unitName = unitName;
        this.salesRate = salesRate;
        this.mrp = mrp;
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
        this.drugType = drugType;
    }


    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Boolean getNegetive() {
        return isNegetive;
    }

    public void setNegetive(Boolean negetive) {
        isNegetive = negetive;
    }

    public Boolean getBatch() {
        return isBatch;
    }

    public void setBatch(Boolean batch) {
        isBatch = batch;
    }

    public Boolean getSerial() {
        return isSerial;
    }

    public void setSerial(Boolean serial) {
        isSerial = serial;
    }

    public Double getRateA() {
        return rateA;
    }

    public void setRateA(Double rateA) {
        this.rateA = rateA;
    }

    public Double getRateB() {
        return rateB;
    }

    public void setRateB(Double rateB) {
        this.rateB = rateB;
    }

    public Double getRateC() {
        return rateC;
    }

    public void setRateC(Double rateC) {
        this.rateC = rateC;
    }

    public Double getCurrStock() {
        return currStock;
    }

    public void setCurrStock(Double currStock) {
        this.currStock = currStock;
    }

    public Boolean getLevelA() {
        return isLevelA;
    }

    public void setLevelA(Boolean levelA) {
        isLevelA = levelA;
    }

    public Boolean getLevelB() {
        return isLevelB;
    }

    public void setLevelB(Boolean levelB) {
        isLevelB = levelB;
    }

    public Boolean getLevelC() {
        return isLevelC;
    }

    public void setLevelC(Boolean levelC) {
        isLevelC = levelC;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getSalesRate() {
        return salesRate;
    }

    public void setSalesRate(Double salesRate) {
        this.salesRate = salesRate;
    }

    public Double getMrp() {
        return mrp;
    }

    public void setMrp(Double mrp) {
        this.mrp = mrp;
    }

    public Integer getLevelAId() {
        return levelAId;
    }

    public void setLevelAId(Integer levelAId) {
        this.levelAId = levelAId;
    }

    public String getDispLevelA() {
        return dispLevelA;
    }

    public void setDispLevelA(String dispLevelA) {
        this.dispLevelA = dispLevelA;
    }

    public Integer getLevelBId() {
        return levelBId;
    }

    public void setLevelBId(Integer levelBId) {
        this.levelBId = levelBId;
    }

    public String getDispLevelB() {
        return dispLevelB;
    }

    public void setDispLevelB(String dispLevelB) {
        this.dispLevelB = dispLevelB;
    }

    public Integer getLevelCId() {
        return levelCId;
    }

    public void setLevelCId(Integer levelCId) {
        this.levelCId = levelCId;
    }

    public String getDispLevelC() {
        return dispLevelC;
    }

    public void setDispLevelC(String dispLevelC) {
        this.dispLevelC = dispLevelC;
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


    public TranxSelectedProduct(Integer productId, String productName, Double igst, Double cgst, Double sgst, Integer packageId, String packageName, String code, String barCode, Boolean isNegetive, Boolean isBatch, Boolean isSerial, Double rateA, Double rateB, Double rateC, Double currStock, Boolean isLevelA, Boolean isLevelB, Boolean isLevelC, Integer levelAId, String dispLevelA, Integer levelBId, String dispLevelB, Integer levelCId, String dispLevelC, String brandName, String unitName, Double salesRate, String drugType, Double mrp, String unit1Name, Integer unit1Id, Double unit1FSRMH, Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI, Double unit1Conv, Double unit1ClsStock, Double unit1ActualStock, Boolean unit1IsNegetive, String unit2Name, Integer unit2Id, Double unit2FSRMH, Double unit2FSRAI, Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, Double unit2ClsStock, Double unit2ActualStock, Boolean unit2IsNegetive, String unit3Name, Integer unit3Id, Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI, Double unit3Conv, Double unit3ClsStock, Double unit3ActualStock, Boolean unit3IsNegetive) {
        this.productId = productId;
        this.productName = productName;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.packageId = packageId;
        this.packageName = packageName;
        this.code = code;
        this.barCode = barCode;
        this.isNegetive = isNegetive;
        this.isBatch = isBatch;
        this.isSerial = isSerial;
        this.rateA = rateA;
        this.rateB = rateB;
        this.rateC = rateC;
        this.currStock = currStock;
        this.isLevelA = isLevelA;
        this.isLevelB = isLevelB;
        this.isLevelC = isLevelC;
        this.levelAId = levelAId;
        this.dispLevelA = dispLevelA;
        this.levelBId = levelBId;
        this.dispLevelB = dispLevelB;
        this.levelCId = levelCId;
        this.dispLevelC = dispLevelC;
        this.brandName = brandName;
        this.unitName = unitName;
        this.salesRate = salesRate;
        this.drugType = drugType;
        this.mrp = mrp;
        this.unit1Name = unit1Name;
        this.unit1Id = unit1Id;
        this.unit1FSRMH = unit1FSRMH;
        this.unit1FSRAI = unit1FSRAI;
        this.unit1CSRMH = unit1CSRMH;
        this.unit1CSRAI = unit1CSRAI;
        this.unit1Conv = unit1Conv;
        this.unit1ClsStock = unit1ClsStock;
        this.unit1ActualStock = unit1ActualStock;
        this.unit1IsNegetive = unit1IsNegetive;
        this.unit2Name = unit2Name;
        this.unit2Id = unit2Id;
        this.unit2FSRMH = unit2FSRMH;
        this.unit2FSRAI = unit2FSRAI;
        this.unit2CSRMH = unit2CSRMH;
        this.unit2CSRAI = unit2CSRAI;
        this.unit2Conv = unit2Conv;
        this.unit2ClsStock = unit2ClsStock;
        this.unit2ActualStock = unit2ActualStock;
        this.unit2IsNegetive = unit2IsNegetive;
        this.unit3Name = unit3Name;
        this.unit3Id = unit3Id;
        this.unit3FSRMH = unit3FSRMH;
        this.unit3FSRAI = unit3FSRAI;
        this.unit3CSRMH = unit3CSRMH;
        this.unit3CSRAI = unit3CSRAI;
        this.unit3Conv = unit3Conv;
        this.unit3ClsStock = unit3ClsStock;
        this.unit3ActualStock = unit3ActualStock;
        this.unit3IsNegetive = unit3IsNegetive;
    }

    public Boolean getUnit1IsNegetive() {
        return unit1IsNegetive;
    }

    public void setUnit1IsNegetive(Boolean unit1IsNegetive) {
        this.unit1IsNegetive = unit1IsNegetive;
    }

    public Boolean getUnit2IsNegetive() {
        return unit2IsNegetive;
    }

    public void setUnit2IsNegetive(Boolean unit2IsNegetive) {
        this.unit2IsNegetive = unit2IsNegetive;
    }

    public Boolean getUnit3IsNegetive() {
        return unit3IsNegetive;
    }

    public void setUnit3IsNegetive(Boolean unit3IsNegetive) {
        this.unit3IsNegetive = unit3IsNegetive;
    }

    public Double getUnit1Mrp() {
        return unit1Mrp;
    }

    public void setUnit1Mrp(Double unit1Mrp) {
        this.unit1Mrp = unit1Mrp;
    }

    public Double getUnit1PurchaseRate() {
        return unit1PurchaseRate;
    }

    public void setUnit1PurchaseRate(Double unit1PurchaseRate) {
        this.unit1PurchaseRate = unit1PurchaseRate;
    }

    public Double getUnit2Mrp() {
        return unit2Mrp;
    }

    public void setUnit2Mrp(Double unit2Mrp) {
        this.unit2Mrp = unit2Mrp;
    }

    public Double getUnit2PurchaseRate() {
        return unit2PurchaseRate;
    }

    public void setUnit2PurchaseRate(Double unit2PurchaseRate) {
        this.unit2PurchaseRate = unit2PurchaseRate;
    }

    public Double getUnit3Mrp() {
        return unit3Mrp;
    }

    public void setUnit3Mrp(Double unit3Mrp) {
        this.unit3Mrp = unit3Mrp;
    }

    public Double getUnit3PurchaseRate() {
        return unit3PurchaseRate;
    }

    public void setUnit3PurchaseRate(Double unit3PurchaseRate) {
        this.unit3PurchaseRate = unit3PurchaseRate;
    }

    //!Final Constructor
    public TranxSelectedProduct(Integer productId, String productName, Double igst, Double cgst, Double sgst, Integer packageId, String packageName, String code, String barCode, Boolean isNegetive, Boolean isBatch, Boolean isSerial, Double rateA, Double rateB, Double rateC, Double currStock, Boolean isLevelA, Boolean isLevelB, Boolean isLevelC, Integer levelAId, String dispLevelA, Integer levelBId, String dispLevelB, Integer levelCId, String dispLevelC, String brandName, String unitName, Double salesRate, String drugType, Double mrp, Integer unit1Id, String unit1Name, Double unit1Mrp, Double unit1PurchaseRate, Double unit1FSRMH, Double unit1FSRAI, Double unit1CSRMH, Double unit1CSRAI, Double unit1Conv, Double unit1ClsStock, Double unit1ActualStock, Boolean unit1IsNegetive, Integer unit2Id, String unit2Name, Double unit2Mrp, Double unit2PurchaseRate, Double unit2FSRMH, Double unit2FSRAI, Double unit2CSRMH, Double unit2CSRAI, Double unit2Conv, Double unit2ClsStock, Double unit2ActualStock, Boolean unit2IsNegetive, Integer unit3Id, String unit3Name, Double unit3Mrp, Double unit3PurchaseRate, Double unit3FSRMH, Double unit3FSRAI, Double unit3CSRMH, Double unit3CSRAI, Double unit3Conv, Double unit3ClsStock, Double unit3ActualStock, Boolean unit3IsNegetive) {
        this.productId = productId;
        this.productName = productName;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.packageId = packageId;
        this.packageName = packageName;
        this.code = code;
        this.barCode = barCode;
        this.isNegetive = isNegetive;
        this.isBatch = isBatch;
        this.isSerial = isSerial;
        this.rateA = rateA;
        this.rateB = rateB;
        this.rateC = rateC;
        this.currStock = currStock;
        this.isLevelA = isLevelA;
        this.isLevelB = isLevelB;
        this.isLevelC = isLevelC;
        this.levelAId = levelAId;
        this.dispLevelA = dispLevelA;
        this.levelBId = levelBId;
        this.dispLevelB = dispLevelB;
        this.levelCId = levelCId;
        this.dispLevelC = dispLevelC;
        this.brandName = brandName;
        this.unitName = unitName;
        this.salesRate = salesRate;
        this.drugType = drugType;
        this.mrp = mrp;
        this.unit1Id = unit1Id;
        this.unit1Name = unit1Name;
        this.unit1Mrp = unit1Mrp;
        this.unit1PurchaseRate = unit1PurchaseRate;
        this.unit1FSRMH = unit1FSRMH;
        this.unit1FSRAI = unit1FSRAI;
        this.unit1CSRMH = unit1CSRMH;
        this.unit1CSRAI = unit1CSRAI;
        this.unit1Conv = unit1Conv;
        this.unit1ClsStock = unit1ClsStock;
        this.unit1ActualStock = unit1ActualStock;
        this.unit1IsNegetive = unit1IsNegetive;
        this.unit2Id = unit2Id;
        this.unit2Name = unit2Name;
        this.unit2Mrp = unit2Mrp;
        this.unit2PurchaseRate = unit2PurchaseRate;
        this.unit2FSRMH = unit2FSRMH;
        this.unit2FSRAI = unit2FSRAI;
        this.unit2CSRMH = unit2CSRMH;
        this.unit2CSRAI = unit2CSRAI;
        this.unit2Conv = unit2Conv;
        this.unit2ClsStock = unit2ClsStock;
        this.unit2ActualStock = unit2ActualStock;
        this.unit2IsNegetive = unit2IsNegetive;
        this.unit3Id = unit3Id;
        this.unit3Name = unit3Name;
        this.unit3Mrp = unit3Mrp;
        this.unit3PurchaseRate = unit3PurchaseRate;
        this.unit3FSRMH = unit3FSRMH;
        this.unit3FSRAI = unit3FSRAI;
        this.unit3CSRMH = unit3CSRMH;
        this.unit3CSRAI = unit3CSRAI;
        this.unit3Conv = unit3Conv;
        this.unit3ClsStock = unit3ClsStock;
        this.unit3ActualStock = unit3ActualStock;
        this.unit3IsNegetive = unit3IsNegetive;
    }

    @Override
    public String toString() {
        return "TranxSelectedProduct{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", igst=" + igst +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", packageId=" + packageId +
                ", packageName='" + packageName + '\'' +
                ", code='" + code + '\'' +
                ", barCode='" + barCode + '\'' +
                ", isNegetive=" + isNegetive +
                ", isBatch=" + isBatch +
                ", isSerial=" + isSerial +
                ", rateA=" + rateA +
                ", rateB=" + rateB +
                ", rateC=" + rateC +
                ", currStock=" + currStock +
                ", isLevelA=" + isLevelA +
                ", isLevelB=" + isLevelB +
                ", isLevelC=" + isLevelC +
                ", levelAId=" + levelAId +
                ", dispLevelA='" + dispLevelA + '\'' +
                ", levelBId=" + levelBId +
                ", dispLevelB='" + dispLevelB + '\'' +
                ", levelCId=" + levelCId +
                ", dispLevelC='" + dispLevelC + '\'' +
                ", brandName='" + brandName + '\'' +
                ", unitName='" + unitName + '\'' +
                ", salesRate=" + salesRate +
                ", drugType='" + drugType + '\'' +
                ", mrp=" + mrp +
                ", unit1Id=" + unit1Id +
                ", unit1Name='" + unit1Name + '\'' +
                ", unit1Mrp=" + unit1Mrp +
                ", unit1PurchaseRate=" + unit1PurchaseRate +
                ", unit1FSRMH=" + unit1FSRMH +
                ", unit1FSRAI=" + unit1FSRAI +
                ", unit1CSRMH=" + unit1CSRMH +
                ", unit1CSRAI=" + unit1CSRAI +
                ", unit1Conv=" + unit1Conv +
                ", unit1ClsStock=" + unit1ClsStock +
                ", unit1ActualStock=" + unit1ActualStock +
                ", unit1IsNegetive=" + unit1IsNegetive +
                ", unit2Id=" + unit2Id +
                ", unit2Name='" + unit2Name + '\'' +
                ", unit2Mrp=" + unit2Mrp +
                ", unit2PurchaseRate=" + unit2PurchaseRate +
                ", unit2FSRMH=" + unit2FSRMH +
                ", unit2FSRAI=" + unit2FSRAI +
                ", unit2CSRMH=" + unit2CSRMH +
                ", unit2CSRAI=" + unit2CSRAI +
                ", unit2Conv=" + unit2Conv +
                ", unit2ClsStock=" + unit2ClsStock +
                ", unit2ActualStock=" + unit2ActualStock +
                ", unit2IsNegetive=" + unit2IsNegetive +
                ", unit3Id=" + unit3Id +
                ", unit3Name='" + unit3Name + '\'' +
                ", unit3Mrp=" + unit3Mrp +
                ", unit3PurchaseRate=" + unit3PurchaseRate +
                ", unit3FSRMH=" + unit3FSRMH +
                ", unit3FSRAI=" + unit3FSRAI +
                ", unit3CSRMH=" + unit3CSRMH +
                ", unit3CSRAI=" + unit3CSRAI +
                ", unit3Conv=" + unit3Conv +
                ", unit3ClsStock=" + unit3ClsStock +
                ", unit3ActualStock=" + unit3ActualStock +
                ", unit3IsNegetive=" + unit3IsNegetive +
                '}';
    }
}
