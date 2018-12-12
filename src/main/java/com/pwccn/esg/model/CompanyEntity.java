package com.pwccn.esg.model;

import com.pwccn.esg.dto.CompanyDTO;
import com.pwccn.esg.dto.UserDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "company")
public class CompanyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String industry;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserEntity> users;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "template_id")
    private TemplateEntity templateEntity;



    public CompanyEntity() {

    }

    public CompanyEntity(CompanyDTO companyDTO) {
        setId(companyDTO.getId());
        setName(companyDTO.getName());
        setIndustry(companyDTO.getIndustry());
        List<UserEntity> userEntities = new ArrayList<>();
        for(UserDTO dto : companyDTO.getUsers()) {
            userEntities.add(new UserEntity(dto));
        }
        setUsers(userEntities);
    }
    public void setTemplateEntity(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
    }

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getIndustry() {
        return industry;
    }
}
