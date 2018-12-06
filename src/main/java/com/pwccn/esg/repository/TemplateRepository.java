package com.pwccn.esg.repository;

import com.pwccn.esg.model.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateEntity, Integer> {

    TemplateEntity findByName(String name);
}
