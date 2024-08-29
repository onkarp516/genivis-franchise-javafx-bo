package com.opethic.genivis.models.tranx.sales;

import java.util.List;

public class LevelCOpts {
    private String value;
    private String label;
    private List<UnitOpts> unitOpts;

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

    public List<UnitOpts> getUnitOpts() {
        return unitOpts;
    }

    public void setUnitOpts(List<UnitOpts> unitOpts) {
        this.unitOpts = unitOpts;
    }

    @Override
    public String toString() {
        return "LevelCOpts{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", unitOpts=" + unitOpts +
                '}';
    }
}

