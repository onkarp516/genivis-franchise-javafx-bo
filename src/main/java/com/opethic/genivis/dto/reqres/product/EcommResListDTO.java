package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EcommResListDTO    {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("ecommerceType")
    @Expose
    private String ecommerceType;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("outletId")
    @Expose
    private Integer outletId;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @Override
    public String toString() {
        return ecommerceType;
    }

    public EcommResListDTO(Integer id, String ecommerceType, Boolean status, Integer outletId, Integer createdBy, String createdAt) {
        this.id = id;
        this.ecommerceType = ecommerceType;
        this.status = status;
        this.outletId = outletId;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEcommerceType() {
        return ecommerceType;
    }

    public void setEcommerceType(String ecommerceType) {
        this.ecommerceType = ecommerceType;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getOutletId() {
        return outletId;
    }

    public void setOutletId(Integer outletId) {
        this.outletId = outletId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
