package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @version sprint_migration_fx 01
 * DateTime: 17-03-2024:17:18:00
 * @implNote mapping Response JsonArray of Tax List  into TaxListDTO Object
 * @auther ashwins@opethic.com
 **/

public class TaxListDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("gst_per")
    @Expose
    private String gstPer;
    @SerializedName("igst")
    @Expose
    private String igst;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGstPer() {
        return gstPer;
    }

    public void setGstPer(String gstPer) {
        this.gstPer = gstPer;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }
}
