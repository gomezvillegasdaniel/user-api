package com.example.demo.service;

import com.example.demo.data.model.MessageObjectResponse;
import com.example.demo.data.model.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UtilityService {

    public ResponseEntity<Object> buildResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new MessageResponse(message), httpStatus);
    }

    public <E> ResponseEntity<Object> buildResponse(HttpStatus httpStatus, String message, E entity) {
        return new ResponseEntity<>(new MessageObjectResponse<>(message, entity), httpStatus);
    }

}
