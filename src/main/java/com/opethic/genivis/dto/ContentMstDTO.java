package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ContentMstDTO {
    private SimpleIntegerProperty id;
    private SimpleStringProperty contentName;
    public static List<String> getPropertiesList() {
        List<String> list = new ArrayList<>();
        for (Method method : ContentPackageMstDTO.class.getMethods()) {
            String name = method.getName();
            if (name.endsWith("Property")) {
                list.add(name.replace("Property", ""));
            }
        }
        return list;
    }
    public Object getPropertyByName(String propertyName) throws Exception {
        Method method = this.getClass().getMethod(propertyName + "Property", (Class[]) null);
        return method.invoke(this);
    }

    public ContentMstDTO(SimpleIntegerProperty id, SimpleStringProperty contentName) {
        this.id = id;
        this.contentName = contentName;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getContentName() {
        return contentName.get();
    }

    public SimpleStringProperty contentNameProperty() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName.set(contentName);
    }
}
