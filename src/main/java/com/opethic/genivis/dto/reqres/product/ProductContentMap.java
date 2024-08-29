package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**** uses in GET BY PRODUCT ID  ****/
public class ProductContentMap {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("content_power")
    @Expose
    private String contentPower;
    @SerializedName("content_package")
    @Expose
    private String contentPackage;
    @SerializedName("contentTypeDose")
    @Expose
    private Object contentTypeDose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentPower() {
        return contentPower;
    }

    public void setContentPower(String contentPower) {
        this.contentPower = contentPower;
    }

    public String getContentPackage() {
        return contentPackage;
    }

    public void setContentPackage(String contentPackage) {
        this.contentPackage = contentPackage;
    }

    public Object getContentTypeDose() {
        return contentTypeDose;
    }

    public void setContentTypeDose(Object contentTypeDose) {
        this.contentTypeDose = contentTypeDose;
    }

    @Override
    public String toString() {
        return "ProductContentMap{" +
                "id=" + id +
                ", contentType='" + contentType + '\'' +
                ", contentPower='" + contentPower + '\'' +
                ", contentPackage='" + contentPackage + '\'' +
                ", contentTypeDose=" + contentTypeDose +
                '}';
    }
}
