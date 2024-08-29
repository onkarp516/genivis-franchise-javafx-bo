package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

public class CourierServiceDTO {

    private SimpleStringProperty id;
    private SimpleStringProperty service_name;
    private SimpleStringProperty contact_No;
    private SimpleStringProperty contact_person;
    private SimpleStringProperty service_Add;



    public CourierServiceDTO(String id,String service_name, String contact_No, String contact_person,
                      String service_Add) {
        this.id = new SimpleStringProperty(id);
        this.service_name = new SimpleStringProperty(service_name);
        this.contact_No =  new SimpleStringProperty(contact_No);
        this.contact_person =  new SimpleStringProperty(contact_person);
        this.service_Add =  new SimpleStringProperty(service_Add);

    }

    public CourierServiceDTO(String id, TextField tfcamAreaName, TextField tfcamAreaCode, TextField tfcamPincode) {
    }

    //Getters
    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }


    public String getService_name() {
        return service_name.get();
    }

    public String getCourierContactNo() {
        return contact_No.get();
    }

    public String getContact_person() {
        return contact_person.get();
    }

    public String getCourierServiceAddress() {
        return service_Add.get();
    }




//    Properties
    public SimpleStringProperty service_nameProperty() {
        return service_name;
    }
    public SimpleStringProperty contact_NoProperty() {return contact_No;}
    public SimpleStringProperty contact_personProperty() {return contact_person;}
    public SimpleStringProperty service_AddProperty() {return service_Add;}


//    Setters
    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }

    public void setService_name(String service_name) {
        this.service_name=new SimpleStringProperty(service_name);
    }

    public void setContact_No(String contact_No) {
        this.contact_No=new SimpleStringProperty(contact_No);
    }

    public void setContactPerson(String contact_person) {
        this.contact_person=new SimpleStringProperty(contact_person);
    }

    public void setService_Add(String service_Add) {
        this.service_Add=new SimpleStringProperty(service_Add);
    }


}
