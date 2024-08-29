package com.opethic.genivis.models.tranx.sales;

import java.util.List;

public class LevelAOpts {

    private String value;
    private String label;
    private List<LevelBOpts> levelBOpts;
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
    public List<LevelBOpts> getLevelBOpts() {
        return levelBOpts;
    }
    public void setLevelBOpts(List<LevelBOpts> levelBOpts) {
        this.levelBOpts = levelBOpts;
    }
    @Override
    public String toString() {
        return "LevelAOpts{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", levelBOpts=" + levelBOpts +
                '}';
    }
}
