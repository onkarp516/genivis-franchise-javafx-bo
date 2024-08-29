package com.opethic.genivis.dto.bill_format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillFormatProductDetailsDTO {

        @SerializedName("product_id")
        @Expose
        private Integer productId;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("details_id")
        @Expose
        private Integer detailsId;
        @SerializedName("unit_conv")
        @Expose
        private Double unitConv;
        @SerializedName("qty")
        @Expose
        private Double qty;
        @SerializedName("rate")
        @Expose
        private Double rate;
        @SerializedName("base_amt")
        @Expose
        private Double baseAmt;
        @SerializedName("final_amt")
        @Expose
        private Double finalAmt;
        @SerializedName("Gst")
        @Expose
        private Double gst;
        @SerializedName("packageId")
        @Expose
        private Integer packageId;
        @SerializedName("pack_name")
        @Expose
        private String packName;
        @SerializedName("flavourId")
        @Expose
        private String flavourId;
        @SerializedName("flavour_name")
        @Expose
        private String flavourName;
        @SerializedName("b_details_id")
        @Expose
        private String bDetailsId;
        @SerializedName("b_no")
        @Expose
        private String bNo;
        @SerializedName("is_batch")
        @Expose
        private Boolean isBatch;
        @SerializedName("unitId")
        @Expose
        private Integer unitId;
        @SerializedName("unit_name")
        @Expose
        private String unitName;

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Integer getDetailsId() {
            return detailsId;
        }

        public void setDetailsId(Integer detailsId) {
            this.detailsId = detailsId;
        }

        public Double getUnitConv() {
            return unitConv;
        }

        public void setUnitConv(Double unitConv) {
            this.unitConv = unitConv;
        }

        public Double getQty() {
            return qty;
        }

        public void setQty(Double qty) {
            this.qty = qty;
        }

        public Double getRate() {
            return rate;
        }

        public void setRate(Double rate) {
            this.rate = rate;
        }

        public Double getBaseAmt() {
            return baseAmt;
        }

        public void setBaseAmt(Double baseAmt) {
            this.baseAmt = baseAmt;
        }

        public Double getFinalAmt() {
            return finalAmt;
        }

        public void setFinalAmt(Double finalAmt) {
            this.finalAmt = finalAmt;
        }

        public Double getGst() {
            return gst;
        }

        public void setGst(Double gst) {
            this.gst = gst;
        }

        public Integer getPackageId() {
            return packageId;
        }

        public void setPackageId(Integer packageId) {
            this.packageId = packageId;
        }

        public String getPackName() {
            return packName;
        }

        public void setPackName(String packName) {
            this.packName = packName;
        }

        public String getFlavourId() {
            return flavourId;
        }

        public void setFlavourId(String flavourId) {
            this.flavourId = flavourId;
        }

        public String getFlavourName() {
            return flavourName;
        }

        public void setFlavourName(String flavourName) {
            this.flavourName = flavourName;
        }

        public String getbDetailsId() {
            return bDetailsId;
        }

        public void setbDetailsId(String bDetailsId) {
            this.bDetailsId = bDetailsId;
        }

        public String getbNo() {
            return bNo;
        }

        public void setbNo(String bNo) {
            this.bNo = bNo;
        }

        public Boolean getIsBatch() {
            return isBatch;
        }

        public void setIsBatch(Boolean isBatch) {
            this.isBatch = isBatch;
        }

        public Integer getUnitId() {
            return unitId;
        }

        public void setUnitId(Integer unitId) {
            this.unitId = unitId;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }
    }
