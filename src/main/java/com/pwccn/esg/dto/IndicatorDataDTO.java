package com.pwccn.esg.dto;

import com.pwccn.esg.model.IndicatorDataEntity;
import com.pwccn.esg.model.IndicatorEntity;

public class IndicatorDataDTO {

    private Integer id;
    private String unit;
    private String sections;
    private String newSections;
    private String context;
    private String newContext;

    private String status;

    public IndicatorDataDTO() {

    }

    public IndicatorDataDTO(IndicatorDataEntity data) {
        setId(data.getId());
        setContext(data.getContext());
        setSections(data.getSections());
        setUnit(data.getUnit());
        setNewContext(data.getNewContext());
        setNewSections(data.getNewSections());
        setStatus(data.getStatus());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSections() {
        return sections;
    }

    public String getNewContext() {
        return newContext;
    }

    public String getNewSections() {
        return newSections;
    }

    public void setNewContext(String newContext) {
        this.newContext = newContext;
    }

    public void setNewSections(String newSections) {
        this.newSections = newSections;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
