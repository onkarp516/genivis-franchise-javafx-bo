package com.opethic.genivis.dto.reqres.sales_tranx;

import com.opethic.genivis.dto.CSMultiPaymentDTO;

import java.util.ArrayList;
import java.util.List;

public class CSMultiPaymentReqDTO {

    private double cashAmt;

    private List<CSMultiPaymentDTO> multiAmt;

    private List<Integer> delDetailsIds = new ArrayList<>();

    public double getCashAmt() {
        return cashAmt;
    }

    public void setCashAmt(double cashAmt) {
        this.cashAmt = cashAmt;
    }

    public List<CSMultiPaymentDTO> getMultiAmt() {
        return multiAmt;
    }

    public void setMultiAmt(List<CSMultiPaymentDTO> multiAmt) {
        this.multiAmt = multiAmt;
    }

    public List<Integer> getDelDetailsIds() {
        return delDetailsIds;
    }

    public void setDelDetailsIds(List<Integer> delDetailsIds) {
        this.delDetailsIds = delDetailsIds;
    }

    @Override
    public String toString() {
        return "CSMultiPaymentReqDTO{" +
                "cashAmt=" + cashAmt +
                ", multiAmt=" + multiAmt +
                ", delDetailsIds=" + delDetailsIds +
                '}';
    }
}
