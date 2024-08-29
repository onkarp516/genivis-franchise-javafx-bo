package com.opethic.genivis.models;

public class ContentMst {
    private int id;
    private String contentName;

    public ContentMst(int id, String contentName) {
        this.id = id;
        this.contentName = contentName;
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
}
