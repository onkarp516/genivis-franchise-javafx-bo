package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreaHeadRegionDTO {

    private Integer id;
    private String regionName;
    private String stateName;

    public AreaHeadRegionDTO(Integer id, String regionName, String stateName) {
        this.id = id;
        this.regionName = regionName;
        this.stateName = stateName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
