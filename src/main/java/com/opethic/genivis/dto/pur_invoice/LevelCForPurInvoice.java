package com.opethic.genivis.dto.pur_invoice;

import com.opethic.genivis.models.tranx.sales.UnitOpts;

import java.util.List;

public class LevelCForPurInvoice {
    private String value;
    private String label;
    private List<UnitForPurInvoice> unitOpts;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<UnitForPurInvoice> getUnitOpts() {
        return unitOpts;
    }

    public void setUnitOpts(List<UnitForPurInvoice> unitOpts) {
        this.unitOpts = unitOpts;
    }

//    @Override
//    public String toString() {
//        return label;
//    }


    @Override
    public String toString() {
        return "LevelCForPurInvoice{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", unitOpts=" + unitOpts +
                '}';
    }
}
