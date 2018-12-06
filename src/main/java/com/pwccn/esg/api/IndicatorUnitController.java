package com.pwccn.esg.api;

import com.pwccn.esg.dto.IndicatorUnitDTO;
import com.pwccn.esg.model.IndicatorUnitEntity;
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

@RestController
@RequestMapping(path = "/api/indicator-units")
public class IndicatorUnitController {

    private IndicatorUnitReposity indicatorUnitReposity;

    @Autowired
    public IndicatorUnitController(IndicatorUnitReposity indicatorUnitReposity) {
        this.indicatorUnitReposity = indicatorUnitReposity;
    }

    @ApiOperation(value = "Get all indicator units.")
    @GetMapping
    public ResponseEntity<List<IndicatorUnitDTO>> all() {
        List<IndicatorUnitEntity> units = indicatorUnitReposity.findAll();
        List<IndicatorUnitDTO> unitDTOs = new ArrayList<>();
        for (IndicatorUnitEntity unit : units) {
            unitDTOs.add(new IndicatorUnitDTO(unit));
        }
        return new ResponseEntity<>(unitDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create an indicator unit.", code = 201,
            notes = "The id in the request body is ignored. " +
                    "HTTP code 201 will be returned if the module is successfully created."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = IndicatorUnitDTO.class),
    })
    @PostMapping
    public ResponseEntity<IndicatorUnitDTO> create(@RequestBody IndicatorUnitDTO unitDTO) {
        IndicatorUnitEntity unit = new IndicatorUnitEntity();
        IndicatorUnitEntity.DTO2Entity(unit, unitDTO, false);
        IndicatorUnitEntity result = indicatorUnitReposity.save(unit);
        return new ResponseEntity<>(new IndicatorUnitDTO(result), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get an indicator unit by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = IndicatorUnitDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<IndicatorUnitDTO> get(@PathVariable Integer id) {
        Optional<IndicatorUnitEntity> unit = indicatorUnitReposity.findById(id);
        return unit.map(u -> new ResponseEntity<>(new IndicatorUnitDTO(u), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Update an indicator unit by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = IndicatorUnitDTO.class),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<IndicatorUnitDTO> update(@PathVariable Integer id, @RequestBody IndicatorUnitDTO unitDTO) {
        if (!unitDTO.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<IndicatorUnitEntity> unit = indicatorUnitReposity.findById(id);
        if (!unit.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        IndicatorUnitEntity unitToUpdate = unit.get();
        IndicatorUnitEntity.DTO2Entity(unitToUpdate, unitDTO, false);
        IndicatorUnitEntity result = indicatorUnitReposity.save(unitToUpdate);
        return new ResponseEntity<>(new IndicatorUnitDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete an indicator unit by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!indicatorUnitReposity.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        indicatorUnitReposity.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
