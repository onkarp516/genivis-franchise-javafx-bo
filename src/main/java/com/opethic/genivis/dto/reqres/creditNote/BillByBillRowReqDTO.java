package com.opethic.genivis.dto.reqres.creditNote;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BillByBillRowReqDTO implements Serializable {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("paid_amt")
    @Expose
    private Double paidAmt;
    @SerializedName("perticulars")
    @Expose
    private BillByBillPerticulars perticulars;
    @SerializedName("bank_payment_type")
    @Expose
    private String paymentType;
    @SerializedName("bank_payment_no")
    @Expose
    private String tranxNo;

    @SerializedName("bank_acc_name")
    @Expose
    private String bankName;

    @SerializedName("payment_date")
    @Expose
    private String paymentDate;


    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(Double paidAmt) {
        this.paidAmt = paidAmt;
    }

    public BillByBillPerticulars getPerticulars() {
        return perticulars;
    }

    public void setPerticulars(BillByBillPerticulars perticulars) {
        this.perticulars = perticulars;
    }

    public BillByBillRowReqDTO(String type, Double paidAmt, BillByBillPerticulars perticulars) {
        this.type = type;
        this.paidAmt = paidAmt;
        this.perticulars = perticulars;
    }

    public BillByBillRowReqDTO(String type, Double paidAmt, BillByBillPerticulars perticulars, String paymentType,
                               String tranxNo, String bankName,String paymentDate) {
        this.type = type;
        this.paidAmt = paidAmt;
        this.perticulars = perticulars;
        this.paymentType = paymentType;
        this.tranxNo = tranxNo;
        this.bankName = bankName;
        this.paymentDate = paymentDate;

    }

    public BillByBillRowReqDTO() {
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTranxNo() {
        return tranxNo;
    }

    public void setTranxNo(String tranxNo) {
        this.tranxNo = tranxNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String toString() {
      /*  return "BillByBillRowReqDTO{" +
                "type='" + type + '\'' +
                ", paidAmt=" + paidAmt +
                ", perticulars=" + perticulars +
                ", paymentType='" + paymentType + '\'' +
                ", tranxNo='" + tranxNo + '\'' +
                ", bankName='" + bankName + '\'' +
                '}';*/
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("paidAmt", paidAmt);
        jsonObject.addProperty("perticulars", perticulars.toString());
        jsonObject.addProperty("paymentType", paymentType);
        jsonObject.addProperty("tranxNo", tranxNo);
        jsonObject.addProperty("bankName", bankName);
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }

}
