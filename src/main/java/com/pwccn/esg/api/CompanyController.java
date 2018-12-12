package com.pwccn.esg.api;

import com.pwccn.esg.dto.CompanyDTO;
import com.pwccn.esg.dto.ModuleDTO;
import com.pwccn.esg.dto.TemplateDTO;
import com.pwccn.esg.dto.UserDTO;
import com.pwccn.esg.model.*;
import com.pwccn.esg.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/companies")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private IndicatorRepository indicatorRepository;

    @Autowired
    private IndicatorDataRepository indicatorDataRepository;

//    @ApiOperation(value = "Get the template of a company.")
//    @GetMapping
//    public ResponseEntity<TemplateDTO> search(@RequestBody CompanyEntity company) {
//        TemplateEntity template = company.getTemplateEntity();
//        return new ResponseEntity<>(new TemplateDTO(template), HttpStatus.OK);
//    }

    @ApiOperation(value = "Get all companies")
    @ApiResponse(code = 200, message = "OK")
    @GetMapping(value = "/all")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<List<CompanyDTO>> getAll() {
        List<CompanyEntity> companyEntities = companyRepository.findAll();
        List<CompanyDTO> companyDTOS = new ArrayList<>();
        for (CompanyEntity companyEntity : companyEntities) {
            if(!companyEntity.getName().equals("PWC")) {
                companyDTOS.add(new CompanyDTO(companyEntity));
            }
        }

        return new ResponseEntity<>(companyDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "Get the admin users of a company by id.")
    @GetMapping(value = "/{id}/allAdminUsers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<List<UserDTO>> getAdminUsers(@PathVariable Integer id) {
        CompanyEntity company = companyRepository.getOne(id);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<UserEntity> userEntities = company.getUsers();
        if(userEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for(UserEntity userEntity : userEntities) {
            if(userEntity.getRoles().get(0).getName() == "ROLE_ADMIN2") {
                UserDTO userDTO = new UserDTO(userEntity);
                userDTOS.add(userDTO);
            }
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @ApiOperation(value = "the level 2 admin get all users of his company")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping(value = "/{id}/getAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    public ResponseEntity<List<UserDTO>> getUsers (@PathVariable Integer id) {
        CompanyEntity company = companyRepository.getOne(id);
        if(company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<UserEntity> userEntities = company.getUsers();
        if(userEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        for(UserEntity userEntity : userEntities) {
            if(userEntity.getRoles().get(0).getName() != "ROLE_ADMIN2") {
                userDTOS.add(new UserDTO(userEntity));
            }
        }
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }


    @ApiOperation(value = "Create a company account.")
    @ApiResponse(code = 200, message = "create successfully")
    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<String> create(@RequestBody CompanyDTO companyDTO) {
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN2"));
        CompanyEntity companyEntity = new CompanyEntity(companyDTO);
        UserEntity userEntity = companyEntity.getUsers().get(0);
        userEntity.setRoles(roles);
        userEntity.setCompany(companyRepository.save(companyEntity));
        userRepository.save(userEntity);
        return new ResponseEntity("create successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Get modules of the company by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 204, message = "No Content"),
    })
    @GetMapping(path = "/getModules/{id}")
    public ResponseEntity<List<ModuleDTO>> getAllModules(@PathVariable Integer id) {
        CompanyEntity company = companyRepository.getOne(id);
        List<ModuleDTO> modules = new ArrayList<>();

        if(company.getTemplateEntity() == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        if(company.getTemplateEntity().getModules().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for(ModuleEntity moduleEntity:company.getTemplateEntity().getModules()) {
            modules.add(new ModuleDTO(moduleEntity));
        }

        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @ApiOperation("Delete a company by id")
    @ApiResponse(code = 200, message = "OK")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN1')")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        CompanyEntity companyToDelete = companyRepository.getOne(id);
        List<UserEntity> users = companyToDelete.getUsers();
        for(UserEntity user : users) {
            user.setRoles(null);
            user.setCompany(null);
            userRepository.delete(user);
        }
        TemplateEntity template = companyToDelete.getTemplateEntity();
        if(template != null) {
            Set<ModuleEntity> modules = template.getModules();
            for(ModuleEntity module : modules) {
                Set<IndicatorEntity> indicators = module.getIndicators();
                for(IndicatorEntity indicator : indicators) {
                    if(indicator.getLevel() == 3) {
                        indicatorDataRepository.delete(indicator.getIndicatorData());
                    }
                    indicator.setParent(null);
                    indicator.setModule(null);
                    indicator.setCompany(null);
                    indicatorRepository.delete(indicator);
                }
            }
            templateRepository.delete(template);
        }
        companyRepository.delete(companyToDelete);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }
}
