package com.pwccn.esg.model;


import com.pwccn.esg.dto.IndicatorDTO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "indicator")
public class IndicatorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer level;

    private String description;


    /* extensive attributes */

    private String type;

    /* internal */
    @Column(name = "parent", insertable = false, updatable = false)
    private Integer parentId;

    /* relationship */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private ModuleEntity module;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private IndicatorEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<IndicatorEntity> children = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "data_id")
    private IndicatorDataEntity indicatorData;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    public IndicatorEntity() {
    }

    public static void DTO2Entity(IndicatorEntity entity, IndicatorDTO dto, boolean setID, IndicatorEntity parent) {
        if(setID) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
        entity.setType(dto.getType());
        entity.setParent(parent);
        entity.setDescription(dto.getDescription());
        if(dto.getLevel() == 3){
            entity.setIndicatorData(new IndicatorDataEntity(dto.getIndicatorDataDTO()));
        } else {
            entity.setIndicatorData(null);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public ModuleEntity getModule() {
        return module;
    }

    public void setModule(ModuleEntity module) {
        this.module = module;
    }

    public IndicatorEntity getParent() {
        return parent;
    }

    public void setParent(IndicatorEntity parent) {
        this.parent = parent;
    }

    public Set<IndicatorEntity> getChildren() {
        return children;
    }


    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IndicatorDataEntity getIndicatorData() {
        return indicatorData;
    }

    public void setIndicatorData(IndicatorDataEntity indicatorDataEntity) {
        this.indicatorData = indicatorDataEntity;
    }
}
