package com.pwccn.esg.repository;

import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    UserEntity findByUsername(String username);
    UserEntity findByUsernameAndCompany(String username, CompanyEntity companyEntity);
    UserEntity findByEmail(String email);
}
