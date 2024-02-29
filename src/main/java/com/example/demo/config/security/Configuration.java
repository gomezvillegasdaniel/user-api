package com.example.demo.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
