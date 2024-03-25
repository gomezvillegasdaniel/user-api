package com.example.demo.service;

import com.example.demo.data.model.JwtToken;
import com.example.demo.data.model.User;
import com.example.demo.repository.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;


@Service
public class JwtService {

    private final JwtTokenRepository jwtTokenRepository;
    private final UserService userService;

    public JwtService(JwtTokenRepository jwtTokenRepository, UserService userService) {
        this.jwtTokenRepository = jwtTokenRepository;
        this.userService = userService;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) &&
            !isTokenExpired(token) &&
            jwtTokenRepository.findByToken(token).isPresent();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getSigninKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public String generateToken(User user) {
        String token = Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000)) // 15 minutes
            .signWith(getSigninKey())
            .compact();
        jwtTokenRepository.save(new JwtToken(token));
        user.setToken(token);
        userService.updateUser(user);
        return token;
    }

    private SecretKey getSigninKey() {
        String secretKey = System.getenv("JWT_TOKEN_SECRET_KEY");
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void cleanTokensWhiteList() {
        jwtTokenRepository.findAll().clear();
    }

    public void invalidateToken(String token) {
        jwtTokenRepository.findByToken(token).ifPresent(jwtTokenRepository::delete);
    }
}
