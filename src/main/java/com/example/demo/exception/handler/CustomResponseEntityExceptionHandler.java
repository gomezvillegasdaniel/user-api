package com.example.demo.exception.handler;

import com.example.demo.service.UtilityService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler {

    private final UtilityService utilService;

    public CustomResponseEntityExceptionHandler(UtilityService utilService) {
        this.utilService = utilService;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolations(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        String message = "Constraint violation not handled";
        if (!constraintViolations.isEmpty()) {
            message = constraintViolations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        }
        return utilService.buildResponse(HttpStatus.BAD_REQUEST, message);
    }

}

