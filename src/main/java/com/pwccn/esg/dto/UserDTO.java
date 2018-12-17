package com.pwccn.esg.dto;

import com.pwccn.esg.model.Role;
import com.pwccn.esg.model.UserEntity;
import io.swagger.models.auth.In;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

    private Integer id;
    private String username;
    private String password;
    private String  role;
    private Integer companyId;
    private String contactor;
    private String telephone;
    private String email;

    public UserDTO(){

    }

    public UserDTO(UserEntity userEntity){
        setId(userEntity.getId());
        setUsername(userEntity.getUsername());
        setPassword(userEntity.getPassword());
        setRole(userEntity.getRoles().get(0).getName());
        if(userEntity.getRoles().get(0).getName() != "ROLE_ADMIN1") {
            setCompanyId(userEntity.getCompany().getId());
        }
        setContactor(userEntity.getContactor());
        setTelephone(userEntity.getTelephone());
        setEmail(userEntity.getEmail());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getContactor() {
        return contactor;
    }
}
