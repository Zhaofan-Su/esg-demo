package com.pwccn.esg.model;

import com.pwccn.esg.dto.IndicatorUnitDTO;

import javax.persistence.*;

@Entity
@Table(name = "indicator_unit")
public class IndicatorUnitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    public IndicatorUnitEntity() {
    }

    public static void DTO2Entity(IndicatorUnitEntity entity, IndicatorUnitDTO dto, boolean setID) {
        if (setID) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
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
