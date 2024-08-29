package com.opethic.genivis.controller.dialogs;

public class ProductContetsList {
    private int id;
    private String contentName;
    private String power;
    private String packing;
    private String contentType;


    public ProductContetsList(int id, String contentName, String power, String packing, String contentType) {
        this.id = id;
        this.contentName = contentName;
        this.power = power;
        this.packing = packing;
        this.contentType = contentType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
