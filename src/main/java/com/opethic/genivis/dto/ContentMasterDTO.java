package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;
import java.lang.reflect.Method;

public class ContentMasterDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty contentName;



    public ContentMasterDTO(String id, String contentName) {
        this.id = new SimpleStringProperty(id);
        this.contentName = new SimpleStringProperty(contentName);


    }

    //Getters
    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }


    public String getContentName() {
        return contentName.get();
    }






//    Properties
    public SimpleStringProperty contentNameProperty() {
        return contentName;
    }


//    Setters
    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }

    public void setContentName(String contentName) {
        this.contentName=new SimpleStringProperty(contentName);
    }

    public Object getPropertyByName(String propertyName) throws Exception {
        Method method = this.getClass().getMethod(propertyName + "Property", (Class[]) null);
        return method.invoke(this);
    }
}
