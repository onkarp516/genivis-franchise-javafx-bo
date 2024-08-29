package com.opethic.genivis.dto.reqres.pur_tranx;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LstPackage {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("levelBOpts")
    @Expose
    private List<LevelBOpt> levelBOpts;

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

    public List<LevelBOpt> getLevelBOpts() {
        return levelBOpts;
    }

    public void setLevelBOpts(List<LevelBOpt> levelBOpts) {
        this.levelBOpts = levelBOpts;
    }

}
