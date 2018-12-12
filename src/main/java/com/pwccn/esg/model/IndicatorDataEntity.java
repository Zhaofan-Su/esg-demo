package com.pwccn.esg.model;

import com.pwccn.esg.dto.IndicatorDataDTO;

import javax.persistence.*;

@Entity
@Table(name = "indicator_data")
public class IndicatorDataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String unit;
    private String context;
    private String newContext;
    private String sections;
    private String newSections;

    public IndicatorDataEntity() {

    }

    public IndicatorDataEntity(IndicatorDataDTO indicatorDataDTO) {
        setContext(indicatorDataDTO.getContext());
        setSections(indicatorDataDTO.getSections());
        setUnit(indicatorDataDTO.getUnit());
        setNewContext(indicatorDataDTO.getNewContext());
        setNewSections(indicatorDataDTO.getNewSections());
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getContext() {
        return context;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public void setNewSections(String newSections) {
        this.newSections = newSections;
    }

    public void setNewContext(String newContext) {
        this.newContext = newContext;
    }

    public String getNewSections() {
        return newSections;
    }

    public String getNewContext() {
        return newContext;
    }
}
