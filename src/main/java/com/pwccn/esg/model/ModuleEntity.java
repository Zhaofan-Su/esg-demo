package com.pwccn.esg.model;

import com.pwccn.esg.dto.ModuleDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "module")
public class ModuleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "modules", fetch = FetchType.LAZY)
    private Set<TemplateEntity> templates = new HashSet<>();

    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    private Set<IndicatorEntity> indicators = new HashSet<>();

    public ModuleEntity() {
    }

    public ModuleEntity(ModuleDTO moduleDTO) {
        setId(moduleDTO.getId());
        setName(moduleDTO.getName());
    }

    public void update(ModuleDTO moduleDTO) {
        setName(moduleDTO.getName());
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

    public Set<TemplateEntity> getTemplates() {
        return templates;
    }

    public Set<IndicatorEntity> getIndicators() {
        return indicators;
    }
}
