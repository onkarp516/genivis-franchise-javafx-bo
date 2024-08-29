package com.opethic.genivis.dto.bill_format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillFormatSupplierDataDTO {

        @SerializedName("company_name")
        @Expose
        private String companyName;
        @SerializedName("company_address")
        @Expose
        private String companyAddress;
        @SerializedName("phone_number")
        @Expose
        private Long phoneNumber;
        @SerializedName("email_address")
        @Expose
        private String emailAddress;
        @SerializedName("gst_number")
        @Expose
        private String gstNumber;

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyAddress() {
            return companyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            this.companyAddress = companyAddress;
        }

        public Long getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(Long phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        public String getGstNumber() {
            return gstNumber;
        }

        public void setGstNumber(String gstNumber) {
            this.gstNumber = gstNumber;
        }
    }
