package com.pwccn.esg.api;

import com.pwccn.esg.dto.ModuleDTO;
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

    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @ApiOperation(value = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
    })
    @PostMapping("/create")
    public ResponseEntity<String> creat(String username, String password, String userType, String company) {
        UserEntity userToCreate = new UserEntity();
        userToCreate.setUsername(username);
        userToCreate.setPassword(bCryptPasswordEncoder.encode(password));
        CompanyEntity companyEntity = companyRepository.findByName(company);
        userToCreate.setCompany(companyEntity);
        Role role = roleRepository.findByName(userType);
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        userToCreate.setRoles(roles);
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
    public ResponseEntity<String> login(String username, String password, HttpServletResponse response) {
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            return new ResponseEntity<>("No user", HttpStatus.NOT_FOUND);
        } else {
            //密码不匹配
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>("Wrong password", HttpStatus.BAD_REQUEST);
            } else {
                List<Role> roles = user.getRoles();
                String token = JwtTokenUtils.createToken(username, roles.get(0).getName());
                // 登录成功后，返回token到header里面
                response.addHeader("Authorization", JwtTokenUtils.TOKEN_PREFIX + token);

                String auth = user.getAuthorities().toString();
                auth = auth.substring(1,auth.length()-1);
                return new ResponseEntity<>(auth, HttpStatus.OK);
            }
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN2')")
    @ApiOperation(value = "Delete a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 400, message = "Bad Request"),

    })
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(String userName) {
        UserEntity userToDelete = userRepository.findByUsername(userName);
        if(userToDelete == null) {
            return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
        } else {
            if(userToDelete.getRoles().get(0).getName() == "ROLE_ADMIN2") {
                return new ResponseEntity<>("Don't have the authority.", HttpStatus.BAD_REQUEST);
            } else {
                userRepository.delete(userToDelete);
                return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
            }
        }

    }

    @ApiOperation(value = "Get all modules user can see")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @GetMapping("/getAllModules")
    public ResponseEntity<Set<ModuleDTO>> getModules(String username) {

        UserEntity user = userRepository.findByUsername(username);
        CompanyEntity company = user.getCompany();

        Set<ModuleEntity> moduleEntities = company.getTemplateEntity().getModules();
        if(moduleEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            Set<ModuleDTO> result = new HashSet<>();
            for(ModuleEntity moduleEntity : moduleEntities) {
                result.add(new ModuleDTO(moduleEntity));
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    //改一个用户还没写！！！
}
