package com.example.demo.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "JWT_TOKEN")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtToken {

    @Id
    private String token;

}
