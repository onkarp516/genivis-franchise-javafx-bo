package com.opethic.genivis.models.tranx.sales;

import java.util.List;

public class TranxRtnCal {
    private List<TranxRow> row;
    private List<TranxTaxCal> totalIgstCal;
    private List<TranxTaxCal> totalCgstCal;
    private List<TranxTaxCal> totalSgstCal;
    private Double totalBaseAmt;
    private Double totalDisAmt;
    private Double totalAmt;
    private Double taxTotalAmt;
    private Double billAmt;
    private Integer totalQty;
    private Double totalFreeQty;

    private Double actDiscAmt;
    private Double actDiscPer;

    public TranxRtnCal(List<TranxRow> row, List<TranxTaxCal> totalIgstCal, List<TranxTaxCal> totalCgstCal, List<TranxTaxCal> totalSgstCal, Double totalBaseAmt, Double totalDisAmt, Double totalAmt, Double taxTotalAmt, Double billAmt, Integer totalQty, Double totalFreeQty, Double DiscAmt, Double DiscPer) {
        this.row = row;
        this.totalIgstCal = totalIgstCal;
        this.totalCgstCal = totalCgstCal;
        this.totalSgstCal = totalSgstCal;
        this.totalBaseAmt = totalBaseAmt;
        this.totalDisAmt = totalDisAmt;
        this.totalAmt = totalAmt;
        this.taxTotalAmt = taxTotalAmt;
        this.billAmt = billAmt;
        this.totalQty = totalQty;
        this.totalFreeQty = totalFreeQty;
        this.actDiscAmt = DiscAmt;
        this.actDiscPer = DiscPer;
    }

    public List<TranxRow> getRow() {
        return row;
    }

    public void setRow(List<TranxRow> row) {
        this.row = row;
    }

    public List<TranxTaxCal> getTotalIgstCal() {
        return totalIgstCal;
    }

    public void setTotalIgstCal(List<TranxTaxCal> totalIgstCal) {
        this.totalIgstCal = totalIgstCal;
    }

    public List<TranxTaxCal> getTotalCgstCal() {
        return totalCgstCal;
    }

    public void setTotalCgstCal(List<TranxTaxCal> totalCgstCal) {
        this.totalCgstCal = totalCgstCal;
    }

    public List<TranxTaxCal> getTotalSgstCal() {
        return totalSgstCal;
    }

    public void setTotalSgstCal(List<TranxTaxCal> totalSgstCal) {
        this.totalSgstCal = totalSgstCal;
    }

    public Double getTotalBaseAmt() {
        return totalBaseAmt;
    }

    public void setTotalBaseAmt(Double totalBaseAmt) {
        this.totalBaseAmt = totalBaseAmt;
    }

    public Double getTotalDisAmt() {
        return totalDisAmt;
    }

    public void setTotalDisAmt(Double totalDisAmt) {
        this.totalDisAmt = totalDisAmt;
    }

    public Double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Double totalAmt) {
        this.totalAmt = totalAmt;
    }

    public Double getTaxTotalAmt() {
        return taxTotalAmt;
    }

    public void setTaxTotalAmt(Double taxTotalAmt) {
        this.taxTotalAmt = taxTotalAmt;
    }

    public Double getBillAmt() {
        return billAmt;
    }

    public void setBillAmt(Double billAmt) {
        this.billAmt = billAmt;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Double getTotalFreeQty() {
        return totalFreeQty;
    }

    public void setTotalFreeQty(Double totalFreeQty) {
        this.totalFreeQty = totalFreeQty;
    }

    public Double getActDiscAmt() {
        return actDiscAmt;
    }

    public void setActDiscAmt(Double actDiscAmt) {
        this.actDiscAmt = actDiscAmt;
    }

    public Double getActDiscPer() {
        return actDiscPer;
    }

    public void setActDiscPer(Double actDiscPer) {
        this.actDiscPer = actDiscPer;
    }
}
