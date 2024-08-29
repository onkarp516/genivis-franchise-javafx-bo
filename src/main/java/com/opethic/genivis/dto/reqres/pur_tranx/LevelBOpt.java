package com.opethic.genivis.dto.reqres.pur_tranx;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LevelBOpt {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("levelCOpts")
    @Expose
    private List<LevelCOpt> levelCOpts;

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

    public List<LevelCOpt> getLevelCOpts() {
        return levelCOpts;
    }

    public void setLevelCOpts(List<LevelCOpt> levelCOpts) {
        this.levelCOpts = levelCOpts;
    }

}
