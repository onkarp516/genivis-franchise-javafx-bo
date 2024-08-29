package com.opethic.genivis.models.master.ledger;


public class LicenseType {
    private String label;
    private Integer value;
    private String slugName;

    public LicenseType(String label, Integer value, String slugName) {
        this.label = label;
        this.value = value;
        this.slugName = slugName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }

    @Override
    public String toString() {
        return "" + label;
    }
}
