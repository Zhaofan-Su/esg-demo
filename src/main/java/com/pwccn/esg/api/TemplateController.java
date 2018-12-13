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
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<List<TemplateDTO>> all() {
        List<TemplateEntity> templates = templateRepository.findAll();
        List<TemplateDTO> templateDTOs = new ArrayList<>();
        for (TemplateEntity template : templates) {
            templateDTOs.add(new TemplateDTO(template));
        }
        return new ResponseEntity<>(templateDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Level 1 admin create a template.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
    })
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<Integer> create(@RequestBody TemplateDTO templateDTO) {

        CompanyEntity companyEntity = companyRepository.findByName(templateDTO.getCompanyName());
        TemplateEntity module = new TemplateEntity(templateDTO);
        module.setCompanyEntity(companyEntity);
        companyEntity.setTemplateEntity(module);
        templateRepository.save(module);
        companyRepository.save(companyEntity);
        return new ResponseEntity<>(module.getId(), HttpStatus.CREATED);
    }

//    @ApiOperation(value = "Get a template by id.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Ok", response = TemplateDTO.class),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @GetMapping(path = "/{id}")
//    public ResponseEntity<TemplateDTO> get(@PathVariable Integer id) {
//        Optional<TemplateEntity> template = templateRepository.findById(id);
//        return template.map(t -> new ResponseEntity<>(new TemplateDTO(t), HttpStatus.OK))
//                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

//    @ApiOperation(value = "Get a template by name.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Ok"),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @GetMapping(path = "/name/{name}")
//    public ResponseEntity<TemplateDTO> getByName(@PathVariable String name) {
//        TemplateEntity template = templateRepository.findByName(name);
//        CompanyEntity company = companyRepository.findByName(name);
//        if(template == null && company == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else if(template != null && company == null) {
//            return new ResponseEntity<>(new TemplateDTO(template), HttpStatus.OK);
//        } else {
//            TemplateEntity templateEntity = company.getTemplateEntity();
//            return new ResponseEntity<>(new TemplateDTO(templateEntity), HttpStatus.OK);
//        }
//
//    }
//
//    @ApiOperation(value = "Update a template by id.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Ok", response = TemplateDTO.class),
//            @ApiResponse(code = 400, message = "Bad Request"),
//    })
//    @PutMapping(path = "/{id}")
//    public ResponseEntity<TemplateDTO> update(@PathVariable Integer id, @RequestBody TemplateDTO templateDTO) {
//        if (!templateDTO.getId().equals(id)) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        Optional<TemplateEntity> template = templateRepository.findById(id);
//        if (!template.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//        TemplateEntity templateToUpdate = template.get();
//        templateToUpdate.update(templateDTO);
//        TemplateEntity result = templateRepository.save(templateToUpdate);
//        return new ResponseEntity<>(new TemplateDTO(result), HttpStatus.OK);
//    }

    @ApiOperation(value = "Level 1 admin delete a template by id.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
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

//    @ApiOperation(value = "Delete a template by  company name.")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @DeleteMapping(path = "/byName/{companyName}")
//    public ResponseEntity<Void> deleteByCName(@PathVariable String companyName) {
//        CompanyEntity company = companyRepository.findByName(companyName);
//        TemplateEntity templateEntity = company.getTemplateEntity();
//        if (templateEntity == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        templateRepository.delete(templateEntity);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @ApiOperation(value = "Get all sub modules of the template.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(path = "/{companyId}/modules")
    public ResponseEntity<List<ModuleDTO>> getAllModules(@PathVariable Integer companyId) {
        CompanyEntity company = companyRepository.getOne(companyId);
        TemplateEntity template = company.getTemplateEntity();
        Set<ModuleEntity> modules = template.getModules();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (ModuleEntity module : modules) {
            moduleDTOs.add(new ModuleDTO(module));
        }
        return new ResponseEntity<>(moduleDTOs, HttpStatus.OK);
    }

    @ApiOperation(value = "Get the template id by its company's id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @GetMapping(value = "/getTemplateId/{id}")
    public ResponseEntity<Integer> getTemplateId(@PathVariable Integer id) {
        CompanyEntity company = companyRepository.getOne(id);
        if(company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        TemplateDTO template = new TemplateDTO(company.getTemplateEntity());
        return new ResponseEntity<>(template.getId(), HttpStatus.OK);
    }

//    @ApiOperation(
//            value = "Create a sub module of the template.", code = 201,
//            notes = "The id in the request body is ignored."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @PostMapping(path = "/{id}/modules")
//    @Transactional
//    public ResponseEntity<ModuleDTO> createModule(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
//        Optional<TemplateEntity> template = templateRepository.findById(id);
//        if (!template.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        ModuleEntity moduleToCreate = new ModuleEntity(moduleDTO);
//        ModuleEntity module = moduleRepository.save(moduleToCreate);
//        TemplateEntity templateToUpdate = template.get();
//        // There is no need to check whether add op is successful.
//        // Because it is ok if the relationship already exists, which is really rare.
//        templateToUpdate.getModules().add(module);
//        templateRepository.save(templateToUpdate);
//        return new ResponseEntity<>(new ModuleDTO(module), HttpStatus.CREATED);
//    }

//    @ApiOperation(
//            value = "Create a sub module of the template.", code = 201,
//            notes = "The id in the request body is ignored."
//    )
//    @ApiResponses(value = {
//            @ApiResponse(code = 201, message = "Created", response = ModuleDTO.class),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @PostMapping(path = "/{companyName}/module")
//    @Transactional
//    public ResponseEntity<ModuleDTO> createModuleByName(@PathVariable String companyName, @RequestBody ModuleDTO moduleDTO) {
//        CompanyEntity companyEntity = companyRepository.findByName(companyName);
//        TemplateEntity templateEntity  = companyEntity.getTemplateEntity();
//        Optional<TemplateEntity> template = templateRepository.findById(templateEntity.getId());
//        if (!template.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        ModuleEntity moduleToCreate = new ModuleEntity(moduleDTO);
//        ModuleEntity module = moduleRepository.save(moduleToCreate);
//        TemplateEntity templateToUpdate = template.get();
//        // There is no need to check whether add op is successful.
//        // Because it is ok if the relationship already exists, which is really rare.
//        templateToUpdate.getModules().add(module);
//        templateRepository.save(templateToUpdate);
//        return new ResponseEntity<>(new ModuleDTO(module), HttpStatus.CREATED);
//    }

//    @ApiOperation(value = "Associate the module with the template.", code = 204)
//    @ApiResponses(value = {
//            @ApiResponse(code = 204, message = "No Content"),
//            @ApiResponse(code = 404, message = "Not Found"),
//            @ApiResponse(code = 409, message = "Conflict"),
//    })
//    @PutMapping(path = "/{id}/modules/{mid}")
//    public ResponseEntity<Void> setModule(@PathVariable Integer id, @PathVariable Integer mid) {
//        Optional<TemplateEntity> template = templateRepository.findById(id);
//        if (!template.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        Optional<ModuleEntity> module = moduleRepository.findById(mid);
//        if (!module.isPresent()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        TemplateEntity templateToUpdate = template.get();
//        boolean success = templateToUpdate.getModules().add(module.get());
//        if (!success) {
//            return new ResponseEntity<>(HttpStatus.CONFLICT);
//        }
//        templateRepository.save(templateToUpdate);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

}
