package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;


public class PatientDTO {


    private SimpleStringProperty id;
    private SimpleStringProperty patientName;

    private SimpleStringProperty patientCode;
    private SimpleStringProperty patientAddress;
    private SimpleStringProperty mobileNo;
    private SimpleStringProperty pincode;
    private SimpleStringProperty gender;
    private SimpleStringProperty age;
    private SimpleStringProperty birthDate;

    private SimpleStringProperty idNo;
    private SimpleStringProperty bloodGroup;
    private SimpleStringProperty weight;
    private SimpleStringProperty tbDiagnosisDate;
    private SimpleStringProperty tbTreatmentInitiationDate;
   // private SimpleStringProperty action;
    public PatientDTO(String id,String patientName,String patientCode, String patientAddress, String mobileNo,
                      String pincode, String gender,String age,String birthDate,String idNo,
                      String bloodGroup,String weight,String tbDiagnosisDate,String tbTreatmentInitiationDate ) {
        this.id = new SimpleStringProperty(id);
        this.patientName = new SimpleStringProperty(patientName);
        this.patientCode = new SimpleStringProperty(patientCode);
        this.patientAddress =  new SimpleStringProperty(patientAddress);
        this.mobileNo =  new SimpleStringProperty(mobileNo);
        this.pincode =  new SimpleStringProperty(pincode);
        this.gender =  new SimpleStringProperty(gender);
        this.age =  new SimpleStringProperty(age);
        this.birthDate =  new SimpleStringProperty(birthDate);
        this.idNo =  new SimpleStringProperty(idNo);
        this.bloodGroup =  new SimpleStringProperty(bloodGroup);
        this.weight =  new SimpleStringProperty(weight);
        this.tbDiagnosisDate =  new SimpleStringProperty(tbDiagnosisDate);
       this.tbTreatmentInitiationDate =  new SimpleStringProperty(tbTreatmentInitiationDate);
    }



    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id=new SimpleStringProperty(id);
    }
    public String getPatientName() {
        return patientName.get();
    }

    public SimpleStringProperty patientNameProperty() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName=new SimpleStringProperty(patientName);
    }

    public String getPatientAddress() {
        return patientAddress.get();
    }

    public SimpleStringProperty patientAddressProperty() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress=new SimpleStringProperty(patientAddress);
    }

    public String getMobileNo() {
        return mobileNo.get();
    }

    public SimpleStringProperty mobileNoProperty() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo=new SimpleStringProperty(mobileNo);
    }

    public String getPincode() {
        return pincode.get();
    }

    public SimpleStringProperty pincodeProperty() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode=new SimpleStringProperty(pincode);
    }

    public String getGender() {
        return gender.get();
    }

    public SimpleStringProperty genderProperty() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public String getAge() {
        return age.get();
    }

    public SimpleStringProperty ageProperty() {
        return age;
    }

    public void setAge(String age) {
        this.age=new SimpleStringProperty(age);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public SimpleStringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate=new SimpleStringProperty(birthDate);
    }

    public String getIdNo() {
        return idNo.get();
    }

    public SimpleStringProperty idNoProperty() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo=new SimpleStringProperty(idNo);
    }

    public String getBloodGroup() {
        return bloodGroup.get();
    }

    public SimpleStringProperty bloodGroupProperty() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup=new SimpleStringProperty(bloodGroup);
    }

    public String getWeight() {
        return weight.get();
    }

    public SimpleStringProperty weightProperty() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight=new SimpleStringProperty(weight);
    }

    public String getTbDiagnosisDate() {
        return tbDiagnosisDate.get();
    }

    public SimpleStringProperty tbDiagnosisDateProperty() {
        return tbDiagnosisDate;
    }

    public void setTbDiagnosisDate(String tbDiagnosisDate) {
        this.tbDiagnosisDate=new SimpleStringProperty(tbDiagnosisDate);
    }

    public String getTbTreatmentInitiationDate() {
        return tbTreatmentInitiationDate.get();
    }

    public SimpleStringProperty tbTreatmentInitiationDateProperty() {
        return tbTreatmentInitiationDate;
    }

    public void setTbTreatmentInitiationDate(String tbTreatmentInitiationDate) {
        this.tbTreatmentInitiationDate=new SimpleStringProperty(tbTreatmentInitiationDate);
    }

    public String getPatientCode() {
        return patientCode.get();
    }

    public SimpleStringProperty patientCodeProperty() {
        return patientCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode=new SimpleStringProperty(patientCode);
    }
}
