package com.pwccn.esg.api;

import com.pwccn.esg.dto.TemplateDTO;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.Role;
import com.pwccn.esg.model.TemplateEntity;
import com.pwccn.esg.model.UserEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.RoleRepository;
import com.pwccn.esg.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/companies")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "Get the template of a company.")
    @GetMapping
    public ResponseEntity<TemplateDTO> search(@RequestBody CompanyEntity company) {
        TemplateEntity template = company.getTemplateEntity();
        return new ResponseEntity<>(new TemplateDTO(template), HttpStatus.OK);
    }


    @ApiOperation(value = "Get all companies' templates.")
    @GetMapping(value = "/all")
    public ResponseEntity<List<TemplateDTO>> get() {
        List<CompanyEntity> companies = companyRepository.findAll();
        List<TemplateDTO> templateDTOS = new ArrayList<>();
        for (CompanyEntity company : companies) {
            templateDTOS.add(new TemplateDTO(company.getTemplateEntity()));
        }

        return new ResponseEntity<>(templateDTOS,HttpStatus.OK);
    }

    @ApiOperation(value = "Create a company account.")
    @ApiResponse(code = 200, message = "create successfully")
    @PostMapping(value = "/create")
    public ResponseEntity<String> create(@RequestBody CompanyEntity company, String username, String password) {
        UserEntity userToCreate = new UserEntity();
        userToCreate.setUsername(username);
        userToCreate.setPassword(password);
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName("ROLE_ADMIN2"));
        userToCreate.setRoles(roles);
        userToCreate.setCompany(company);
        companyRepository.save(company);
        userRepository.save(userToCreate);
        return new ResponseEntity("create successfully", HttpStatus.OK);
    }
}
