package com.pwccn.esg.api;

import com.pwccn.esg.dto.UserDTO;
import com.pwccn.esg.mail.MailService;
import com.pwccn.esg.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(path = "/api/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    private Map<String , String> userChek = new HashMap<>();

    @ApiOperation("Users reset their password using email check code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The check code has been sent"),
            @ApiResponse(code = 404, message = "The email is not exit"),
    })
    @GetMapping("/getCheckCode/{email}")
    public ResponseEntity<UserDTO> getCheckCode(@PathVariable String email) {
        String checkCode = String.valueOf(new Random().nextInt(899999)+100000);
        String message = "您的验证码为：" +checkCode;
        if(userRepository.findByEmail(email) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userChek.put(userRepository.findByEmail(email).getUsername(), checkCode);
        mailService.sendSimpleMail(email, "您正在使用验证码修改密码", message);
        return new ResponseEntity<>(new UserDTO(userRepository.findByEmail(email)), HttpStatus.OK);
    }

    @ApiOperation("Check the check code.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The check code is right."),
            @ApiResponse(code = 400, message = "The check code is wrong."),
    })
    @PostMapping("/checkCode/{checkCode}")
    public ResponseEntity<UserDTO> checkCode(@RequestBody UserDTO userDTO, @PathVariable String checkCode) {
        if(userChek.get(userDTO.getUsername()).equals(checkCode)) {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
