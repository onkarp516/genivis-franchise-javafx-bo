package com.opethic.genivis.dto.pur_invoice;

import com.opethic.genivis.models.tranx.sales.LevelBOpts;

import java.util.List;

public class LevelAForPurInvoice {

    private String value;
    private String label;
    private List<LevelBForPurInvoice> levelBOpts;
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
    public List<LevelBForPurInvoice> getLevelBOpts() {
        return levelBOpts;
    }
    public void setLevelBOpts(List<LevelBForPurInvoice> levelBOpts) {
        this.levelBOpts = levelBOpts;
    }
    @Override
    public String toString() {
        return label;
    }


//    @Override
//    public String toString() {
//        return "LevelAForPurInvoice{" +
//                "value='" + value + '\'' +
//                ", label='" + label + '\'' +
//                ", levelBOpts=" + levelBOpts +
//                '}';
//    }
}
