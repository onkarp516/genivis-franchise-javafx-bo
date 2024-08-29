package com.opethic.genivis.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRtnDataListDTO;

import java.io.Serializable;

   public class SalesQuotationMainDTO implements Serializable {
        @SerializedName("responseStatus")
        @Expose
        private Integer responseStatus;
        @SerializedName("responseObject")//SalesQuotationListResObjDTO
        @Expose
        private SalesQuotationListResObjDTO responseObject;
        @SerializedName("message")
        @Expose
        private String message;

        public Integer getResponseStatus() {
            return responseStatus;
        }

        public void setResponseStatus(Integer responseStatus) {
            this.responseStatus = responseStatus;
        }

        public SalesQuotationListResObjDTO getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(SalesQuotationListResObjDTO responseObject) {
            this.responseObject = responseObject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


}
