package com.example.demo.repository;

import com.example.demo.data.model.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, String> {

    Optional<JwtToken> findByToken(String token);
}
