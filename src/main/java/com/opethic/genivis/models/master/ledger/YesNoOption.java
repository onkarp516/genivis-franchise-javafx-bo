package com.opethic.genivis.models.master.ledger;


public class YesNoOption {
    private Boolean value;
    private String label;

    public YesNoOption(Boolean value, String label) {
        this.value = value;
        this.label = label;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
