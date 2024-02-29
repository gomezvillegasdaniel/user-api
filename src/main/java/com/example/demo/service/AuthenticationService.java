package com.example.demo.service;

import com.example.demo.data.model.AuthenticationResponse;
import com.example.demo.data.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.demo.constant.SecurityConstant.USER_NOT_FOUND_AUTHENTICATING_MESSAGE;
import static com.example.demo.constant.SecurityConstant.USER_SUCCESSFULLY_AUTHENTICATED_MESSAGE;

@Service
public class AuthenticationService {

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

    public AuthenticationResponse authenticate(User request) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_AUTHENTICATING_MESSAGE));
        return new AuthenticationResponse(USER_SUCCESSFULLY_AUTHENTICATED_MESSAGE, jwtService.generateToken(user));
    }

    public void register(User request) {
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
    }
}
