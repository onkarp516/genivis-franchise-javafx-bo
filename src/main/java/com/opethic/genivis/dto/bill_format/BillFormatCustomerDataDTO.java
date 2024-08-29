package com.opethic.genivis.dto.bill_format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillFormatCustomerDataDTO {
        @SerializedName("supplier_name")
        @Expose
        private String supplierName;
        @SerializedName("supplier_address")
        @Expose
        private String supplierAddress;
        @SerializedName("supplier_gstin")
        @Expose
        private Object supplierGstin;
        @SerializedName("supplier_phone")
        @Expose
        private Long supplierPhone;

        public String getSupplierName() {
            return supplierName;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public String getSupplierAddress() {
            return supplierAddress;
        }

        public void setSupplierAddress(String supplierAddress) {
            this.supplierAddress = supplierAddress;
        }

        public Object getSupplierGstin() {
            return supplierGstin;
        }

        public void setSupplierGstin(Object supplierGstin) {
            this.supplierGstin = supplierGstin;
        }

        public Long getSupplierPhone() {
            return supplierPhone;
        }

        public void setSupplierPhone(Long supplierPhone) {
            this.supplierPhone = supplierPhone;
        }

    }
