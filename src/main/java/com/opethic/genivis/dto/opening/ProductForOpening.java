package com.opethic.genivis.dto.opening;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductForOpening {
    @SerializedName("product_id")
    @Expose
    private Integer product_id;

    @SerializedName("product_name")
    @Expose
    private String product_name;

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    @Override
    public String toString() {
        return "ProductForOpening{" +
                "product_id=" + product_id +
                ", product_name='" + product_name + '\'' +
                '}';
    }
}
