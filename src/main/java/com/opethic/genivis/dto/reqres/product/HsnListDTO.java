package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:43:00
 * @implNote mapping Response JsonArray of Brand List  into HsnListDTO Object
 * @auther ashwins@opethic.com
 **/

public class HsnListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("hsnno")
    @Expose
    private String hsnNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(String hsnNo) {
        this.hsnNo = hsnNo;
    }

    @Override
    public String toString() {
        return "HsnListDTO{" +
                "hsnNo='" + hsnNo + '\'' +
                '}';
    }
}
