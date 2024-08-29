package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ContentTypeListDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("contentNameDose")
    @Expose
    private String contentNameDose;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentNameDose() {
        return contentNameDose;
    }

    public void setContentNameDose(String contentNameDose) {
        this.contentNameDose = contentNameDose;
    }
}
