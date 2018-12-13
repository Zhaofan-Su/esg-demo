package com.pwccn.esg.dto;

public class DataAnalysisDTO {

    private String companyName;
    private Double data;
    private String unit;

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Double getData() {
        return data;
    }

    public void setData(Double data) {
        this.data = data;
    }
}
