package com.pwccn.esg.model;

import com.pwccn.esg.dto.TemplateDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "template")
public class TemplateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "template_has_module",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<ModuleEntity> modules = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "company_template",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id"))
    private CompanyEntity companyEntity;

    @OneToOne(fetch = FetchType.LAZY)
    private UserEntity userEntity;

    public TemplateEntity() {
    }

    public TemplateEntity(TemplateDTO templateDTO) {
        setId(templateDTO.getId());
        setName(templateDTO.getName());
    }

    public void update(TemplateDTO templateDTO) {
        setName(templateDTO.getName());
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

    public Set<ModuleEntity> getModules() {
        return modules;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }
}
