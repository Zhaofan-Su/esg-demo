package com.pwccn.esg.dto;

import com.pwccn.esg.model.ModuleEntity;

public class ModuleDTO {

    private Integer id;

    private String name;

    public ModuleDTO() {
    }

    public ModuleDTO(ModuleEntity module) {
        setId(module.getId());
        setName(module.getName());
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
