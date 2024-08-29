package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:43:00
 * @implNote mapping Response JsonArray of Brand List  into HsnListDTO Object
 * @auther ashwins@opethic.com
 **/


public class DrugTypeListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("drugName")
    @Expose
    private String drugName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
}
