package com.opethic.genivis.dto;

import com.opethic.genivis.dto.reqres.sales_tranx.CSMultiPaymentDataDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.CSMultiPaymentModeDTO;
import javafx.beans.property.SimpleStringProperty;

public class CSMultiPaymentDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty bank;
    private SimpleStringProperty bankId;
    private SimpleStringProperty mode;
    private SimpleStringProperty modeId;
    private SimpleStringProperty amount;
    private SimpleStringProperty refID;
//    private CSMultiPaymentDataDTO selectedBank;
//    private CSMultiPaymentModeDTO selectedPaymentMode;


    public CSMultiPaymentDTO( String bank,String bankId, String mode,String modeId, String amount,String refID) {
//        this.id = new SimpleStringProperty(id);
        this.bank = new SimpleStringProperty( bank);
        this.bankId = new SimpleStringProperty( bankId);
        this.mode = new SimpleStringProperty(mode);
        this.modeId = new SimpleStringProperty(modeId);
        this.amount =new SimpleStringProperty( amount);
        this.refID =new SimpleStringProperty( refID);
    }
    public CSMultiPaymentDTO( String id , String bank,String bankId, String mode,String modeId, String amount,String refID) {
        this.id = new SimpleStringProperty(id);
        this.bank = new SimpleStringProperty( bank);
        this.bankId = new SimpleStringProperty( bankId);
        this.mode = new SimpleStringProperty(mode);
        this.modeId = new SimpleStringProperty(modeId);
        this.amount =new SimpleStringProperty( amount);
        this.refID =new SimpleStringProperty( refID);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getBank() {
        return bank.get();
    }

    public SimpleStringProperty bankProperty() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public String getMode() {
        return mode.get();
    }

    public SimpleStringProperty modeProperty() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode.set(mode);
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public String getRefID() {
        return refID.get();
    }

    public SimpleStringProperty refIDProperty() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID.set(refID);
    }

    public String getBankId() {
        return bankId.get();
    }

    public SimpleStringProperty bankIdProperty() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId.set(bankId);
    }

    public String getModeId() {
        return modeId.get();
    }

    public SimpleStringProperty modeIdProperty() {
        return modeId;
    }

    public void setModeId(String modeId) {
        this.modeId.set(modeId);
    }

//    public CSMultiPaymentDataDTO getSelectedBank() {
//        return selectedBank;
//    }
//
//    public void setSelectedBank(CSMultiPaymentDataDTO selectedBank) {
//        this.selectedBank = selectedBank;
//    }
//
//    public CSMultiPaymentModeDTO getSelectedPaymentMode() {
//        return selectedPaymentMode;
//    }
//
//    public void setSelectedPaymentMode(CSMultiPaymentModeDTO selectedPaymentMode) {
//        this.selectedPaymentMode = selectedPaymentMode;
//    }


    @Override
    public String toString() {
        return "CSMultiPaymentDTO{" +
                "id=" + id +
                ", bank=" + bank +
                ", bankId=" + bankId +
                ", mode=" + mode +
                ", modeId=" + modeId +
                ", amount=" + amount +
                ", refID=" + refID +
//                ", selectedBank=" + selectedBank +
//                ", selectedPaymentMode=" + selectedPaymentMode +
                '}';
    }
}
