package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:17:18:00
 * @implNote mapping Response Json Object into TaxDTO Object **
 * @auther ashwins@opethic.com
 **/
public class TaxDTO {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<TaxListDTO> responseObject;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<TaxListDTO> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<TaxListDTO> responseObject) {
        this.responseObject = responseObject;
    }
}
