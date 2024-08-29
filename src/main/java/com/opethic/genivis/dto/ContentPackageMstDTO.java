package com.opethic.genivis.dto;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import java.lang.reflect.Method;

public class ContentPackageMstDTO extends SimpleStringProperty {
    private SimpleIntegerProperty id;
    private SimpleStringProperty contentPackageName;

    public Object getPropertyByName(String propertyName) throws Exception {
        Method method = this.getClass().getMethod(propertyName + "Property", (Class[]) null);
        return method.invoke(this);
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

    public ContentPackageMstDTO(Integer id, String contentPackageName) {
        this.id = new SimpleIntegerProperty(id);
        this.contentPackageName = new SimpleStringProperty(contentPackageName);
    }
    public String getContentPackageName() {
        return contentPackageName.get();
    }

    public SimpleStringProperty contentPackageNameProperty() {
        return contentPackageName;
    }

    public void setContentPackageName(String contentPackageName) {
        this.contentPackageName.set(contentPackageName);
    }
}
