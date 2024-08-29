package com.opethic.genivis.dto.bill_format;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillFormatDTO {

        @SerializedName("product_details")
        @Expose
        private List<BillFormatProductDetailsDTO> productDetails;
        @SerializedName("supplier_data")
        @Expose
        private BillFormatSupplierDataDTO supplierData;
        @SerializedName("customer_data")
        @Expose
        private BillFormatCustomerDataDTO customerData;
        @SerializedName("invoice_data")
        @Expose
        private BillFormatInvoiceDataDTO invoiceData;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("responseStatus")
        @Expose
        private Integer responseStatus;

        public List<BillFormatProductDetailsDTO> getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(List<BillFormatProductDetailsDTO> productDetails) {
            this.productDetails = productDetails;
        }

        public BillFormatSupplierDataDTO getSupplierData() {
            return supplierData;
        }

        public void setSupplierData(BillFormatSupplierDataDTO supplierData) {
            this.supplierData = supplierData;
        }

        public BillFormatCustomerDataDTO getCustomerData() {
            return customerData;
        }

        public void setCustomerData(BillFormatCustomerDataDTO customerData) {
            this.customerData = customerData;
        }

        public BillFormatInvoiceDataDTO getInvoiceData() {
            return invoiceData;
        }

        public void setInvoiceData(BillFormatInvoiceDataDTO invoiceData) {
            this.invoiceData = invoiceData;
        }

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
    }
