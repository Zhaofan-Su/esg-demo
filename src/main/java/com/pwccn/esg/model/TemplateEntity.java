package com.pwccn.esg.model;

import com.pwccn.esg.dto.TemplateDTO;
import com.pwccn.esg.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    @OneToOne(mappedBy = "templateEntity", fetch = FetchType.LAZY)
    private CompanyEntity companyEntity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "template_has_module",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "module_id"))
    private Set<ModuleEntity> modules = new HashSet<>();

    public TemplateEntity() {
    }

    public TemplateEntity(TemplateDTO templateDTO) {
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


    public CompanyEntity getCompanyEntity() {
        return companyEntity;
    }

    public void setCompanyEntity(CompanyEntity companyEntity) {
        this.companyEntity = companyEntity;
    }
}
