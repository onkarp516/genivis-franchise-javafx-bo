package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ResponseObject {

    @SerializedName("lst_packages")
    @Expose
    private List<LstPackage> lstPackages;

    public List<LstPackage> getLstPackages() {
        return lstPackages;
    }

    public void setLstPackages(List<LstPackage> lstPackages) {
        this.lstPackages = lstPackages;
    }

}