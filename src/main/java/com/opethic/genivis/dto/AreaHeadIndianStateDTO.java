package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class AreaHeadIndianStateDTO {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("stateName")
    @Expose
    private String stateName;
    @SerializedName("stateCode")
    @Expose
    private String stateCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }


}
