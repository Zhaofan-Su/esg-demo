package com.pwccn.esg.repository;


import com.pwccn.esg.model.DomainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainRepository extends JpaRepository<DomainEntity, Integer> {

    DomainEntity findByName(String name);
}
