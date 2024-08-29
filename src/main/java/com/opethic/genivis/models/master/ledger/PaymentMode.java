package com.opethic.genivis.models.master.ledger;


public class PaymentMode {
    private Integer id;
    private String paymentMode;
    private Integer isSelected;
    private Integer detailsId;

    public PaymentMode() {
    }

    public PaymentMode(Integer id, String paymentMode, Integer isSelected, Integer detailsId) {
        this.id = id;
        this.paymentMode = paymentMode;
        this.isSelected = isSelected;
        this.detailsId = detailsId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Integer getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Integer isSelected) {
        this.isSelected = isSelected;
    }

    public Integer getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Integer detailsId) {
        this.detailsId = detailsId;
    }
}
