package com.example.demo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class AuthenticationControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("Register user")
    @Order(1)
    void registerUserTest() throws Exception {
        String message = registerTestUser();
        assertEquals("User registered successfully", message);
    }

    @Test
    @DisplayName("Login user")
    @Order(2)
    void loginUserTest() throws Exception {
        loginTestUser();
        assertNotNull(token);
    }

    @Test
    @DisplayName("Clean up")
    @Order(3)
    void cleanUp() throws Exception {
        String message = deleteTestUser();
        assertEquals("User deleted successfully", message);
    }

}
