package com.pwccn.esg.api;

import com.pwccn.esg.dto.IndicatorDTO;
import com.pwccn.esg.model.IndicatorEntity;
import com.pwccn.esg.model.IndicatorUnitEntity;
import com.pwccn.esg.repository.IndicatorRepository;
import com.pwccn.esg.repository.IndicatorUnitReposity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/indicators")
public class IndicatorController {

    private IndicatorRepository indicatorRepository;
    private IndicatorUnitReposity indicatorUnitReposity;

    @Autowired
    public IndicatorController(IndicatorRepository indicatorRepository, IndicatorUnitReposity indicatorUnitReposity) {
        this.indicatorRepository = indicatorRepository;
        this.indicatorUnitReposity = indicatorUnitReposity;
    }

    @ApiOperation(value = "Get all indicators.")
    @GetMapping
    public ResponseEntity<List<IndicatorDTO>> all() {
        List<IndicatorEntity> indicators = indicatorRepository.findAll();
        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
        for (IndicatorEntity indicator : indicators) {
            indicatorDTOs.add(new IndicatorDTO(indicator));
        }
        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create an indicator.", code = 201,
            notes = "The id in the request body is ignored. " +
                    "HTTP code 201 will be returned if the module is successfully created."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = IndicatorDTO.class)
    })
    @PostMapping
    public ResponseEntity<IndicatorDTO> create(@RequestBody IndicatorDTO indicatorDTO) {
        IndicatorEntity parent = indicatorRepository.getOne(indicatorDTO.getParent());
        IndicatorUnitEntity unit = indicatorUnitReposity.getOne(indicatorDTO.getUnit());
        IndicatorEntity indicator = new IndicatorEntity();
        IndicatorEntity.DTO2Entity(indicator, indicatorDTO, false, parent, unit);
        IndicatorEntity result = indicatorRepository.save(indicator);
        return new ResponseEntity<>(new IndicatorDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Query some indicators.")
    @GetMapping(path = "/query")
    public ResponseEntity<List<IndicatorDTO>> query(@RequestParam Integer parent) {
        List<IndicatorEntity> indicators = indicatorRepository.findByParentId(parent);
        List<IndicatorDTO> indicatorDTOs = new ArrayList<>();
        for (IndicatorEntity indicator : indicators) {
            indicatorDTOs.add(new IndicatorDTO(indicator));
        }
        return new ResponseEntity<>(indicatorDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Get an indicator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = IndicatorDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<IndicatorDTO> get(@PathVariable Integer id) {
        Optional<IndicatorEntity> indicator = indicatorRepository.findById(id);
        return indicator.map(i -> new ResponseEntity<>(new IndicatorDTO(i), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(
            value = "Update a indicator by id.",
            notes = "ALL FIELDS instead of CHANGED ONES should be present in the request body."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = IndicatorDTO.class),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<IndicatorDTO> update(@PathVariable Integer id, @RequestBody IndicatorDTO indicatorDTO) {
        Optional<IndicatorEntity> indicator = indicatorRepository.findById(id);
        if (!indicator.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        IndicatorEntity parent = indicatorRepository.getOne(indicatorDTO.getParent());
        IndicatorUnitEntity unit = indicatorUnitReposity.getOne(indicatorDTO.getUnit());
        IndicatorEntity indicatorToUpdate = indicator.get();
        IndicatorEntity.DTO2Entity(indicatorToUpdate, indicatorDTO, false, parent, unit);
        IndicatorEntity result = indicatorRepository.save(indicatorToUpdate);
        return new ResponseEntity<>(new IndicatorDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a indicator by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!indicatorRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        indicatorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
