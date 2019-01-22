package com.pwccn.esg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableAutoConfiguration
public class ESGApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ESGApplication.class, args);
    }


}
