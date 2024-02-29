package com.example.demo.controller;

import com.example.demo.data.model.MessageObjectResponse;
import com.example.demo.data.model.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class AdminController {

    @GetMapping(produces = "application/json")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> admin() {
        return buildResponse(HttpStatus.OK, "Admin area");
    }

    private ResponseEntity<Object> buildResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new MessageResponse(message), httpStatus);
    }

    private <E> ResponseEntity<Object> buildResponse(HttpStatus httpStatus, String message, E entity) {
        return new ResponseEntity<>(new MessageObjectResponse<>(message, entity), httpStatus);
    }
}
