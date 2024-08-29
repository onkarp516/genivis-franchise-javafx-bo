package com.opethic.genivis.dto.pur_invoice.reqres;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PurchaseSerialRes {
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("responseStatus")
        @Expose
        private Integer responseStatus;
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("serialNo")
        @Expose
        private String serialNo;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(Integer responseStatus) {
            this.responseStatus = responseStatus;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

}