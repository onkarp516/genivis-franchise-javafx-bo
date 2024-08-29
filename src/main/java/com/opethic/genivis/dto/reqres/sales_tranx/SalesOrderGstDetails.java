package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SalesOrderGstDetails {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("gstNo")
        @Expose
        private String gstNo;
        @SerializedName("state")
        @Expose
        private String state;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getGstNo() {
            return gstNo;
        }

        public void setGstNo(String gstNo) {
            this.gstNo = gstNo;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

    }

