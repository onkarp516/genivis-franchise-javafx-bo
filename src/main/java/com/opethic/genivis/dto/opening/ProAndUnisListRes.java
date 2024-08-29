package com.opethic.genivis.dto.opening;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProAndUnisListRes {


    @SerializedName("list")
    @Expose
    private List<ProAndUnisList> list;


    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;

    public List<ProAndUnisList> getList() {
        return list;
    }

    public void setList(List<ProAndUnisList> list) {
        this.list = list;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    @Override
    public String toString() {
        return "ProAndUnisListRes{" +
                "list=" + list +
                ", responseStatus=" + responseStatus +
                '}';
    }
}
