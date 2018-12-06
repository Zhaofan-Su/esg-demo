package com.pwccn.esg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","OPTIONS","PUT")
                .allowedHeaders("Content-Type","X-Requseted-With","accept",
                        "Origin", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers","Authorization")
                .exposedHeaders("Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials","Authorization")
                .allowCredentials(true).maxAge(3600);
    }
}
