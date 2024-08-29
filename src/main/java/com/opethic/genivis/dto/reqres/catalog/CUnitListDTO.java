package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CUnitListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("unitName")
    @Expose
    private String unitName;

    @SerializedName("unitCode")
    @Expose
    private String unitCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    @Override
    public String toString() {
        return  unitName;
    }
}
