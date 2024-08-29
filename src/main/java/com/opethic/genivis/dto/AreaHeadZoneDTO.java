package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AreaHeadZoneDTO {

    private Integer id;
    private Integer stateId;
    private String zoneName;

    public AreaHeadZoneDTO(Integer id, Integer stateId, String zoneName) {
        this.id = id;
        this.stateId = stateId;
        this.zoneName = zoneName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}
