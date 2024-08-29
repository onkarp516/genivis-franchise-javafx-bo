package com.opethic.genivis.models.tranx.sales;

import java.util.List;

public class LevelBOpts {
    private String value;
    private String label;
    private List<LevelCOpts> levelCOpts;


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

    public List<LevelCOpts> getLevelCOpts() {
        return levelCOpts;
    }

    public void setLevelCOpts(List<LevelCOpts> levelCOpts) {
        this.levelCOpts = levelCOpts;
    }

    @Override
    public String toString() {
        return "LevelBOpts{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", levelCOpts=" + levelCOpts +
                '}';
    }
}
