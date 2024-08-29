package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CSubCatListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subcategoryName")
    @Expose
    private String subcategoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }
}
