package com.pwccn.esg.repository;

import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.model.ModuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndicatorRepository extends JpaRepository<IndicatorEntity, Integer> {

    List<IndicatorEntity> findByParentId(Integer parent);

    List<IndicatorEntity> findByName(String name);

    List<IndicatorEntity> findByLevel(Integer level);

    List<IndicatorEntity> findByCompanyAndModule(CompanyEntity companyEntity, ModuleEntity moduleEntity);
}
