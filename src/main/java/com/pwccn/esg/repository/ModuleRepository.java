package com.pwccn.esg.repository;

import com.pwccn.esg.model.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Integer> {

    ModuleEntity findByName(String name);
}
