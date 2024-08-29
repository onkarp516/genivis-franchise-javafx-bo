package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:43:00
 * @implNote mapping Response Json Object into HsnResDTO Object
 * @auther ashwins@opethic.com
 **/
public class HsnResDTO {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<HsnListDTO> responseObject;

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

    public List<HsnListDTO> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<HsnListDTO> responseObject) {
        this.responseObject = responseObject;
    }
}
