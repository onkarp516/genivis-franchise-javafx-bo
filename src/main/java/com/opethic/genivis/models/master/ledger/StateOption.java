package com.opethic.genivis.models.master.ledger;

public class StateOption {
    private int id;
    private String stateName;
    private int stateCode;

    public StateOption(int id, String stateName, int stateCode) {
        this.id = id;
        this.stateName = stateName;
        this.stateCode = stateCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    @Override
    public String toString() {
        return "StateOption{" +
                "id=" + id +
                ", stateName='" + stateName + '\'' +
                ", stateCode=" + stateCode +
                '}';
//        return stateName;
    }
}
