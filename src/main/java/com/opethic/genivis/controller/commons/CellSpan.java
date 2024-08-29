package com.opethic.genivis.controller.commons;

public final class CellSpan {
    private final int rwoSpan;
    private final int columnSpan;

    public CellSpan(int rwoSpan, int columnSpan) {
        this.rwoSpan = rwoSpan;
        this.columnSpan = columnSpan;
    }

    public int getRwoSpan() {
        return rwoSpan;
    }

    public int getColumnSpan() {
        return columnSpan;
    }
}
