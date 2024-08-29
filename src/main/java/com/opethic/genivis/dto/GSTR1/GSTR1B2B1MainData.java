package com.opethic.genivis.dto.GSTR1;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class GSTR1B2B1MainData {
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("responseStatus")
    @Expose
    private Integer responseStatus;
    @SerializedName("data")
    @Expose
    private List<GSTR1B2B1ResponseData> data;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Integer responseStatus) {
        this.responseStatus = responseStatus;
    }

    public List<GSTR1B2B1ResponseData> getData() {
        return data;
    }

    public void setData(List<GSTR1B2B1ResponseData> data) {
        this.data = data;
    }
}
