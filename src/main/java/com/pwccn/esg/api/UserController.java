package com.pwccn.esg.api;

import com.pwccn.esg.dto.ModuleDTO;
import com.pwccn.esg.dto.UserDTO;
import com.pwccn.esg.jwt.JWTLoginFilter;
import com.pwccn.esg.jwt.JwtTokenUtils;
import com.pwccn.esg.model.CompanyEntity;
import com.pwccn.esg.model.ModuleEntity;
import com.pwccn.esg.model.Role;
import com.pwccn.esg.model.UserEntity;
import com.pwccn.esg.repository.CompanyRepository;
import com.pwccn.esg.repository.RoleRepository;
import com.pwccn.esg.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.aspectj.weaver.patterns.HasMemberTypePatternForPerThisMatching;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CompanyRepository companyRepository;

    private AuthenticationManager authenticationManager;
    private JWTLoginFilter jwtLoginFilter;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @ApiOperation(value = "Level 2 admin create a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user has been created successfully."),
            @ApiResponse(code = 400, message = "The username has already exited."),
            @ApiResponse(code = 404, message = "The company doesn't exit.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @PostMapping("/create")
    public ResponseEntity<Integer> create(@RequestBody UserDTO userDTO) {
        CompanyEntity company = companyRepository.getOne(userDTO.getCompanyId());
        if(company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<UserEntity> users = company.getUsers();
        UserEntity userToCreate = new UserEntity(userDTO);
        for(UserEntity userEntity : users) {
           if(userEntity.getUsername() == userDTO.getUsername()) {
               return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
           }
        }
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(userDTO.getRole()));
        userToCreate.setRoles(roles);
        userToCreate.setCompany(company);
        UserEntity result = userRepository.save(userToCreate);
        return new ResponseEntity<>(result.getId(), HttpStatus.OK);
    }

    @ApiOperation(value = "Login with username and password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successfully."),
            @ApiResponse(code = 404, message = "The user doesn't exit."),
            @ApiResponse(code = 400, message = "The password is wrong."),
    })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(String username, String password, HttpServletResponse response) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            //密码不匹配
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } else {
                List<Role> roles = user.getRoles();
                String token = JwtTokenUtils.createToken(username, roles.get(0).getName());
                // 登录成功后，返回token到header里面
                response.addHeader("Authorization", JwtTokenUtils.TOKEN_PREFIX + token);

                UserDTO userDTO = new UserDTO(user);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);
            }
        }
    }


    @ApiOperation(value = "Level 2 delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user has been deleted successfully."),
            @ApiResponse(code = 404, message = "The user to delete doesn't exit."),

    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable Integer id) {
        UserEntity userToDelete = userRepository.getOne(id);
        if(userToDelete == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            CompanyEntity company = userToDelete.getCompany();
            company.getUsers().remove(userToDelete);
            userToDelete.setRoles(null);
            userToDelete.setCompany(null);
            userRepository.delete(userToDelete);
            return new ResponseEntity(HttpStatus.OK);
        }

    }

    @ApiOperation("Get a user's details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user's details have been gotten successfully."),
            @ApiResponse(code = 404, message = "The user doesn't exit."),
    })
    @GetMapping("/getDetails/{Cid}/{name}")
    public ResponseEntity<UserDTO> getDetails(@PathVariable Integer Cid, @PathVariable String name) {
        UserEntity userEntity = userRepository.findByUsernameAndCompany(name, companyRepository.getOne(Cid));
        if(userEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new UserDTO(userEntity), HttpStatus.OK);
    }

    @ApiOperation("Change password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The password has been changed successfully."),
            @ApiResponse(code = 404, message = "The user doesn't exit."),
    })
    @PutMapping("/changePassword")
    public ResponseEntity changePassword(Integer CId, String username, String newPassword) {
        UserEntity userEntity = userRepository.findByUsernameAndCompany(username, companyRepository.getOne(CId));
        if(userEntity == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        userEntity.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(userEntity);
        return new ResponseEntity(HttpStatus.OK);
    }


    @ApiOperation("Update a user's details")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The user's details have been updated successfully."),
            @ApiResponse(code = 404, message = "The user doesn't exit."),
            @ApiResponse(code = 409, message = "The username has been used"),
    })
    @PutMapping("/update/{username}")
    public ResponseEntity update(@RequestBody UserDTO userDTO, @PathVariable  String username) {
        UserEntity userEntity = userRepository.findByUsernameAndCompany(username,
                companyRepository.getOne(userDTO.getCompanyId()));

        if(userEntity == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(userRepository.findByUsername(userDTO.getUsername()) != null) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setTelephone(userDTO.getTelephone());
        userEntity.setContactor(userDTO.getContactor());
        userRepository.save(userEntity);
        return new ResponseEntity(HttpStatus.OK);
    }

}
