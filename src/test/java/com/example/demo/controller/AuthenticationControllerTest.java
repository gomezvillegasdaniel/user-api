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
        assertNotNull(TOKEN);
    }

    @Test
    @DisplayName("Logout user")
    @Order(3)
    void logoutUserTest() throws Exception {
        String message = logoutUser();
        assertEquals("User successfully logged out", message);
    }

//    @Test
//    @DisplayName("Clean up")
//    @Order(4)
//    void cleanUp() throws Exception {
//        registerTestUser();
//        loginTestUser();
//        String message = deleteTestUser();
//        assertEquals("User deleted successfully", message);
//    }

}
