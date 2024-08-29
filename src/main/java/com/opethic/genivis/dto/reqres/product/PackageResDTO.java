package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:16:34:00
 * @implNote mapping Response Json Object into PackageResDTO Object
 * @auther ashwins@opethic.com
 **/
public class PackageResDTO {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("list")
    @Expose
    private List<PackageListData> list;

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

    public List<PackageListData> getList() {
        return list;
    }

    public void setList(List<PackageListData> list) {
        this.list = list;
    }


}

