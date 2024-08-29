package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:34:00
 * @implNote mapping Response JsonArray of Package List  into PackageListData Object
 * @auther ashwins@opethic.com
 **/

public class PackageListData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}