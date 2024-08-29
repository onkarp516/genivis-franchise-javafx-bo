package com.opethic.genivis.dto.opening;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitForOpeining {
    @SerializedName("unit_id")
    @Expose
    private Integer unitId;

    @SerializedName("unit_name")
    @Expose
    private String unitName;

    public Integer getId() {
        return unitId;
    }

    public void setId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return unitName;
    }
}
