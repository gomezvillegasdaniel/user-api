package com.example.demo.controller;

import com.example.demo.data.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static com.example.demo.data.model.Role.USER;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseControllerTest {

    @Test
    @Order(1)
    void init() throws Exception {
        registerTestUser();
        loginTestUser();
        assertNotNull(token);
    }

    @Test
    @DisplayName("Get user")
    @Order(2)
    void getUserTest() throws Exception {
        MockHttpServletRequestBuilder request = get("/user/testUser")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token);
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", equalTo("User retrieved successfully")))
            .andExpect(jsonPath("$.object.email", equalTo("test@host.com")));
    }

    @Test
    @DisplayName("Update user")
    @Order(3)
    void updateUserTest() throws Exception {
        User user = User.builder()
            .username("testUser")
            .password("123456")
            .phone("(123) 456-7890")
            .email("updated_email@host.com")
            .role(USER)
            .firstname("Test")
            .lastname("User")
            .build();
        MockHttpServletRequestBuilder request = put("/user")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(user))
            .header("Authorization", "Bearer " + token);
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", equalTo("User updated successfully")))
            .andExpect(jsonPath("$.object.email", equalTo("updated_email@host.com")));
    }

    @Test
    @DisplayName("Delete user")
    @Order(4)
    void deleteUserTest() throws Exception {
        String message = deleteTestUser();
        assertEquals("User deleted successfully", message);
    }
}
