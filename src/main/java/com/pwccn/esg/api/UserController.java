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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/user")
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
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @PostMapping("/create")
    public ResponseEntity<String> creat(@RequestBody UserDTO userDTO) {
//        UserEntity userToCreate = new UserEntity();
//        userToCreate.setUsername(username);
//        userToCreate.setPassword(bCryptPasswordEncoder.encode(password));
//        CompanyEntity companyEntity = companyRepository.findByName(company);
//        userToCreate.setCompany(companyEntity);
//        Role role = roleRepository.findByName(userType);
//        List<Role> roles = new ArrayList<>();
//        roles.add(role);
//        userToCreate.setRoles(roles);
//        userRepository.save(userToCreate);
//        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
        CompanyEntity company = companyRepository.getOne(userDTO.getId());
        if(company == null) {
            return new ResponseEntity<>("The company is not exiting", HttpStatus.NOT_FOUND);
        }
        List<UserEntity> users = company.getUsers();
        UserEntity userToCreate = new UserEntity(userDTO);
        for(UserEntity userEntity : users) {
           if(userEntity.getUsername() == userDTO.getUsername()) {
               return new ResponseEntity<>("The user " + userDTO.getUsername() + "with role " +
                       userDTO.getRole() + "has existed", HttpStatus.BAD_REQUEST);
           }
        }
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findByName(userDTO.getRole()));
        userToCreate.setRoles(roles);
        userToCreate.setCompany(company);
        userRepository.save(userToCreate);
        return new ResponseEntity<>("Create successfully", HttpStatus.OK);
    }

    @ApiOperation(value = "Login with username and password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request"),
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
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request"),

    })
    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(String userName) {
        UserEntity userToDelete = userRepository.findByUsername(userName);
        if(userToDelete == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        } else {
            CompanyEntity company = userToDelete.getCompany();
            company.getUsers().remove(userToDelete);
            userToDelete.setRoles(null);
            userToDelete.setCompany(null);
            userRepository.delete(userToDelete);
            return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
        }

    }



//    @ApiOperation(value = "Get all modules user can see")
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "OK"),
//            @ApiResponse(code = 404, message = "Not Found"),
//    })
//    @GetMapping("/getAllModules")
//    public ResponseEntity<Set<ModuleDTO>> getModules(String username) {
//
//        UserEntity user = userRepository.findByUsername(username);
//        CompanyEntity company = user.getCompany();
//
//        Set<ModuleEntity> moduleEntities = company.getTemplateEntity().getModules();
//        if(moduleEntities.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        } else {
//            Set<ModuleDTO> result = new HashSet<>();
//            for(ModuleEntity moduleEntity : moduleEntities) {
//                result.add(new ModuleDTO(moduleEntity));
//            }
//            return new ResponseEntity<>(result, HttpStatus.OK);
//        }
//    }

    //改一个用户还没写！！！
}
