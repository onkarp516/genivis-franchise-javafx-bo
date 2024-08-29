package com.opethic.genivis.dto;

public class CompanySystemConfigDTO {
    private int id;
    private String display_name;
    private String slug;
    private boolean is_label;
    private int value;
    private String label="";

    public CompanySystemConfigDTO(int id, String display_name, String slug, boolean is_label, int value, String label) {
        this.id = id;
        this.display_name = display_name;
        this.slug = slug;
        this.is_label = is_label;
        this.value = value;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isIs_label() {
        return is_label;
    }

    public void setIs_label(boolean is_label) {
        this.is_label = is_label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


}
