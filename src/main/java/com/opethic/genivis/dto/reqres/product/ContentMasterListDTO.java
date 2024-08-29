package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ContentMasterListDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("contentName")
    @Expose
    private String contentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public ContentMasterListDTO(Integer id, String contentName) {
        this.id = id;
        this.contentName = contentName;
    }

    public ContentMasterListDTO() {
    }
}
