package com.example.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

@org.springframework.context.annotation.Configuration
@EnableRetry
public class Configuration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
