package com.pwccn.esg.api;

import com.pwccn.esg.dto.ModuleDTO;
import com.pwccn.esg.dto.TemplateDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.model.TemplateEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.ModuleRepository;
import com.pwccn.esg.repository.TemplateRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(path = "/api/templates")
public class TemplateController {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @ApiOperation(value = "Get all templates.")
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<List<TemplateDTO>> all() {
        List<TemplateEntity> templates = templateRepository.findAll();
        List<TemplateDTO> templateDTOs = new ArrayList<>();
        for (TemplateEntity template : templates) {
            templateDTOs.add(new TemplateDTO(template));
        }
        return new ResponseEntity<>(templateDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Level 1 admin create a template for a company.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The template has been created successfully."),
            @ApiResponse(code = 400, message = "The company has already owned a template."),
            @ApiResponse(code = 404, message = "The company doesn't exit.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<TemplateDTO> create(@RequestBody TemplateDTO templateDTO) {
        if(companyRepository.findByName(templateDTO.getCompanyName()).getTemplateEntity() != null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CompanyEntity companyEntity = companyRepository.findByName(templateDTO.getCompanyName());
        if(companyEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TemplateEntity module = new TemplateEntity(templateDTO);
        module.setCompanyEntity(companyEntity);
        companyEntity.setTemplateEntity(module);
        templateRepository.save(module);
        companyRepository.save(companyEntity);
        return new ResponseEntity<>(new TemplateDTO(module), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get a template by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The template has been found."),
            @ApiResponse(code = 404, message = "The template doesn't exit."),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    @GetMapping(path = "/name/{name}")
    public ResponseEntity<TemplateDTO> getByName(@PathVariable String name) {
        TemplateEntity template = templateRepository.findByName(name);
        CompanyEntity company = companyRepository.findByName(name);
        if(template == null && company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if(template != null && company == null) {
            return new ResponseEntity<>(new TemplateDTO(template), HttpStatus.OK);
        } else {
            TemplateEntity templateEntity = company.getTemplateEntity();
            return new ResponseEntity<>(new TemplateDTO(templateEntity), HttpStatus.OK);
        }

    }

    @ApiOperation(value = "Level 1 admin delete a template by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The template has been deleted."),
            @ApiResponse(code = 404, message = "The template doesn't exit."),
    })
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if(templateRepository.findById(id).isPresent()) {
            TemplateEntity template  = templateRepository.findById(id).get();
            CompanyEntity company = companyRepository.findByName(template.getCompanyEntity().getName());
            company.setTemplateEntity(null);
            templateRepository.delete(template);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Get the template id by its company's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The company's template has been gotten."),
            @ApiResponse(code = 404, message = "The compant doesn't exit."),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @GetMapping(value = "/getTemplate/{Cid}")
    public ResponseEntity<TemplateDTO> getTemplateId(@PathVariable Integer Cid) {
        CompanyEntity company = companyRepository.getOne(Cid);
        if(company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TemplateDTO template = new TemplateDTO(company.getTemplateEntity());
        return new ResponseEntity<>(template, HttpStatus.OK);
    }

}
