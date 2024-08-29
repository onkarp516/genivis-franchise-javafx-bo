package com.opethic.genivis.controller.tranx_sales.common;

public class TranxConsumer {
    private String clmnName;
    private int currentIndex;

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getClmnName() {
        return clmnName;
    }

    public void setClmnName(String clmnName) {
        this.clmnName = clmnName;
    }

    @Override
    public String toString() {
        return "TranxConsumer{" +
                "clmnName='" + clmnName + '\'' +
                ", currentIndex=" + currentIndex +
                '}';
    }

    public TranxConsumer(String clmnName, int currentIndex) {
        this.clmnName = clmnName;
        this.currentIndex = currentIndex;
    }
}
