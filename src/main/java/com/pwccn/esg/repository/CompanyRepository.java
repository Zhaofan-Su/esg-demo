package com.pwccn.esg.repository;

import com.pwccn.esg.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    CompanyEntity findByName(String name);
    List<CompanyEntity> findByIndustry(String industry);
}

