package com.pwccn.esg.dto;

import com.pwccn.esg.model.IndicatorEntity;

import java.util.Set;

public class IndicatorDTO {

    private Integer id;
    private String name;
    private Integer level;

    // extensive attribute
    private Integer type;

    // relationship
    private Integer parent;
    private Integer unit;

    public IndicatorDTO() {
    }

    public IndicatorDTO(IndicatorEntity indicator) {
        setId(indicator.getId());
        setName(indicator.getName());
        setLevel(indicator.getLevel());
        setType(indicator.getType());
        if (indicator.getParent() != null) {
            setParent(indicator.getParent().getId());
        }
        if (indicator.getUnit() != null) {
            setUnit(indicator.getUnit().getId());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
}
