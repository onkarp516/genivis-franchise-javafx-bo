package com.opethic.genivis.models.tranx.sales;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TranxRowNw {
    private String detailId;
    private String productId;
    private String productName;
    private String productPackage;
    private TranxSelectedProduct selectedProduct;
    private TranxSelectedBatch selectedBatch;
    private int levelAId;
    private String dispLevelA;
    private LevelAOpts selectedLevelA;
    private int levelBId;
    private String dispLevelB;
    private LevelBOpts selectedLevelB;
    private int levelCId;
    private String dispLevelC;
    private LevelCOpts selectedLevelC;
    private int unitId;
    private String dispUnit;
    private UnitOpts selectedUnit;
    private String qty;
    private String freeqty;
    private double unitConv;
    private String rate;
    private double baseAmt;
    private String disAmt;
    private String disPer;
    private String disPer2;
    private double rowDisAmt;
    private Double grossAmt;
    private double addChargesAmt;
    private double grossAmt1;
    private double invoiceDisAmt;
    private double disPerCal;
    private double disAmtCal;
    private double totalAmt;
    private double igst;
    private double cgst;
    private double sgst;

    private double totalIgst;
    private double totalCgst;
    private double totalSgst;
    private double finalAmt;
    private boolean isBatch;
    private int bDetailId;
    private String bNo;
    private double bRate;
    private double bPurRate;
    private LocalDate bExpiry;
    private double saleRate;
    private double rateA;
    private double rateB;
    private double rateC;
    private double minMargin;
    private double marginPer;
    private LocalDate manufacturingDate;
    private String referenceType;
    private int referenceId;
    private List<String> serialNo;
    private boolean isSerial;

    public TranxRowNw(String detailId, String productId, String productName, String productPackage, TranxSelectedProduct selectedProduct, TranxSelectedBatch selectedBatch, int levelAId, String dispLevelA, LevelAOpts selectedLevelA, int levelBId, String dispLevelB, LevelBOpts selectedLevelB, int levelCId, String dispLevelC, LevelCOpts selectedLevelC, int unitId, String dispUnit, UnitOpts selectedUnit, String qty, String freeqty, double unitConv, String rate, double baseAmt, String disAmt, String disPer, String disPer2, double rowDisAmt, Double grossAmt, double addChargesAmt, double grossAmt1, double invoiceDisAmt, double disPerCal, double disAmtCal, double totalAmt, double igst, double cgst, double sgst, double totalIgst, double totalCgst, double totalSgst, double finalAmt, boolean isBatch, int bDetailId, String bNo, double bRate, double bPurRate, LocalDate bExpiry, double saleRate, double rateA, double rateB, double rateC, double minMargin, double marginPer, LocalDate manufacturingDate, String referenceType, int referenceId, List<String> serialNo, boolean isSerial, UnitLevelLst unitLevelLst) {
        this.detailId = detailId;
        this.productId = productId;
        this.productName = productName;
        this.productPackage = productPackage;
        this.selectedProduct = selectedProduct;
        this.selectedBatch = selectedBatch;
        this.levelAId = levelAId;
        this.dispLevelA = dispLevelA;
        this.selectedLevelA = selectedLevelA;
        this.levelBId = levelBId;
        this.dispLevelB = dispLevelB;
        this.selectedLevelB = selectedLevelB;
        this.levelCId = levelCId;
        this.dispLevelC = dispLevelC;
        this.selectedLevelC = selectedLevelC;
        this.unitId = unitId;
        this.dispUnit = dispUnit;
        this.selectedUnit = selectedUnit;
        this.qty = qty;
        this.freeqty = freeqty;
        this.unitConv = unitConv;
        this.rate = rate;
        this.baseAmt = baseAmt;
        this.disAmt = disAmt;
        this.disPer = disPer;
        this.disPer2 = disPer2;
        this.rowDisAmt = rowDisAmt;
        this.grossAmt = grossAmt;
        this.addChargesAmt = addChargesAmt;
        this.grossAmt1 = grossAmt1;
        this.invoiceDisAmt = invoiceDisAmt;
        this.disPerCal = disPerCal;
        this.disAmtCal = disAmtCal;
        this.totalAmt = totalAmt;
        this.igst = igst;
        this.cgst = cgst;
        this.sgst = sgst;
        this.totalIgst = totalIgst;
        this.totalCgst = totalCgst;
        this.totalSgst = totalSgst;
        this.finalAmt = finalAmt;
        this.isBatch = isBatch;
        this.bDetailId = bDetailId;
        this.bNo = bNo;
        this.bRate = bRate;
        this.bPurRate = bPurRate;
        this.bExpiry = bExpiry;
        this.saleRate = saleRate;
        this.rateA = rateA;
        this.rateB = rateB;
        this.rateC = rateC;
        this.minMargin = minMargin;
        this.marginPer = marginPer;
        this.manufacturingDate = manufacturingDate;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.serialNo = serialNo;
        this.isSerial = isSerial;
        this.unitLevelLst = unitLevelLst;
    }

    private UnitLevelLst unitLevelLst;

    public UnitLevelLst getUnitLevelLst() {
        return unitLevelLst;
    }

    public void setUnitLevelLst(UnitLevelLst unitLevelLst) {
        this.unitLevelLst = unitLevelLst;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerial(boolean serial) {
        isSerial = serial;
    }


    public int getLevelAId() {
        return levelAId;
    }

    public void setLevelAId(int levelAId) {
        this.levelAId = levelAId;
    }

    public int getLevelBId() {
        return levelBId;
    }

    public void setLevelBId(int levelBId) {
        this.levelBId = levelBId;
    }

    public int getLevelCId() {
        return levelCId;
    }

    public void setLevelCId(int levelCId) {
        this.levelCId = levelCId;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public double getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(double unitConv) {
        this.unitConv = unitConv;
    }


    public double getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(double baseAmt) {
        this.baseAmt = baseAmt;
    }


    public double getRowDisAmt() {
        return rowDisAmt;
    }

    public void setRowDisAmt(double rowDisAmt) {
        this.rowDisAmt = rowDisAmt;
    }

    public Double getGrossAmt() {
        return grossAmt;
    }

    public void setGrossAmt(Double grossAmt) {
        this.grossAmt = grossAmt;
    }

    public double getAddChargesAmt() {
        return addChargesAmt;
    }

    public void setAddChargesAmt(double addChargesAmt) {
        this.addChargesAmt = addChargesAmt;
    }

    public double getGrossAmt1() {
        return grossAmt1;
    }

    public void setGrossAmt1(double grossAmt1) {
        this.grossAmt1 = grossAmt1;
    }

    public double getInvoiceDisAmt() {
        return invoiceDisAmt;
    }

    public void setInvoiceDisAmt(double invoiceDisAmt) {
        this.invoiceDisAmt = invoiceDisAmt;
    }

    public double getDisPerCal() {
        return disPerCal;
    }

    public void setDisPerCal(double disPerCal) {
        this.disPerCal = disPerCal;
    }

    public double getDisAmtCal() {
        return disAmtCal;
    }

    public void setDisAmtCal(double disAmtCal) {
        this.disAmtCal = disAmtCal;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public double getIgst() {
        return igst;
    }

    public void setIgst(double igst) {
        this.igst = igst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getTotalIgst() {
        return totalIgst;
    }

    public void setTotalIgst(double totalIgst) {
        this.totalIgst = totalIgst;
    }

    public double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public double getFinalAmt() {
        return finalAmt;
    }

    public void setFinalAmt(double finalAmt) {
        this.finalAmt = finalAmt;
    }

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }

    public int getbDetailId() {
        return bDetailId;
    }

    public void setbDetailId(int bDetailId) {
        this.bDetailId = bDetailId;
    }

    public String getbNo() {
        return bNo;
    }

    public void setbNo(String bNo) {
        this.bNo = bNo;
    }

    public double getbRate() {
        return bRate;
    }

    public void setbRate(double bRate) {
        this.bRate = bRate;
    }

    public double getbPurRate() {
        return bPurRate;
    }

    public void setbPurRate(double bPurRate) {
        this.bPurRate = bPurRate;
    }

    public LocalDate getbExpiry() {
        return bExpiry;
    }

    public void setbExpiry(LocalDate bExpiry) {
        this.bExpiry = bExpiry;
    }

    public double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(double saleRate) {
        this.saleRate = saleRate;
    }

    public double getRateA() {
        return rateA;
    }

    public void setRateA(double rateA) {
        this.rateA = rateA;
    }

    public double getRateB() {
        return rateB;
    }

    public void setRateB(double rateB) {
        this.rateB = rateB;
    }

    public double getRateC() {
        return rateC;
    }

    public void setRateC(double rateC) {
        this.rateC = rateC;
    }

    public double getMinMargin() {
        return minMargin;
    }

    public void setMinMargin(double minMargin) {
        this.minMargin = minMargin;
    }

    public double getMarginPer() {
        return marginPer;
    }

    public void setMarginPer(double marginPer) {
        this.marginPer = marginPer;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(int referenceId) {
        this.referenceId = referenceId;
    }

    public List<String> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(List<String> serialNo) {
        this.serialNo = serialNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDispLevelA() {
        return dispLevelA;
    }

    public void setDispLevelA(String dispLevelA) {
        this.dispLevelA = dispLevelA;
    }

    public String getDispLevelB() {
        return dispLevelB;
    }

    public void setDispLevelB(String dispLevelB) {
        this.dispLevelB = dispLevelB;
    }

    public String getDispLevelC() {
        return dispLevelC;
    }

    public void setDispLevelC(String dispLevelC) {
        this.dispLevelC = dispLevelC;
    }

    public String getDispUnit() {
        return dispUnit;
    }

    public void setDispUnit(String dispUnit) {
        this.dispUnit = dispUnit;
    }

    public String getProductPackage() {
        return productPackage;
    }

    public void setProductPackage(String productPackage) {
        this.productPackage = productPackage;
    }


    public TranxSelectedProduct getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(TranxSelectedProduct selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public TranxSelectedBatch getSelectedBatch() {
        return selectedBatch;
    }

    public void setSelectedBatch(TranxSelectedBatch selectedBatch) {
        this.selectedBatch = selectedBatch;
    }


    public List<LevelAOpts> getLevelAOpts() {
        return unitLevelLst.getLevelAOpts();
    }


    public LevelAOpts getSelectedLevelA() {
        return selectedLevelA;
    }

    public void setSelectedLevelA(LevelAOpts selectedLevelA) {
        this.selectedLevelA = selectedLevelA;
    }

    public LevelBOpts getSelectedLevelB() {
        return selectedLevelB;
    }

    public void setSelectedLevelB(LevelBOpts selectedLevelB) {
        this.selectedLevelB = selectedLevelB;
    }

    public LevelCOpts getSelectedLevelC() {
        return selectedLevelC;
    }

    public void setSelectedLevelC(LevelCOpts selectedLevelC) {
        this.selectedLevelC = selectedLevelC;
    }

    public UnitOpts getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(UnitOpts selectedUnit) {
        this.selectedUnit = selectedUnit;
    }


    public List<String> getAllUnits() {
        List<String> disUnitOpts = new ArrayList<>();
        if (selectedLevelC != null) {
            List<UnitOpts> unitOpts = selectedLevelC.getUnitOpts();
            List<Integer> unitIdActStock = new ArrayList<>();
            if (selectedBatch != null && !selectedBatch.getBatchNo().isEmpty()) {
                for (UnitOpts unitOpt : unitOpts) {
                    if (unitOpt.getUnitId() == selectedBatch.getUnit1Id() && selectedBatch.getUnit1ActualStock() > 0) {
                        unitIdActStock.add(unitOpt.getUnitId());
                    }
                    if (unitOpt.getUnitId() == selectedBatch.getUnit2Id() && selectedBatch.getUnit2ActualStock() > 0) {
                        unitIdActStock.add(unitOpt.getUnitId());
                    }
                    if (unitOpt.getUnitId() == selectedBatch.getUnit3Id() && selectedBatch.getUnit3ActualStock() > 0) {
                        unitIdActStock.add(unitOpt.getUnitId());
                    }
                }
            } else {
                unitIdActStock.addAll(unitOpts.stream().map(v -> v.getUnitId()).collect(Collectors.toList()));
            }
            disUnitOpts = unitOpts.stream().filter(v -> unitIdActStock.contains(v.getUnitId())).map(s -> s.getUnitName()).collect(Collectors.toList());
        }
        return disUnitOpts.size() > 0 ? disUnitOpts : Arrays.asList();
    }

    public List<String> getAllLevelC() {
        List<LevelCOpts> levelCOpts = selectedLevelB.getLevelCOpts();
        List<String> disUnitOpts = levelCOpts.stream().map(s -> s.getLabel()).collect(Collectors.toList());
        return disUnitOpts.size() > 0 ? disUnitOpts : Arrays.asList();
    }

    public List<String> getAllLevelB() {
        List<LevelBOpts> levelBOpts = selectedLevelA.getLevelBOpts();
        List<String> disUnitOpts = levelBOpts.stream().map(s -> s.getLabel()).collect(Collectors.toList());
        return disUnitOpts.size() > 0 ? disUnitOpts : Arrays.asList();
    }

    public List<String> getAllLevelA() {
        List<LevelAOpts> levelAOpts = unitLevelLst.getLevelAOpts();
        List<String> disUnitOpts = levelAOpts.stream().map(s -> s.getLabel()).collect(Collectors.toList());
        return disUnitOpts.size() > 0 ? disUnitOpts : Arrays.asList();
    }

    public List<LevelAOpts> getAllLevelAOpt() {
        return unitLevelLst.getLevelAOpts();
    }

    public List<LevelBOpts> getAllLevelBOpt() {
        return selectedLevelA.getLevelBOpts();
    }

    public List<LevelCOpts> getAllLevelCOpt() {
        return selectedLevelB.getLevelCOpts();
    }

    public List<UnitOpts> getAllUnitOpt() {
        return selectedLevelC.getUnitOpts();
    }

    public double getUnitWiseRateMH() {
        double rtnrate = 0.0;
        if (selectedBatch != null && !selectedBatch.getBatchNo().isEmpty()) {
            if (selectedUnit.getUnitId() == selectedBatch.getUnit1Id() && selectedBatch.getUnit1ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit1FSRMH();
            }
            if (selectedUnit.getUnitId() == selectedBatch.getUnit2Id() && selectedBatch.getUnit2ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit2FSRMH();
            }
            if (selectedUnit.getUnitId() == selectedBatch.getUnit3Id() && selectedBatch.getUnit3ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit3FSRMH();
            }
        } else {
            if (selectedUnit.getUnitId() == selectedProduct.getUnit1Id() && selectedProduct.getUnit1ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit1FSRMH();
            }
            if (selectedUnit.getUnitId() == selectedProduct.getUnit2Id() && selectedProduct.getUnit2ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit2FSRMH();
            }
            if (selectedUnit.getUnitId() == selectedProduct.getUnit3Id() && selectedProduct.getUnit3ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit3FSRMH();
            }
        }


        return rtnrate;
    }

    public double getUnitWiseRateAI() {
        double rtnrate = 0.0;
        if (selectedBatch != null && !selectedBatch.getBatchNo().isEmpty()) {
            if (selectedUnit.getUnitId() == selectedBatch.getUnit1Id() && selectedBatch.getUnit1ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit1FSRAI();
            }
            if (selectedUnit.getUnitId() == selectedBatch.getUnit2Id() && selectedBatch.getUnit2ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit2FSRAI();
            }
            if (selectedUnit.getUnitId() == selectedBatch.getUnit3Id() && selectedBatch.getUnit3ActualStock() > 0) {
                rtnrate = selectedBatch.getUnit3FSRAI();
            }
        } else {
            if (selectedUnit.getUnitId() == selectedProduct.getUnit1Id() && selectedProduct.getUnit1ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit1FSRAI();
            }
            if (selectedUnit.getUnitId() == selectedProduct.getUnit2Id() && selectedProduct.getUnit2ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit2FSRAI();
            }
            if (selectedUnit.getUnitId() == selectedProduct.getUnit3Id() && selectedProduct.getUnit3ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit3FSRAI();
            }
        }
        
        return rtnrate;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getFreeqty() {
        return freeqty;
    }

    public void setFreeqty(String freeqty) {
        this.freeqty = freeqty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(String disAmt) {
        this.disAmt = disAmt;
    }

    public String getDisPer() {
        return disPer;
    }

    public void setDisPer(String disPer) {
        this.disPer = disPer;
    }

    public String getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(String disPer2) {
        this.disPer2 = disPer2;
    }

    @Override
    public String toString() {
        return "TranxRowNw{" +
                "detailId='" + detailId + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", productPackage='" + productPackage + '\'' +
                ", selectedProduct=" + selectedProduct +
                ", selectedBatch=" + selectedBatch +
                ", levelAId=" + levelAId +
                ", dispLevelA='" + dispLevelA + '\'' +
                ", selectedLevelA=" + selectedLevelA +
                ", levelBId=" + levelBId +
                ", dispLevelB='" + dispLevelB + '\'' +
                ", selectedLevelB=" + selectedLevelB +
                ", levelCId=" + levelCId +
                ", dispLevelC='" + dispLevelC + '\'' +
                ", selectedLevelC=" + selectedLevelC +
                ", unitId=" + unitId +
                ", dispUnit='" + dispUnit + '\'' +
                ", selectedUnit=" + selectedUnit +
                ", qty='" + qty + '\'' +
                ", freeqty='" + freeqty + '\'' +
                ", unitConv=" + unitConv +
                ", rate='" + rate + '\'' +
                ", baseAmt=" + baseAmt +
                ", disAmt='" + disAmt + '\'' +
                ", disPer='" + disPer + '\'' +
                ", disPer2='" + disPer2 + '\'' +
                ", rowDisAmt=" + rowDisAmt +
                ", grossAmt=" + grossAmt +
                ", addChargesAmt=" + addChargesAmt +
                ", grossAmt1=" + grossAmt1 +
                ", invoiceDisAmt=" + invoiceDisAmt +
                ", disPerCal=" + disPerCal +
                ", disAmtCal=" + disAmtCal +
                ", totalAmt=" + totalAmt +
                ", igst=" + igst +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", totalIgst=" + totalIgst +
                ", totalCgst=" + totalCgst +
                ", totalSgst=" + totalSgst +
                ", finalAmt=" + finalAmt +
                ", isBatch=" + isBatch +
                ", bDetailId=" + bDetailId +
                ", bNo='" + bNo + '\'' +
                ", bRate=" + bRate +
                ", bPurRate=" + bPurRate +
                ", bExpiry=" + bExpiry +
                ", saleRate=" + saleRate +
                ", rateA=" + rateA +
                ", rateB=" + rateB +
                ", rateC=" + rateC +
                ", minMargin=" + minMargin +
                ", marginPer=" + marginPer +
                ", manufacturingDate=" + manufacturingDate +
                ", referenceType='" + referenceType + '\'' +
                ", referenceId=" + referenceId +
                ", serialNo=" + serialNo +
                ", isSerial=" + isSerial +
                ", unitLevelLst=" + unitLevelLst +
                '}';
    }
}
