package com.opethic.genivis.dto.reqres.pur_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TranxPurRtnDataListDTO implements Serializable {
    @SerializedName("data")
    @Expose
    private List<TranxPurRtnRowDataListDTO> data;

    public List<TranxPurRtnRowDataListDTO> getData() {
        return data;
    }

    public void setData(List<TranxPurRtnRowDataListDTO> data) {
        this.data = data;
    }
}
