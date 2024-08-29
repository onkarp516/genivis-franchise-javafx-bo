package com.opethic.genivis.dto.pur_invoice;

import com.opethic.genivis.models.tranx.sales.LevelCOpts;

import java.util.List;

public class LevelBForPurInvoice {
    private String value;
    private String label;
    private List<LevelCForPurInvoice> levelCOpts;


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

    public List<LevelCForPurInvoice> getLevelCOpts() {
        return levelCOpts;
    }

    public void setLevelCOpts(List<LevelCForPurInvoice> levelCOpts) {
        this.levelCOpts = levelCOpts;
    }

//    @Override
//    public String toString() {
//        return label;
//    }


    @Override
    public String toString() {
        return "LevelBForPurInvoice{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", levelCOpts=" + levelCOpts +
                '}';
    }
}
