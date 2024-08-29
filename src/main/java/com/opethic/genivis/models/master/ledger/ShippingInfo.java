package com.opethic.genivis.models.master.ledger;


public class ShippingInfo {
    private int id;
    private String shippingName;
    private int shippingStateId;
    private String shippingStateName;
    private int shippingStateCode;

    public ShippingInfo(int id, String shippingName, int shippingStateId, String shippingStateName,int shippingStateCode) {
        this.id = id;
        this.shippingName = shippingName;
        this.shippingStateId = shippingStateId;
        this.shippingStateName = shippingStateName;
        this.shippingStateCode = shippingStateCode;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public int getShippingStateId() {
        return shippingStateId;
    }

    public void setShippingStateId(int shippingStateId) {
        this.shippingStateId = shippingStateId;
    }

    public String getShippingStateName() {
        return shippingStateName;
    }

    public void setShippingStateName(String shippingStateName) {
        this.shippingStateName = shippingStateName;
    }

    public int getShippingStateCode() {
        return shippingStateCode;
    }

    public void setShippingStateCode(int shippingStateCode) {
        this.shippingStateCode = shippingStateCode;
    }

    @Override
    public String toString() {
        return "ShippingInfo{" +
                "id=" + id +
                ", shippingName='" + shippingName + '\'' +
                ", shippingStateId=" + shippingStateId +
                ", shippingStateName='" + shippingStateName + '\'' +
                ", shippingStateCode=" + shippingStateCode +
                '}';
    }
}
