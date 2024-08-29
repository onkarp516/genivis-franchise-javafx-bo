package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:43:00
 * @implNote mapping Response JsonArray of Brand List  into BrandListDTO Object
 * @auther ashwins@opethic.com
 **/

public class BrandListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brandName")
    @Expose
    private String brandName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
