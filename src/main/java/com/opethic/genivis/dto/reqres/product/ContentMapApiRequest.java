package com.opethic.genivis.dto.reqres.product;

import java.io.Serializable;


public class ContentMapApiRequest implements Serializable {
    private String id;
    private String contentType;
    private String content_power;
    private String content_package;
    private String contentTypeDose;

    public ContentMapApiRequest() {
    }

    public ContentMapApiRequest(String id, String contentType, String content_power, String content_package,
                                String contentTypeDose) {
        this.id = id;
        this.contentType = contentType;
        this.content_power = content_power;
        this.content_package = content_package;
        this.contentTypeDose = contentTypeDose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent_power() {
        return content_power;
    }

    public void setContent_power(String content_power) {
        this.content_power = content_power;
    }

    public String getContent_package() {
        return content_package;
    }

    public void setContent_package(String content_package) {
        this.content_package = content_package;
    }

    public String getContentTypeDose() {
        return contentTypeDose;
    }

    public void setContentTypeDose(String contentTypeDose) {
        this.contentTypeDose = contentTypeDose;
    }



}
