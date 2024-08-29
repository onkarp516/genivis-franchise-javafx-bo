package com.opethic.genivis.dto.opening;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProAndUniRes {
    @SerializedName("product")
    @Expose
    private ProductForOpening productForOpening;

    @SerializedName("list")
    @Expose
    private List<UnitForOpeining> unitForOpeining;

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public ProductForOpening getProductForOpening() {
        return productForOpening;
    }

    public void setProductForOpening(ProductForOpening productForOpening) {
        this.productForOpening = productForOpening;
    }


    public List<UnitForOpeining> getUnitForOpeining() {
        return unitForOpeining;
    }

    public void setUnitForOpeining(List<UnitForOpeining> unitForOpeining) {
        this.unitForOpeining = unitForOpeining;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString() {
        return "ProAndUniRes{" +
                "productForOpening=" + productForOpening +
                ", unitForOpeining=" + unitForOpeining +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
