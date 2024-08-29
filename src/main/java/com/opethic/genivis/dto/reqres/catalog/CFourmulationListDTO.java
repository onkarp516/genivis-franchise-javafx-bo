package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CFourmulationListDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("subgroupName")
    @Expose
    private String subgroupName;

    @Override
    public String toString() {
        return "CFourmulationListDTO{" +
                "id=" + id +
                ", subgroupName='" + subgroupName + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubgroupName() {
        return subgroupName;
    }

    public void setSubgroupName(String subgroupName) {
        this.subgroupName = subgroupName;
    }
}
