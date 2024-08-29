package com.opethic.genivis.dto;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SalesQuotationListResObjDTO implements Serializable {
    @SerializedName("data")
    @Expose
    private List<SalesQuotResObjDataDTO> data;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;

    public List<SalesQuotResObjDataDTO> getData() {
        return data;
    }

    public void setData(List<SalesQuotResObjDataDTO> data) {
        this.data = data;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
}
