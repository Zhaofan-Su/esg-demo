package com.pwccn.esg.repository;

import com.pwccn.esg.model.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {

    CompanyEntity findByName(String name);
}

