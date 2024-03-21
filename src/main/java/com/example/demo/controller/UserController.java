package com.example.demo.controller;

import com.example.demo.data.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.example.demo.constant.UserConstant.USER_NOT_FOUND_MESSAGE;

@RestController
@RequestMapping("user")
@Validated
public class UserController {

    private final UserService userService;
    private final UtilityService utilService;

    public UserController(UserService userService, UtilityService utilService) {
        this.userService = userService;
        this.utilService = utilService;
    }

    @DeleteMapping(value = "/{userName}", produces = "application/json")
    public ResponseEntity<Object> deleteUser(@PathVariable String userName) {
        Optional<User> existingUser = userService.findByUserName(userName);
        if (existingUser.isEmpty()) {
            return utilService.buildResponse(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE);
        }
        userService.deleteUser(existingUser.get());
        return utilService.buildResponse(HttpStatus.OK, "User deleted successfully");
    }

    @PutMapping(produces = "application/json")
    public ResponseEntity<Object> updateUser(@RequestBody User user) {
        userService.findByUserName(user.getUsername())
            .ifPresentOrElse(userService::updateUser, () -> utilService.buildResponse(HttpStatus.NOT_FOUND, USER_NOT_FOUND_MESSAGE));
        return utilService.buildResponse(HttpStatus.OK, "User updated successfully");
    }

    @GetMapping(value = "/{userName}", produces = "application/json")
    public ResponseEntity<Object> getUser(@PathVariable String userName) {
        Optional<User> existingUser = userService.findByUserName(userName);
        User user = existingUser.orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE));
        return utilService.buildResponse(HttpStatus.OK,  "User retrieved successfully", user);
    }

    @GetMapping(produces = "application/json")
    public List<User> getUsers() {
        return userService.findAll();
    }

}
