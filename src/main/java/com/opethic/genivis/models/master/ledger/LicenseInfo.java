package com.opethic.genivis.models.master.ledger;


import java.time.LocalDate;

public class LicenseInfo {
    private int id;
    private String LicenseTypeName;
    private String LicenseSlugName;
    private Integer LicenseTypeId;
    private String LicenseNo;
    private String LicenseExp;
    private String LicenseDocURL;

    public LicenseInfo(int id, String licenseTypeName, String licenseSlugName, Integer licenseTypeId, String licenseNo, String licenseExp, String licenseDocURL) {
        this.id = id;
        LicenseTypeName = licenseTypeName;
        LicenseSlugName = licenseSlugName;
        LicenseTypeId = licenseTypeId;
        LicenseNo = licenseNo;
        LicenseExp = licenseExp;
        LicenseDocURL = licenseDocURL;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicenseTypeName() {
        return LicenseTypeName;
    }

    public void setLicenseTypeName(String licenseTypeName) {
        LicenseTypeName = licenseTypeName;
    }

    public String getLicenseSlugName() {
        return LicenseSlugName;
    }

    public void setLicenseSlugName(String licenseSlugName) {
        LicenseSlugName = licenseSlugName;
    }

    public Integer getLicenseTypeId() {
        return LicenseTypeId;
    }

    public void setLicenseTypeId(Integer licenseTypeId) {
        LicenseTypeId = licenseTypeId;
    }

    public String getLicenseNo() {
        return LicenseNo;
    }

    public void setLicenseNo(String licenseNo) {
        LicenseNo = licenseNo;
    }

    public String getLicenseExp() {
        return LicenseExp;
    }

    public void setLicenseExp(String licenseExp) {
        LicenseExp = licenseExp;
    }

    public String getLicenseDocURL() {
        return LicenseDocURL;
    }

    public void setLicenseDocURL(String licenseDocURL) {
        LicenseDocURL = licenseDocURL;
    }
}
