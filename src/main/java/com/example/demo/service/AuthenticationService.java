package com.example.demo.service;

import com.example.demo.data.model.AuthenticationResponse;
import com.example.demo.data.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.demo.constant.SecurityConstant.USER_NOT_FOUND_AUTHENTICATING_MESSAGE;
import static com.example.demo.constant.SecurityConstant.USER_SUCCESSFULLY_AUTHENTICATED_MESSAGE;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationService.class.getName());

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // TODO: normalize all the responses to be HttpResponse

    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public AuthenticationResponse authenticate(User request) {
        LOGGER.log(Level.INFO, "Authenticating user...");

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (AuthenticationException exception) {
            LOGGER.log(Level.SEVERE, "User authentication failed", exception);
        }

        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_AUTHENTICATING_MESSAGE));

        LOGGER.log(Level.INFO, "User authenticated successfully");
        return new AuthenticationResponse(USER_SUCCESSFULLY_AUTHENTICATED_MESSAGE, jwtService.generateToken(user));
    }

    @Retryable(retryFor = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public void register(User request) {
        LOGGER.log(Level.INFO, "Registering user...");
        var user = User.builder()
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .phone(request.getPhone())
            .role(request.getRole())
            .build();
        userRepository.save(user);
        LOGGER.log(Level.INFO, "User registered successfully");
    }
}
