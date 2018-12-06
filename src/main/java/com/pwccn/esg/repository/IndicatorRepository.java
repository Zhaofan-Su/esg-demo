package com.pwccn.esg.repository;

import com.pwccn.esg.model.IndicatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorRepository extends JpaRepository<IndicatorEntity, Integer> {

    List<IndicatorEntity> findByParentId(Integer parent);

}
