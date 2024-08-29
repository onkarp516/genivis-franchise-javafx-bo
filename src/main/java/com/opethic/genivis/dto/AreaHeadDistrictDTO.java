package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreaHeadDistrictDTO {

    private Integer id;
    private String districtName;
    private String stateName;

    public AreaHeadDistrictDTO(Integer id, String districtName, String stateName) {
        this.id = id;
        this.districtName = districtName;
        this.stateName = stateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
