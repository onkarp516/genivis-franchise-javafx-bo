package com.opethic.genivis.models.master.ledger;

public class GSTType {
    private int value;
    private String label;

    public GSTType(int value, String label) {
        this.value = value;
        this.label = label;
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


    @Override
    public String toString() {
        return "" + label;
    }
}
