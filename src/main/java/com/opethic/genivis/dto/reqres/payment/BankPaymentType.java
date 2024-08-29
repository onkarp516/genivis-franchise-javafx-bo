package com.opethic.genivis.dto.reqres.payment;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankPaymentType implements Serializable {
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("label", label);
        jsonObject.addProperty("value", value);
        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
}
