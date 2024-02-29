package com.example.demo.constant;

public class SecurityConstant {
    public static final String[] PUBLIC_URLS = {
        "/login/**",
        "/register/**",
        "/h2-console/**"
    };
    public static final String FORBIDDEN_MESSAGE = "You don't have permission to access this resource";
    public static final String UNAUTHORIZED_MESSAGE = "Your authentication credentials are not valid";
    public static final String USER_ALREADY_EXISTS_MESSAGE = "User with this username, email or phone already exists";
    public static final String USER_REGISTERED_SUCCESSFULLY_MESSAGE = "User registered successfully";
    public static final String USER_NOT_FOUND_AUTHENTICATING_MESSAGE = "User not found while authenticating";
    public static final String USER_SUCCESSFULLY_AUTHENTICATED_MESSAGE = "User successfully authenticated";

    private SecurityConstant() {}
}
