package com.opethic.genivis.models.tranx.sales;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TranxRow {
    private int detailId;
    private int productId;
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
    private int qty;
    private int freeqty;
    private double unitConv;
    private double rate;
    private double baseAmt;
    private double disAmt;
    private double disPer;
    private double disPer2;
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

    public TranxRow(int detailId, int productId, int levelAId, int levelBId, int levelCId, int unitId, int qty, int freeqty, double unitConv, double rate, double baseAmt, double disAmt, double disPer, double disPer2, double rowDisAmt, double grossAmt, double addChargesAmt, double grossAmt1, double invoiceDisAmt, double disPerCal, double disAmtCal, double totalAmt, double igst, double cgst, double sgst, double totalIgst, double totalCgst, double totalSgst, double finalAmt, boolean isBatch, int bDetailId, String bNo, double bRate, double bPurRate, LocalDate bExpiry, double saleRate, double rateA, double rateB, double rateC, double minMargin, double marginPer, LocalDate manufacturingDate, String referenceType, int referenceId, List<String> serialNo) {
        this.detailId = detailId;
        this.productId = productId;
        this.levelAId = levelAId;
        this.levelBId = levelBId;
        this.levelCId = levelCId;
        this.unitId = unitId;
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
        this.productName = "";
        this.dispLevelA = "";
        this.dispLevelB = "";
        this.dispLevelC = "";
        this.dispUnit = "";
        this.productPackage = "";
        this.unitLevelLst = null;
        this.isSerial = false;
        this.selectedProduct = null;
        this.selectedBatch = null;
        this.selectedLevelA = null;
        this.selectedLevelB = null;
        this.selectedLevelC = null;
        this.selectedUnit = null;
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


    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getFreeqty() {
        return freeqty;
    }

    public void setFreeqty(int freeqty) {
        this.freeqty = freeqty;
    }

    public double getUnitConv() {
        return unitConv;
    }

    public void setUnitConv(double unitConv) {
        this.unitConv = unitConv;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getBaseAmt() {
        return baseAmt;
    }

    public void setBaseAmt(double baseAmt) {
        this.baseAmt = baseAmt;
    }

    public double getDisAmt() {
        return disAmt;
    }

    public void setDisAmt(double disAmt) {
        this.disAmt = disAmt;
    }

    public double getDisPer() {
        return disPer;
    }

    public void setDisPer(double disPer) {
        this.disPer = disPer;
    }

    public double getDisPer2() {
        return disPer2;
    }

    public void setDisPer2(double disPer2) {
        this.disPer2 = disPer2;
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

    @Override
    public String toString() {
        return "TranxRow{" +
                "detailId=" + detailId +
                ", productId=" + productId +
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
                ", qty=" + qty +
                ", freeqty=" + freeqty +
                ", unitConv=" + unitConv +
                ", rate=" + rate +
                ", baseAmt=" + baseAmt +
                ", disAmt=" + disAmt +
                ", disPer=" + disPer +
                ", disPer2=" + disPer2 +
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
}
