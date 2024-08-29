package com.opethic.genivis.models.tranx.sales;

public class TranxGst {
    private Double digst;
    private Double igst;
    private Double dgst;
    private Double cgst;
    private Double sgst;

    public TranxGst(Double digst,Double igst, Double dgst, Double cgst, Double sgst) {
        this.digst = digst;
        this.igst = igst;
        this.dgst = dgst;
        this.cgst = cgst;
        this.sgst = sgst;
    }

    public Double getDigst() {
        return digst;
    }

    public void setDigst(Double digst) {
        this.digst = digst;
    }

    public Double getDgst() {
        return dgst;
    }

    public void setDgst(Double dgst) {
        this.dgst = dgst;
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

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }
}
