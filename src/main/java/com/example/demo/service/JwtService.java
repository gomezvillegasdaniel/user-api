package com.example.demo.service;

import com.example.demo.data.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET_KEY = "22cb98e624e8920be075d27983853f8d86ed636826c23bfc752647510ebf110d";

    private static final List<String> BLACK_LIST = new ArrayList<>();

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private void addTokenToBlackList(String token) {
        BLACK_LIST.add(token);
    }

    public void invalidateToken(String token) {
        addTokenToBlackList(token);
    }

    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token) && !BLACK_LIST.contains(token);
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
        return Jwts.builder()
            .subject(user.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)) // 1 hour
            .signWith(getSigninKey())
            .compact();
    }

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
