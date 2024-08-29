package com.opethic.genivis.dto.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutletAppConfigRes {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("display_name")
    @Expose
    private String displayName;
    @SerializedName("value")
    @Expose
    private Integer value;
    @SerializedName("is_label")
    @Expose
    private Boolean isLabel;
    @SerializedName("label")
    @Expose
    private String label;

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

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public Boolean getIsLabel(){
        return this.isLabel;
    }
    public void setIsLabel(Boolean isLabel){
        this.isLabel = isLabel;
    }


}
