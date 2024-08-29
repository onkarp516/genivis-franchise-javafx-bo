package com.opethic.genivis.dto.pur_invoice;

public class UnitForPurInvoice {
    private String value;
    private String label;
    private Integer unitId;
    private String unitName;
    private String unitCode;
    private Double unitConversion;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public Double getUnitConversion() {
        return unitConversion;
    }

    public void setUnitConversion(Double unitConversion) {
        this.unitConversion = unitConversion;
    }

//    @Override
//    public String toString() {
//        return label;
//
//    }


    @Override
    public String toString() {
        return "UnitForPurInvoice{" +
                "value='" + value + '\'' +
                ", label='" + label + '\'' +
                ", unitId=" + unitId +
                ", unitName='" + unitName + '\'' +
                ", unitCode='" + unitCode + '\'' +
                ", unitConversion=" + unitConversion +
                '}';
    }
}
