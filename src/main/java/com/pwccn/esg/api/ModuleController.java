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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Autowired
    public ModuleController(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;

    }

    @ApiOperation(value = "Level 1 and level 2 admin get all modules.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All the modules have been gotten."),
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN1','ROLE_ADMIN2')")
    public ResponseEntity<List<ModuleDTO>> all() {
        List<ModuleEntity> modules = moduleRepository.findAll();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (ModuleEntity module : modules) {
            moduleDTOs.add(new ModuleDTO(module));
        }
        return new ResponseEntity<>(moduleDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Get all level-1 indicators of the company's module.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The company's module's level 1 indicators have been gotten."),
            @ApiResponse(code = 404, message = "The module doesn't exit."),
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
            if(indicator.getCompany().getId() == Cid && indicator.getLevel() == 1) {
                indicatorDTOs.add(new IndicatorDTO(indicator));
            }

        }
        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
    }

}
