package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.reqres.pur_tranx.ResponseObject;

/**** uses in GET BY PRODUCT ID  ****/
public class ProductMasterDTO {
    @SerializedName("messege")
    @Expose
    private String messege;
    @SerializedName("responseStatus")
    @Expose
    private Long responseStatus;
    @SerializedName("responseObject")
    @Expose
    private ProductResponseObject responseObject;

    public ProductMasterDTO() {

    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public Long getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Long responseStatus) {
        this.responseStatus = responseStatus;
    }

    public ProductResponseObject getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(ProductResponseObject responseObject) {
        this.responseObject = responseObject;
    }
}
