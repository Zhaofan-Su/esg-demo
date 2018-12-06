package com.pwccn.esg.dto;

import com.pwccn.esg.model.IndicatorUnitEntity;

public class IndicatorUnitDTO {

    private Integer id;
    private String name;

    public IndicatorUnitDTO() {
    }

    public IndicatorUnitDTO(IndicatorUnitEntity unit) {
        setId(unit.getId());
        setName(unit.getName());
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
}
