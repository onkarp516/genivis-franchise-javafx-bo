package com.opethic.genivis.models.tranx.sales;

import java.util.List;

public class UnitLevelLst {
    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    private List<LevelAOpts> levelAOpts;

    public List<LevelAOpts> getLevelAOpts() {
        return levelAOpts;
    }

    public void setLevelAOpts(List<LevelAOpts> levelAOpts) {
        this.levelAOpts = levelAOpts;
    }


    @Override
    public String toString() {
        return "UnitLevelLst{" +
                "productId=" + productId +
                ", levelAOpts=" + levelAOpts +
                '}';
    }
}
