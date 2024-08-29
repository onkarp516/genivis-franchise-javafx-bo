package com.opethic.genivis.dto;

import javafx.beans.property.SimpleStringProperty;

public class DoctorMasterDTO {

    private int id;
    private String doctorName;
    private String hospitalName;
    private String hospitalAddress;
    private String mobileNumber;
    private String commision;
    private String qualification;
    private String registerNo;
    private String specialization;


    public DoctorMasterDTO(int id, String doctorName, String hospitalName, String hospitalAddress, String mobileNumber, String commision, String qualification, String registerNo, String specialization) {
        this.id = id;
        this.doctorName = doctorName;
        this.hospitalName = hospitalName;
        this.hospitalAddress = hospitalAddress;
        this.mobileNumber = mobileNumber;
        this.commision = commision;
        this.qualification = qualification;
        this.registerNo = registerNo;
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCommision() {
        return commision;
    }

    public void setCommision(String commision) {
        this.commision = commision;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
