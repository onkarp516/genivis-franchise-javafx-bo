package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContentPackingListDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("contentPackageName")
    @Expose
    private String contentPackageName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentPackageName() {
        return contentPackageName;
    }

    public void setContentPackageName(String contentPackageName) {
        this.contentPackageName = contentPackageName;
    }
}
