package com.example.demo.controller;


import com.example.demo.data.model.Role;
import com.example.demo.data.model.User;
import com.google.gson.Gson;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseControllerTest {

    public static String TOKEN;

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public Gson gson;

    private static final User USER = User.builder()
        .username("testUser")
        .password("123456")
        .phone("(123) 456-7890")
        .email("test@host.com")
        .role(Role.USER)
        .firstname("Test")
        .lastname("User")
        .build();

    public String registerTestUser() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(USER));
        String response = mockMvc.perform(request).andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return gson.fromJson(response, Map.class).get("message").toString();
    }

    public void loginTestUser() throws Exception {
        User user = User.builder().username("testUser").password("123456").build();
        MockHttpServletRequestBuilder request = post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(gson.toJson(user));
        String response = mockMvc.perform(request)
//            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", equalTo("User successfully authenticated")))
            .andReturn().getResponse().getContentAsString();
        TOKEN = gson.fromJson(response, Map.class).get("token").toString();
        USER.setToken(TOKEN);
    }

    public String deleteTestUser() throws Exception {
        MockHttpServletRequestBuilder request = delete("/user/testUser")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + TOKEN);
        String response = mockMvc.perform(request)
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.message", equalTo("User deleted successfully")))
            .andReturn().getResponse().getContentAsString();
        return gson.fromJson(response, Map.class).get("message").toString();
    }

    public String logoutUser() throws Exception {
        MockHttpServletRequestBuilder request = post("/user/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + TOKEN)
            .content(gson.toJson(USER));
        String response = mockMvc.perform(request)
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return gson.fromJson(response, Map.class).get("message").toString();
    }
}
