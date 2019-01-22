package com.pwccn.esg.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","POST","OPTIONS","PUT","DELETE")
                .allowedHeaders("Content-Type","X-Requested-With","accept",
                        "Origin", "Access-Control-Request-Method",
                        "Access-Control-Request-Headers","Authorization")
                .exposedHeaders("Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials","Authorization")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/index.html").setViewName("index");
    }
}
