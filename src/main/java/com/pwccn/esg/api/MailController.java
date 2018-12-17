package com.pwccn.esg.api;

import com.pwccn.esg.mail.MailService;
import com.pwccn.esg.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Random;

@Controller
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation("Users reset their password using email check code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The check code has been sent"),
            @ApiResponse(code = 404, message = "The email is not exit"),
    })
    @GetMapping("/getCheckCode/{email}")
    public ResponseEntity<String> getCheckCode(@PathVariable String email) {
        String checkCode = String.valueOf(new Random().nextInt(899999)+100000);
        String message = "您的验证码为：" +checkCode;
        if(userRepository.findByEmail(email) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        mailService.sendSimpleMail(email, "您正在使用验证码修改密码", message);
        return new ResponseEntity<>(checkCode, HttpStatus.OK);
    }
}
