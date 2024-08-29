package com.opethic.genivis.dto.reqres.catalog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CFormulationResDTO {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<CFourmulationListDTO> responseObject;

    @Override
    public String toString() {
        return "CFormulationResDTO{" +
                "message='" + message + '\'' +
                ", responseStatus=" + responseStatus +
                ", responseObject=" + responseObject +
                '}';
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

    public List<CFourmulationListDTO> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<CFourmulationListDTO> responseObject) {
        this.responseObject = responseObject;
    }
}
