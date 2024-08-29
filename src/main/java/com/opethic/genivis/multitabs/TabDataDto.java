package com.opethic.genivis.multitabs;

import java.util.List;

public class TabDataDto {
    private String title;
    private String fileName;
    private String slug;
    private List<String> cmpSlug;
    private String controllerName;


    public TabDataDto(String inTitle, String inFileName, String inSlug, List<String> inCmpSlug, String inControllerName) {
        this.title = inTitle;
        this.fileName = inFileName;
        this.slug = inSlug;
        this.cmpSlug = inCmpSlug;
        this.controllerName = inControllerName;

    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public List<String> getCmpSlug() {
        return cmpSlug;
    }

    public void setCmpSlug(List<String> cmpSlug) {
        this.cmpSlug = cmpSlug;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    @Override
    public String toString() {
        return "Slug => " + this.slug + " Title =>" + this.title + " FileName =>" + this.fileName + " CmpSlug" + this.cmpSlug.toString();
    }
}
