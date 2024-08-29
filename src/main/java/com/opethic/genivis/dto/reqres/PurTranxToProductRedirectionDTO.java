package com.opethic.genivis.dto.reqres;

import java.util.List;

public class PurTranxToProductRedirectionDTO {

    String redirect;
    String tranxDate;
    String ledegrId;
    String gstNum;
    String purSerNum;
    String challanNo;
    String purAcc;
    String challanDate;

    private List<PurTranxToPrdRediRowDTO> batchWindowTableDTOList;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getTranxDate() {
        return tranxDate;
    }

    public void setTranxDate(String tranxDate) {
        this.tranxDate = tranxDate;
    }

    public String getLedegrId() {
        return ledegrId;
    }

    public void setLedegrId(String ledegrId) {
        this.ledegrId = ledegrId;
    }

    public String getGstNum() {
        return gstNum;
    }

    public void setGstNum(String gstNum) {
        this.gstNum = gstNum;
    }

    public String getPurSerNum() {
        return purSerNum;
    }

    public void setPurSerNum(String purSerNum) {
        this.purSerNum = purSerNum;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getPurAcc() {
        return purAcc;
    }

    public void setPurAcc(String purAcc) {
        this.purAcc = purAcc;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }
}
