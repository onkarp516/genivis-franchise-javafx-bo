package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class LevelCOpt {

    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("unitOpts")
    @Expose
    private List<UnitListDTO> unitOpts;

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

    public List<UnitListDTO> getUnitOpts() {
        return unitOpts;
    }

    public void setUnitOpts(List<UnitListDTO> unitOpts) {
        this.unitOpts = unitOpts;
    }

}
