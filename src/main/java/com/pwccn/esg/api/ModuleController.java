package com.pwccn.esg.api;

import com.pwccn.esg.dto.IndicatorDTO;
import com.pwccn.esg.dto.ModuleDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.IndicatorRepository;
import com.pwccn.esg.repository.ModuleRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/modules")
public class ModuleController {

    private ModuleRepository moduleRepository;
    private IndicatorRepository indicatorRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public ModuleController(ModuleRepository moduleRepository, IndicatorRepository indicatorRepository, CompanyRepository companyRepository) {
        this.moduleRepository = moduleRepository;
        this.indicatorRepository = indicatorRepository;
        this.companyRepository = companyRepository;
    }

    @ApiOperation(value = "Get all modules.")
    @GetMapping
    public ResponseEntity<List<ModuleDTO>> all() {
        List<ModuleEntity> modules = moduleRepository.findAll();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (ModuleEntity module : modules) {
            moduleDTOs.add(new ModuleDTO(module));
        }
        return new ResponseEntity<>(moduleDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create a module.", code = 201,
            notes = "The id in the request body is ignored. " +
                    "HTTP code 201 will be returned if the module is successfully created."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
    })
    @PostMapping
    public ResponseEntity<ModuleDTO> create(@RequestBody ModuleDTO moduleDTO) {
        ModuleEntity module = moduleRepository.save(new ModuleEntity(moduleDTO));
        return new ResponseEntity<>(new ModuleDTO(module), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get a module by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = ModuleDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<ModuleDTO> get(@PathVariable Integer id) {
        Optional<ModuleEntity> module = moduleRepository.findById(id);
        return module.map(m -> new ResponseEntity<>(new ModuleDTO(m), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Update a module by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = ModuleDTO.class),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<ModuleDTO> update(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        if (!moduleDTO.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<ModuleEntity> module = moduleRepository.findById(id);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ModuleEntity moduleToUpdate = module.get();
        moduleToUpdate.update(moduleDTO);
        ModuleEntity result = moduleRepository.save(moduleToUpdate);
        return new ResponseEntity<>(new ModuleDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a module by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!moduleRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        moduleRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get all level-1 indicators of the company's module.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{Cid}/{Mid}/indicators")
    public ResponseEntity<List<IndicatorDTO>> getAllIndicators(@PathVariable Integer Cid, @PathVariable Integer Mid ) {
        Optional<ModuleEntity> module = moduleRepository.findById(Mid);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<IndicatorEntity> indicators = module.get().getIndicators();
        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
        for (IndicatorEntity indicator : indicators) {
            if(indicator.getCompany().getId() == Cid) {
                indicatorDTOs.add(new IndicatorDTO(indicator));
            }
        }
        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create a root (level 1) indicator of the module.", code = 201,
            notes = "The level in the request body must be 1. The parent in the request body must NOT be present."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PostMapping(path = "/{CName}/{Mid}/indicators")
    @Transactional
    public ResponseEntity<IndicatorDTO> createRootIndicator(@PathVariable String CName, @PathVariable Integer Mid, @RequestBody IndicatorDTO indicatorDTO) {
        Optional<ModuleEntity> module = moduleRepository.findById(Mid);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!indicatorDTO.getLevel().equals(1)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (indicatorDTO.getParent() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        IndicatorEntity indicatorToCreate = new IndicatorEntity();
        IndicatorEntity.DTO2Entity(indicatorToCreate, indicatorDTO, false, null, null);

        indicatorToCreate.setCompany(companyRepository.findByName(CName));
        IndicatorEntity indicator = indicatorRepository.save(indicatorToCreate);
        ModuleEntity moduleToUpdate = module.get();
        // There is no need to check whether add op is successful.
        // Because it is ok if the relationship already exists, which is really rare.
        moduleToUpdate.getIndicators().add(indicator);
        moduleRepository.save(moduleToUpdate);
        return new ResponseEntity<>(new IndicatorDTO(indicator), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Associate the root (level 1) indicator with the module.", code = 204)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    @PutMapping(path = "/{id}/indicators/{iid}")
    public ResponseEntity<Void> setIndicator(@PathVariable Integer id, @PathVariable Integer iid) {
        Optional<ModuleEntity> module = moduleRepository.findById(id);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<IndicatorEntity> indicator = indicatorRepository.findById(iid);
        if (!indicator.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!indicator.get().getLevel().equals(1)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ModuleEntity moduleToUpdate = module.get();
        boolean success = moduleToUpdate.getIndicators().add(indicator.get());
        if (!success) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        moduleRepository.save(moduleToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
