package com.pwccn.esg.api;

import com.pwccn.esg.dto.ModuleDTO;
import com.pwccn.esg.dto.TemplateDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.model.TemplateEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.ModuleRepository;
import com.pwccn.esg.repository.TemplateRepository;
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

    private TemplateRepository templateRepository;
    private ModuleRepository moduleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    public TemplateController(TemplateRepository templateRepository, ModuleRepository moduleRepository) {
        this.templateRepository = templateRepository;
        this.moduleRepository = moduleRepository;
    }

    @ApiOperation(value = "Get all templates.")
    @GetMapping
    public ResponseEntity<List<TemplateDTO>> all() {
        List<TemplateEntity> templates = templateRepository.findAll();
        List<TemplateDTO> templateDTOs = new ArrayList<>();
        for (TemplateEntity template : templates) {
            templateDTOs.add(new TemplateDTO(template));
        }
        return new ResponseEntity<>(templateDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create a template.", code = 201,
            notes = "The id in the request body is ignored. " +
                    "HTTP code 201 will be returned if the template is successfully created."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = TemplateDTO.class),
    })
    @PostMapping
    public ResponseEntity<TemplateDTO> create(@RequestBody TemplateDTO templateDTO) {
        TemplateEntity module = templateRepository.save(new TemplateEntity(templateDTO));
        return new ResponseEntity<>(new TemplateDTO(module), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Get a template by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<TemplateDTO> get(@PathVariable Integer id) {
        Optional<TemplateEntity> template = templateRepository.findById(id);
        return template.map(t -> new ResponseEntity<>(new TemplateDTO(t), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Get a template by name.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
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

    @ApiOperation(value = "Update a template by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok", response = TemplateDTO.class),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<TemplateDTO> update(@PathVariable Integer id, @RequestBody TemplateDTO templateDTO) {
        if (!templateDTO.getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<TemplateEntity> template = templateRepository.findById(id);
        if (!template.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        TemplateEntity templateToUpdate = template.get();
        templateToUpdate.update(templateDTO);
        TemplateEntity result = templateRepository.save(templateToUpdate);
        return new ResponseEntity<>(new TemplateDTO(result), HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a template by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!templateRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        templateRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Delete a template by  company name.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @DeleteMapping(path = "/{companyName}")
    public ResponseEntity<Void> deleteByCName(@PathVariable String companyName) {
        CompanyEntity company = companyRepository.findByName(companyName);
        TemplateEntity templateEntity = company.getTemplateEntity();
        if (templateEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        templateRepository.delete(templateEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Get all sub modules of the template.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{id}/modules")
    public ResponseEntity<List<ModuleDTO>> getAllModules(@PathVariable Integer id) {
        Optional<TemplateEntity> template = templateRepository.findById(id);
        if (!template.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Set<ModuleEntity> modules = template.get().getModules();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (ModuleEntity module : modules) {
            moduleDTOs.add(new ModuleDTO(module));
        }
        return new ResponseEntity<>(moduleDTOs, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Create a sub module of the template.", code = 201,
            notes = "The id in the request body is ignored."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PostMapping(path = "/{id}/modules")
    @Transactional
    public ResponseEntity<ModuleDTO> createModule(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        Optional<TemplateEntity> template = templateRepository.findById(id);
        if (!template.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ModuleEntity moduleToCreate = new ModuleEntity(moduleDTO);
        ModuleEntity module = moduleRepository.save(moduleToCreate);
        TemplateEntity templateToUpdate = template.get();
        // There is no need to check whether add op is successful.
        // Because it is ok if the relationship already exists, which is really rare.
        templateToUpdate.getModules().add(module);
        templateRepository.save(templateToUpdate);
        return new ResponseEntity<>(new ModuleDTO(module), HttpStatus.CREATED);
    }

    @ApiOperation(
            value = "Create a sub module of the template.", code = 201,
            notes = "The id in the request body is ignored."
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PostMapping(path = "/{companyName}/modules")
    @Transactional
    public ResponseEntity<ModuleDTO> createModuleByName(@PathVariable String companyName, @RequestBody ModuleDTO moduleDTO) {
        CompanyEntity companyEntity = companyRepository.findByName(companyName);
        TemplateEntity templateEntity  = companyEntity.getTemplateEntity();
        Optional<TemplateEntity> template = templateRepository.findById(templateEntity.getId());
        if (!template.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ModuleEntity moduleToCreate = new ModuleEntity(moduleDTO);
        ModuleEntity module = moduleRepository.save(moduleToCreate);
        TemplateEntity templateToUpdate = template.get();
        // There is no need to check whether add op is successful.
        // Because it is ok if the relationship already exists, which is really rare.
        templateToUpdate.getModules().add(module);
        templateRepository.save(templateToUpdate);
        return new ResponseEntity<>(new ModuleDTO(module), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Associate the module with the template.", code = 204)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 409, message = "Conflict"),
    })
    @PutMapping(path = "/{id}/modules/{mid}")
    public ResponseEntity<Void> setModule(@PathVariable Integer id, @PathVariable Integer mid) {
        Optional<TemplateEntity> template = templateRepository.findById(id);
        if (!template.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<ModuleEntity> module = moduleRepository.findById(mid);
        if (!module.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TemplateEntity templateToUpdate = template.get();
        boolean success = templateToUpdate.getModules().add(module.get());
        if (!success) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        templateRepository.save(templateToUpdate);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
