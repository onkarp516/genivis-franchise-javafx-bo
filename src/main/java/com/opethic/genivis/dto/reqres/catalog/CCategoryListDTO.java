package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CCategoryListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @Override
    public String toString() {
        return "CCategoryListDTO{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
