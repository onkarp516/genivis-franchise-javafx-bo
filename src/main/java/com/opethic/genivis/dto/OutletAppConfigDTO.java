package com.opethic.genivis.dto;

public class OutletAppConfigDTO {
    private Integer id;
    private String slug;
    private String displayName;
    private Integer value;
    private Boolean isLabel;
    private String label;


    public OutletAppConfigDTO(Integer id, String slug, String displayName, Integer value, Boolean isLabel, String label) {
        this.id = id;
        this.slug = slug;
        this.displayName = displayName;
        this.value = value;
        this.isLabel = isLabel;
        this.label = label;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Boolean getIsLabel() {
        return isLabel;
    }

    public void setIsLabel(Boolean isLabel) {
        this.isLabel = isLabel;
    }


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "OutletAppConfigDTO{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", displayName='" + displayName + '\'' +
                ", value=" + value +
                ", isLabel=" + isLabel +
                ", label='" + label + '\'' +
                '}';
    }
}
