package com.pwccn.esg.dto;

import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class CompanyDTO {

    private Integer id;
    private String name;
    private List<UserDTO> users;
    private String template;
    private String industry;
    public CompanyDTO() {

    }

    public CompanyDTO(CompanyEntity companyEntity) {
        setId(companyEntity.getId());
        setName(companyEntity.getName());
        List<UserDTO> userDTOs = new ArrayList<>();
        for(UserEntity userEntity:companyEntity.getUsers()) {
            userDTOs.add(new UserDTO(userEntity));
        }
        setIndustry(companyEntity.getIndustry());
        setUsers(userDTOs);
        if(companyEntity.getTemplateEntity() != null) {
            setTemplate(companyEntity.getTemplateEntity().getName());
        }
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}

