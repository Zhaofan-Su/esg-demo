package com.pwccn.esg.repository;

import com.pwccn.esg.model.IndicatorUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndicatorUnitReposity extends JpaRepository<IndicatorUnitEntity, Integer> {
}
