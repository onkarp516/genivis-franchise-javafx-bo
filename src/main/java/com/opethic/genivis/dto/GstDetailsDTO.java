package com.opethic.genivis.dto;

public class GstDetailsDTO {


    private String id;
    private String gstNo;
    private String state;

    public GstDetailsDTO(String id, String gstNo, String state) {
        this.id = id;
        this.gstNo = gstNo;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "GstDetailsDTO{" +
                "id='" + id + '\'' +
                ", gstNo='" + gstNo + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
