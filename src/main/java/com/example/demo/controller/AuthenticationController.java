package com.example.demo.controller;

import com.example.demo.data.constraint.UniqueUserConstraint;
import com.example.demo.data.model.User;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.constant.SecurityConstant.USER_REGISTERED_SUCCESSFULLY_MESSAGE;

@RestController
@Validated
public class AuthenticationController {

    private final AuthenticationService authService;
    private final UtilityService utilityService;

    public AuthenticationController(AuthenticationService authService, UtilityService utilityService) {
        this.authService = authService;
        this.utilityService = utilityService;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user) {
        return ResponseEntity.ok(authService.authenticate(user));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @UniqueUserConstraint User user) {
        authService.register(user);
        return utilityService.buildResponse(HttpStatus.OK, USER_REGISTERED_SUCCESSFULLY_MESSAGE);
    }
}
