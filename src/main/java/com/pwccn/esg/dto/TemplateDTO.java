package com.pwccn.esg.dto;

import com.pwccn.esg.model.TemplateEntity;

public class TemplateDTO {

    private Integer id;
    private String name;
    private String companyName;
    private Integer companyId;

    public TemplateDTO() {
    }

    public TemplateDTO(TemplateEntity template) {
        setId(template.getId());
        setName(template.getName());
        setCompanyName(template.getCompanyEntity().getName());
        setCompanyId(template.getCompanyEntity().getId());
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
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


    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
