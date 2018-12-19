package com.pwccn.esg.api;

import com.pwccn.esg.dto.IndicatorDTO;
import com.pwccn.esg.dto.IndicatorDataDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.IndicatorDataEntity;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.repository.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/indicators")
public class IndicatorController {


    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private IndicatorRepository indicatorRepository;
    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private IndicatorDataRepository indicatorDataRepository;

    @ApiOperation(value = "Get all of the level 1 indicators of a module.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The module's all level 1 indicators have been gotten."),
            @ApiResponse(code = 204, message = "The module has no level 1 indicators."),
    })
    @GetMapping("/{moduleId}/getAllLevelOne")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<List<IndicatorDTO>> getAllLevelOne(@PathVariable Integer moduleId) {
        List<IndicatorEntity> indicatorEntities = indicatorRepository.findByLevel(1);
        if(indicatorEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<IndicatorDTO> result = new ArrayList<>();
        for(IndicatorEntity indicatorEntity :indicatorEntities) {
            if(indicatorEntity.getModule().getId() == moduleId) {
                result.add(new IndicatorDTO(indicatorEntity));
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "Level 1 and level 2 admin create an indicator for a company.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator has been created successfully."),
            @ApiResponse(code = 400, message = "The indicator has already exited.")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN1','ROLE_ADMIN2')")
    public ResponseEntity<Integer> create(@RequestBody IndicatorDTO indicatorDTO) {        //父指标
        Optional<IndicatorEntity> parentOption = indicatorRepository.findById(indicatorDTO.getParent());
        IndicatorEntity parent = new IndicatorEntity();
        if(parentOption.isPresent()) {
            parent = parentOption.get();
            for(IndicatorEntity i : parent.getChildren()) {
                if(indicatorDTO.getName().equals(i.getName())) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }
        } else {
            parent = null;
            if(indicatorDTO.getModuleId() != null) {
                for(IndicatorEntity i : moduleRepository.getOne(indicatorDTO.getModuleId()).getIndicators()) {
                    if(indicatorDTO.getName().equals(i.getName())) {
                        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }
        IndicatorEntity indicator = new IndicatorEntity();
        IndicatorEntity.DTO2Entity(indicator, indicatorDTO, false, parent);
        CompanyEntity company = companyRepository.findById(indicatorDTO.getCompanyId()).get();
        indicator.setCompany(company);
        IndicatorEntity result = new IndicatorEntity();
        if(indicatorDTO.getModuleId() != null) {
            ModuleEntity module = moduleRepository.findById(indicatorDTO.getModuleId()).get();
            if(!company.getTemplateEntity().getModules().contains(module)) {
                company.getTemplateEntity().getModules().add(module);
                templateRepository.save(company.getTemplateEntity());
            }
            indicator.setModule(module);
            result = indicatorRepository.save(indicator);
            if(result.getLevel() == 1) {
                module.getIndicators().add(result);
                moduleRepository.save(module);
            }
        } else {
            result = indicatorRepository.save(indicator);
        }
        return new ResponseEntity<>(result.getId(), HttpStatus.OK);
    }

    @ApiOperation(value = "Level 1 and level 2 admin update a indicator's attributes by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator's attributes have been updated."),
            @ApiResponse(code = 404, message = "The indicator doesn't exit."),
    })
    @PutMapping(path = "/update")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN1','ROLE_ADMIN2')")
    public ResponseEntity<IndicatorDTO> update(@RequestBody IndicatorDTO indicatorDTO) {
        Optional<IndicatorEntity> indicator = indicatorRepository.findById(indicatorDTO.getId());
        if (!indicator.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        IndicatorEntity indicatorToUpdate = indicator.get();
        indicatorToUpdate.setName(indicatorDTO.getName());
        indicatorToUpdate.setType(indicatorDTO.getType());
        indicatorToUpdate.setDescription(indicatorDTO.getDescription());
        indicatorToUpdate.getIndicatorData().setUnit(indicatorDTO.getIndicatorDataDTO().getUnit());
        IndicatorEntity result = indicatorRepository.save(indicatorToUpdate);
        return new ResponseEntity<>(new IndicatorDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Data operators update a level 3 indicator's data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator's data has been updated."),
    })
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping(path = "/updateData")
    public ResponseEntity updateData (@RequestBody IndicatorDTO indicatorDTO) {
        IndicatorEntity indicatorEntity = indicatorRepository.getOne(indicatorDTO.getId());
        IndicatorDataEntity indicatorDataEntity = indicatorDataRepository.getOne(indicatorDTO.getIndicatorDataDTO().getId());
        indicatorDataEntity.setSections(indicatorDTO.getIndicatorDataDTO().getSections());
        indicatorDataEntity.setContext(indicatorDTO.getIndicatorDataDTO().getContext());
        indicatorDataEntity.setStatus("待审核");
        indicatorEntity.setIndicatorData(indicatorDataEntity);
        indicatorDataRepository.save(indicatorDataEntity);
        indicatorRepository.save(indicatorEntity);

        return new ResponseEntity(HttpStatus.OK);

    }

    @ApiOperation("The data-auditor audits the data")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator's data has been audited."),
            @ApiResponse(code = 404, message = "The indicator doesn't exit."),
    })
    @PutMapping(value = "/auditData/{id}/{result}")
    @PreAuthorize("hasRole('ROLE_AUDITOR')")
    public ResponseEntity auditData(@PathVariable Integer id, @PathVariable String result) {
        IndicatorEntity indicator = indicatorRepository.getOne(id);
        if(indicator == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        IndicatorDataEntity indicatorData = indicator.getIndicatorData();
        if(indicatorData == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        indicatorData.setStatus(result);
        indicatorDataRepository.save(indicatorData);
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(value = "Level 1 and level 2 admin delete a indicator by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator has been deleted successfully."),
            @ApiResponse(code = 404, message = "The indicator doesn't exit."),
    })
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN1','ROLE_ADMIN2')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        IndicatorEntity indicator = indicatorRepository.getOne(id);
        if (indicator == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(indicator.getLevel() == 3) {
            //delete the indicator's data
            indicatorDataRepository.delete(indicator.getIndicatorData());
        } else {
            if(indicator.getLevel() == 1){
                indicator.getModule().getIndicators().remove(indicator);
                Set<IndicatorEntity> indicatorSecondS = indicator.getChildren();
                for(IndicatorEntity indicatorEntity : indicatorSecondS) {
                    for(IndicatorEntity indicatorEntity1 :indicatorEntity.getChildren()) {
                        indicatorRepository.delete(indicatorEntity1);
                    }
                    indicatorRepository.delete(indicatorEntity);
                }
            }
            else {
                for(IndicatorEntity indicatorEntity : indicator.getChildren()) {
                    indicatorRepository.delete(indicatorEntity);
                }
            }
        }
        indicatorRepository.delete(indicator);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Get all children of the indicator.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The indicator's children have been gotten."),
            @ApiResponse(code = 404, message = "The indicator doesn't exit."),
    })
    @GetMapping(path = "/{id}/children")
    public ResponseEntity<List<IndicatorDTO>> children(@PathVariable Integer id) {
        Optional<IndicatorEntity> indicator = indicatorRepository.findById(id);
        if (!indicator.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<IndicatorEntity> children = indicator.get().getChildren();
        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
        for (IndicatorEntity child : children) {
            indicatorDTOs.add(new IndicatorDTO(child));
        }
        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
    }
}
