package com.example.demo.controller;


import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;

class RetryFromAuthenticationTest extends BaseControllerTest {

//    @Autowired
//    private RetryTemplate retryTemplate;

//    @Test
//    @Order(1)
//    void init() throws Exception {
//        registerTestUser();
//    }

//    @Test
//    @Order(2)
//    @Disabled
//    void retryTest() throws Exception {
//        Stream.generate(() -> 1)
//            .parallel()
//            .limit(1000)
//            .forEach(retry -> {
//                try {
//                    loginTestUser();
//                } catch (Exception e) {
//                }
//            });
//        retryTemplate.execute(context -> {
//            assertEquals(3, context.getRetryCount());
//            return null;
//        });
//    }
}
