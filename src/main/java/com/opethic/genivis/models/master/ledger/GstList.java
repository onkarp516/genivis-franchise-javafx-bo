package com.opethic.genivis.models.master.ledger;


import java.time.LocalDate;


public class GstList {
    private int id;
    private int gstTypeid;
    private String gstTypeName;
    private String gstRegDate;
    private String gstNo;
    private String panNo;

    public GstList(int id, int gstTypeid, String gstTypeName, String gstRegDate, String gstNo, String panNo) {
        this.id = id;
        this.gstTypeid = gstTypeid;
        this.gstTypeName = gstTypeName;
        this.gstRegDate = gstRegDate;
        this.gstNo = gstNo;
        this.panNo = panNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGstTypeid() {
        return gstTypeid;
    }

    public void setGstTypeid(int gstTypeid) {
        this.gstTypeid = gstTypeid;
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getGstRegDate() {
        return gstRegDate;
    }

    public void setGstRegDate(String gstRegDate) {
        this.gstRegDate = gstRegDate;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    @Override
    public String toString() {
        return "GstList{" +
                "id=" + id +
                ", gstTypeid=" + gstTypeid +
                ", gstTypeName='" + gstTypeName + '\'' +
                ", gstRegDate=" + gstRegDate +
                ", gstNo='" + gstNo + '\'' +
                ", panNo='" + panNo + '\'' +
                '}';
    }
}
