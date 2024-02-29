package com.example.demo.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageObjectResponse<T> {

    private final String message;
    private T object;

}
