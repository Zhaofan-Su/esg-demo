package com.pwccn.esg.dto;

import com.pwccn.esg.model.IndicatorDataEntity;
import com.pwccn.esg.model.IndicatorEntity;

import java.util.Set;

public class IndicatorDTO {

    private Integer id;
    private String name;
    private Integer level;
    private String description;
    private Integer moduleId;
    private Integer companyId;

    // extensive attribute
    private String type;

    // relationship
    private Integer parent;


    private IndicatorDataDTO indicatorDataDTO;

    public IndicatorDTO() {
    }

    public IndicatorDTO(IndicatorEntity indicator) {
        setId(indicator.getId());
        setName(indicator.getName());
        setLevel(indicator.getLevel());
        setType(indicator.getType());
        setDescription(indicator.getDescription());
        setCompanyId(indicator.getCompany().getId());
        if(indicator.getLevel() == 1) {
            setModuleId(indicator.getModule().getId());
        }
        if (indicator.getParent() != null) {
            setParent(indicator.getParent().getId());
        }
        if(indicator.getLevel() == 3) {

            setIndicatorDataDTO(new IndicatorDataDTO(indicator.getIndicatorData()));
        } else {
            setIndicatorDataDTO(null);
        }
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public IndicatorDataDTO getIndicatorDataDTO() {
        return indicatorDataDTO;
    }

    public void setIndicatorDataDTO(IndicatorDataDTO indicatorDataDTO) {
        this.indicatorDataDTO = indicatorDataDTO;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }
}

