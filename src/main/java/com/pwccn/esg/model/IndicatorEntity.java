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

    /* extensive attributes */

    @Column(columnDefinition = "TINYINT")
    private Integer type;

    /* internal */
    @Column(name = "parent", insertable = false, updatable = false)
    private Integer parentId;

    /* relationship */

    @ManyToMany(mappedBy = "indicators", fetch = FetchType.LAZY)
    private Set<ModuleEntity> modules = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private IndicatorEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<IndicatorEntity> children = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "unit")
    private IndicatorUnitEntity unit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "indicator_for_company",
            joinColumns = @JoinColumn(name = "indicator_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id"))
    private CompanyEntity company;

    public IndicatorEntity() {
    }

    public static void DTO2Entity(IndicatorEntity entity, IndicatorDTO dto, boolean setID, IndicatorEntity parent, IndicatorUnitEntity unit) {
        if (setID) {
            entity.setId(dto.getId());
        }
        entity.setName(dto.getName());
        entity.setLevel(dto.getLevel());
        entity.setType(dto.getType());
        entity.setParent(parent);
        entity.setUnit(unit);
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

    public Integer getParentId() {
        return parentId;
    }

    public Set<ModuleEntity> getModules() {
        return modules;
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

    public IndicatorUnitEntity getUnit() {
        return unit;
    }

    public void setUnit(IndicatorUnitEntity unit) {
        this.unit = unit;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public void setCompany(CompanyEntity company) {
        this.company = company;
    }
}
