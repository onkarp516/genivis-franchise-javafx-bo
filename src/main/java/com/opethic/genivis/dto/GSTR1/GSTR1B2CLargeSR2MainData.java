package com.opethic.genivis.dto.GSTR1;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GSTR1B2CLargeSR2MainData implements Serializable {

    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("responseObject")
    @Expose
    private List<GSTR1B2CLargeSR2RespData> responseObject;
    @SerializedName("message")
    @Expose
    private Object message;
    @SerializedName("data")
    @Expose
    private Object data;
    @SerializedName("response")
    @Expose
    private Object response;

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<GSTR1B2CLargeSR2RespData> getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(List<GSTR1B2CLargeSR2RespData> responseObject) {
        this.responseObject = responseObject;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }


}
