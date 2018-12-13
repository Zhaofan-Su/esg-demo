package com.pwccn.esg.api;

import com.pwccn.esg.dto.IndicatorDTO;
import com.pwccn.esg.dto.IndicatorDataDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.IndicatorDataEntity;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.IndicatorDataRepository;
import com.pwccn.esg.repository.IndicatorRepository;
import com.pwccn.esg.repository.ModuleRepository;
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
    private CompanyRepository companyRepository;
    @Autowired
    private IndicatorDataRepository indicatorDataRepository;

//    @ApiOperation(value = "Get all indicators.")
//    @GetMapping
//    public ResponseEntity<List<IndicatorDTO>> all() {
//        List<IndicatorEntity> indicators = indicatorRepository.findAll();
//        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
//        for (IndicatorEntity indicator : indicators) {
//            indicatorDTOs.add(new IndicatorDTO(indicator));
//        }
//        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
//    }

    @ApiOperation(value = "Level 1 admin create an indicator for a company.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<Integer> create(@RequestBody IndicatorDTO indicatorDTO) {
        if(indicatorRepository.findById(indicatorDTO.getId()).isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<IndicatorEntity> parentOption = indicatorRepository.findById(indicatorDTO.getParent());
        IndicatorEntity parent = new IndicatorEntity();
        if(parentOption.isPresent()) {
            parent = parentOption.get();
        } else {
            parent = null;
        }
        IndicatorEntity indicator = new IndicatorEntity();
        IndicatorEntity.DTO2Entity(indicator, indicatorDTO, false, parent);
        CompanyEntity company = companyRepository.findById(indicatorDTO.getCompanyId()).get();
        indicator.setCompany(company);
        IndicatorEntity result = new IndicatorEntity();
        if(indicatorDTO.getModuleId() != null) {
            ModuleEntity module = moduleRepository.findById(indicatorDTO.getModuleId()).get();
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

//    @ApiOperation(value = "Query some indicators.")
//    @GetMapping(path = "/query")
//    public ResponseEntity<List<IndicatorDTO>> query(@RequestParam Integer parent) {
//        List<IndicatorEntity> indicators = indicatorRepository.findByParentId(parent);
//        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
//        for (IndicatorEntity indicator : indicators) {
//            indicatorDTOs.add(new IndicatorDTO(indicator));
//        }
//        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
//    }

//    @ApiOperation(value = "Get an indicator")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Ok", response = IndicatorDTO.class),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @GetMapping(path = "/{id}")
//    public ResponseEntity<IndicatorDTO> get(@PathVariable Integer id) {
//        Optional<IndicatorEntity> indicator = indicatorRepository.findById(id);
//        return indicator.map(i -> new ResponseEntity<>(new IndicatorDTO(i), HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

    @ApiOperation(value = "Level 1 admin update a indicator's attributes by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PutMapping(path = "/update")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
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
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    //@PreAuthorize("hasRole('ROLE_OPERATOR')")
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

    @ApiOperation(value = "Level 1 admin delete a indicator by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
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
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
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
