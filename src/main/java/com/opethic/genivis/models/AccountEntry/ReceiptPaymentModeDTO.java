package com.opethic.genivis.models.AccountEntry;

import java.time.LocalDate;

public class ReceiptPaymentModeDTO {
    private int id;
    private String payment_mode;

    private int paymentId;

    private String refrenceNo;

    private LocalDate paymentDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaymentMode() {
        return payment_mode;
    }

    public void setPaymentMode(String paymentMode) {
        this.payment_mode = paymentMode;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public String getRefrenceNo() {
        return refrenceNo;
    }

    public void setRefrenceNo(String refrenceNo) {
        this.refrenceNo = refrenceNo;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public ReceiptPaymentModeDTO(int id, String paymentMode, int paymentID, String refrenceNo, LocalDate paymentDate) {
        this.id = id;
        this.payment_mode = paymentMode;
        this.paymentId = paymentID;
        this.refrenceNo = refrenceNo;
        this.paymentDate = paymentDate;
    }

    public ReceiptPaymentModeDTO() {
    }
}

