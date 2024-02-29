package com.example.demo.config.security;

import com.example.demo.config.security.filter.JwtAuthenticationFilter;
import com.example.demo.data.model.HttpResponse;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.OutputStream;
import java.time.Instant;
import java.util.Date;

import static com.example.demo.constant.SecurityConstant.FORBIDDEN_MESSAGE;
import static com.example.demo.constant.SecurityConstant.PUBLIC_URLS;
import static com.example.demo.constant.SecurityConstant.UNAUTHORIZED_MESSAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfiguration(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter, ObjectMapper objectMapper) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req.requestMatchers(PUBLIC_URLS).permitAll().anyRequest().authenticated())
            .userDetailsService(userService)
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headerConf -> headerConf.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .exceptionHandling(config -> {
                config.accessDeniedHandler(accessDeniedHandler());
                config.authenticationEntryPoint(authenticationEntryPoint());
            })
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(Date.from(Instant.now()))
                .httpStatusCode(UNAUTHORIZED.value())
                .httpStatus(UNAUTHORIZED)
                .reason(UNAUTHORIZED.getReasonPhrase())
                .message(UNAUTHORIZED_MESSAGE)
                .build();
            response.setStatus(UNAUTHORIZED.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            OutputStream outputStream = response.getOutputStream();
            objectMapper.writeValue(outputStream, httpResponse);
            outputStream.flush();
        };
    }

    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(Date.from(Instant.now()))
                .httpStatusCode(FORBIDDEN.value())
                .httpStatus(FORBIDDEN)
                .reason(FORBIDDEN.getReasonPhrase())
                .message(FORBIDDEN_MESSAGE)
                .build();
            response.setStatus(FORBIDDEN.value());
            response.setContentType(APPLICATION_JSON_VALUE);
            OutputStream outputStream = response.getOutputStream();
            objectMapper.writeValue(outputStream, httpResponse);
            outputStream.flush();
        };
    }

}
