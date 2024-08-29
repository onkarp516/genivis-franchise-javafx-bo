package com.opethic.genivis.dto;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;

public class Person {
    String pName,pAddress,pCountry;
    Long pMobile;
    SimpleObjectProperty<Date> date;

    public Person(String pName, Long pMobile, String pAddress, String pCountry, Date date) {
        this.pName = pName;
        this.pMobile = pMobile;
        this.pAddress = pAddress;
        this.pCountry = pCountry;
        this.date=new SimpleObjectProperty<>(date);
    }

    public Date getDate() {
        return date.get();
    }

    public SimpleObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public String getpCountry() {
        return pCountry;
    }

    public void setpCountry(String pCountry) {
        this.pCountry = pCountry;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public Long getpMobile() {
        return pMobile;
    }

    public void setpMobile(Long pMobile) {
        this.pMobile = pMobile;
    }

    public String getpAddress() {
        return pAddress;
    }

    public void setpAddress(String pAddress) {
        this.pAddress = pAddress;
    }
}
